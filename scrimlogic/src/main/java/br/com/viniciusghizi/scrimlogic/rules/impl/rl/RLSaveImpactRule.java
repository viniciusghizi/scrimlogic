package br.com.viniciusghizi.scrimlogic.rules.impl.rl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.viniciusghizi.scrimlogic.rules.ScrimRule;

@Component
public class RLSaveImpactRule implements ScrimRule {
    
    @Override
    public void evaluate(JsonNode data, Map<String, Object> analysis) {
        int saves = data.path("saves").asInt(0);
        
        if (saves >= 3) {
            analysis.put("defensive_performance", "Defensive Wall");
        } else {
            analysis.put("defensive_performance", "Standard");
        }
    }
}
