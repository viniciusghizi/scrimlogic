package br.com.viniciusghizi.scrimlogic.domain.dto;

import java.util.List;

public record IngestionBatch(
    String batchId,
    String source,
    List<MatchEventDTO> matches
) {}
