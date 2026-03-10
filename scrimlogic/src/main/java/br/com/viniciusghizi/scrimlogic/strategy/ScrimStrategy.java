package br.com.viniciusghizi.scrimlogic.strategy;

import br.com.viniciusghizi.scrimlogic.domain.dto.MatchEventDTO;
import br.com.viniciusghizi.scrimlogic.domain.dto.ProcessedMatchMetrics;

public interface ScrimStrategy {
    boolean supports(String game);
    ProcessedMatchMetrics process(MatchEventDTO event);
}   