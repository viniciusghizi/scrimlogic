package br.com.viniciusghizi.scrimlogic.strategy.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.viniciusghizi.scrimlogic.domain.dto.MatchEventDTO;
import br.com.viniciusghizi.scrimlogic.domain.dto.ProcessedMatchMetrics;
import br.com.viniciusghizi.scrimlogic.rules.ScrimRule;
import br.com.viniciusghizi.scrimlogic.strategy.ScrimStrategy;

@Component
public class RocketLeagueStrategy implements ScrimStrategy{

    private final List<ScrimRule> rules;
    
    public RocketLeagueStrategy(List<ScrimRule> allRules) {
        this.rules = allRules.stream()
            .filter(r -> r.getClass().getSimpleName().contains("RL"))
            .toList();
    }

    @Override
    public boolean supports(String game) {
        return "RL".equals(game);
    }

    @Override
    public ProcessedMatchMetrics process(MatchEventDTO event) {
        var data = event.data();
        
        int goals = data.path("goals").asInt(0);
        int saves = data.path("saves").asInt(0);
        int shots = data.path("shots").asInt(0);

        Map<String, Object> stats = Map.of(
            "goals", goals,
            "saves", saves,
            "shots", shots,
            "efficiency", shots > 0 ? (double) goals / shots : 0.0
        );

        Map<String, Object> analysis = new HashMap<>();
        rules.forEach(rule -> rule.evaluate(data, analysis));

        return new ProcessedMatchMetrics(
            event.matchId(), event.game(), event.mode(), 
            LocalDateTime.now(), stats, analysis
        );
    }
}
