package br.com.viniciusghizi.scrimlogic.domain.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ProcessedMatchMetrics(String matchId,
    String game,
    String mode,
    LocalDateTime processedAt,
    Map<String, Object> teamStats, 
    Map<String, Object> analysis   
) {}