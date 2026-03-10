package br.com.viniciusghizi.scrimlogic.rules.impl.lol;

import br.com.viniciusghizi.scrimlogic.rules.ScrimRule;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class ObjectiveDominanceRule implements ScrimRule {
    @Override
    public void evaluate(JsonNode data, Map<String, Object> analysis) {
        int barons = data.get("team_100").path("barons").asInt() + 
                     data.get("team_200").path("barons").asInt();
        
        analysis.put("is_objective_heavy", barons >= 2);
    }
}