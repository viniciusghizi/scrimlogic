package br.com.viniciusghizi.scrimlogic.rules;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

public interface ScrimRule {
    void evaluate(JsonNode data, Map<String, Object> analysis);
}