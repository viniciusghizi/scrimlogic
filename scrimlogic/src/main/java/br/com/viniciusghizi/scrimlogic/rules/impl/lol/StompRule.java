package br.com.viniciusghizi.scrimlogic.rules.impl.lol;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.viniciusghizi.scrimlogic.rules.ScrimRule;

@Component
public class StompRule implements ScrimRule {
    @Override
    public void evaluate(JsonNode data, Map<String, Object> analysis) {
        int g100 = data.get("team_100").path("gold").asInt();
        int g200 = data.get("team_200").path("gold").asInt();
        analysis.put("is_stomp", Math.abs(g100 - g200) > 10000);
    }
}