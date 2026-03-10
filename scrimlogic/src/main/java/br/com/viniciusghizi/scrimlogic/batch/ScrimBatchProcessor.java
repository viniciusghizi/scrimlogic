package br.com.viniciusghizi.scrimlogic.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.viniciusghizi.scrimlogic.domain.dto.MatchEventDTO;
import br.com.viniciusghizi.scrimlogic.domain.dto.ProcessedMatchMetrics;
import br.com.viniciusghizi.scrimlogic.domain.entity.MatchEntity;
import br.com.viniciusghizi.scrimlogic.repository.MatchRepository;
import br.com.viniciusghizi.scrimlogic.strategy.ScrimStrategy;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScrimBatchProcessor {

    private final BlockingQueue<ProcessedMatchMetrics> buffer = new LinkedBlockingQueue<>();
    private final List<ScrimStrategy> strategies;
    private final MatchRepository repository;
    
    private final Semaphore semaphore = new Semaphore(3);

    public ScrimBatchProcessor(List<ScrimStrategy> strategies, MatchRepository repository) {
        this.strategies = strategies;
        this.repository = repository;
    }

    public void processEvent(MatchEventDTO event) {
        strategies.stream()
            .filter(s -> s.supports(event.game()))
            .findFirst()
            .map(s -> s.process(event))
            .ifPresentOrElse(
                buffer::add, 
                () -> 
                log.error("Nenhuma estratégia para: [ {} ] ", event.game())
            );
    }

    @Scheduled(fixedDelay = 5000)
    public void flushToDatabase() {
        if (buffer.isEmpty()) return;
        
        try {
            if (semaphore.tryAcquire()) {
                log.info("Iniciando persistência de lote com [ {} ] itens", buffer.size());
                try {
                    performFlush();
                } finally {
                    semaphore.release();
                }
            } else {
                log.warn("Processamento ocupado (Semaphore bloqueado). Aguardando próximo ciclo.");
            }
        } catch (Exception e) {
            log.error("Erro crítico ao realizar Flush no banco de dados: [ {} ]", e.getMessage(), e);
        }
    }

    private void performFlush() {
        List<ProcessedMatchMetrics> batch = new ArrayList<>();
        buffer.drainTo(batch);

        List<MatchEntity> entities = batch.stream().map(m -> {
            MatchEntity entity = new MatchEntity();
            entity.setMatchId(m.matchId());
            entity.setGame(m.game());
            entity.setMode(m.mode());
            entity.setProcessedAt(m.processedAt());
            entity.setAnalysis(m.analysis());
            
            log.info("salvando MatchID [ {} ] .",m.matchId());
            
            return entity;
        }).toList();

        repository.saveAll(entities);
        log.info("Batch persistido com sucesso no Postgres.");
    }
}