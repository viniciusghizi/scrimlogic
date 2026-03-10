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
public class LolRankedStrategy implements ScrimStrategy {
    private final List<ScrimRule> rules;

    public LolRankedStrategy(List<ScrimRule> rules) {
        this.rules = rules;
    }

    @Override
    public boolean supports(String game) { return "LOL".equalsIgnoreCase(game); }

    @Override
    public ProcessedMatchMetrics process(MatchEventDTO event) {
        var data = event.data();
        
        int duration = data.get("duration").asInt();
        int gold100 = data.get("team_100").path("gold").asInt();
        int gold200 = data.get("team_200").path("gold").asInt();

        Map<String, Object> stats = Map.of(
            "gold_diff", Math.abs(gold100 - gold200), 
            "duration_min", duration / 60
        );

        Map<String, Object> analysis = new HashMap<>();
        rules.forEach(rule -> rule.evaluate(data, analysis));

        return new ProcessedMatchMetrics(
            event.matchId(), event.game(), event.mode(), 
            LocalDateTime.now(), stats, analysis
        );
    }
}