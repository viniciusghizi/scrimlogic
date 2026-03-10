package br.com.viniciusghizi.scrimlogic.service;

import br.com.viniciusghizi.scrimlogic.batch.ScrimBatchProcessor;
import br.com.viniciusghizi.scrimlogic.domain.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;



@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final ScrimBatchProcessor processor;
    private final ObjectMapper objectMapper;

    @Value("${scrimlogic.inbox.path}")
    private String inboxPath;

    /**
     * Monitora a pasta de entrada a cada 10 segundos.
     * Utiliza threads virtuais (configuradas no AsyncConfig) para não bloquear o sistema.
     */
    
    @Scheduled(fixedDelay = 10000)
    public void scan() {
        File folder = new File(inboxPath);
        
        if (!folder.exists() || !folder.isDirectory()) {
            log.error("Pasta de entrada não encontrada: {}", inboxPath);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null || files.length == 0) {
            log.debug("Nenhum arquivo novo para processar na pasta: {}", inboxPath);
            return;
        }

        log.info("Encontrados[  {} ] arquivos na pasta de ingestão.", files.length);

        for (File file : files) {
            processFile(file);
        }
    }

    private void processFile(File file) {
        log.info("Iniciando processamento do arquivo: {}", file.getName());
        
        try {
            IngestionBatch batch = objectMapper.readValue(file, IngestionBatch.class);
            
            log.info("Batch [  {} ] (origem: [ {} ] ) contém[  {} ] partidas.", 
                     batch.batchId(), batch.source(), batch.matches().size());

            batch.matches().forEach(processor::processEvent);

            if (file.delete()) {
                log.info("Arquivo [ {} ] processado e removido com sucesso.", file.getName());
            } else {
                log.warn("Arquivo [ {} ] processado, mas não foi possível removê-lo.", file.getName());
            }
            
        } catch (IOException e) {
            log.error("Falha ao ler o arquivo [ {} ]: [  {} ]", file.getName(), e.getMessage());
        } catch (Exception e) {
            log.error("Erro inesperado ao processar o batch no arquivo [ {} ]: [ {} ]", file.getName(), e.getMessage());
        }
    }
}