package br.com.viniciusghizi.scrimlogic.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MatchEventDTO(
    String game,        
    String matchId,
    String mode,
    com.fasterxml.jackson.databind.JsonNode data 
) {}