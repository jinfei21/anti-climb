package com.yjfei.antibot.engine.rule;

import com.yjfei.antibot.common.RiskType;
import com.yjfei.antibot.engine.Calculator;
import com.yjfei.antibot.engine.Context;
import lombok.Getter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Strategy implements Calculator<StrategyResult> {

    @Getter
    private final long id;

    @Getter
    private final List<Rule> ruleList;

    private final int midScore;

    private final int highScore;

    public Strategy(long id,List<Rule> ruleList, int midScore, int highScore){
        this.id = id;
        ruleList.sort(Comparator.comparingInt(Rule::getPriority));
        this.ruleList = Collections.unmodifiableList(ruleList);
        this.highScore = highScore;
        this.midScore = midScore;
    }

    @Override
    public StrategyResult calculate(Context context) {
        List<RuleResult> ruleResults = ruleList.stream().map(rule -> rule.calculate(context))
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList());

        int sum = ruleResults.stream().map(ruleResult -> ruleResult.getScore()).reduce(Integer::sum).orElse(0);

        StrategyResult strategyResult = new StrategyResult();

        if (sum >= highScore){
            strategyResult.setType(RiskType.HIGH);
        }else if (sum>=midScore){
            strategyResult.setType(RiskType.MID);
        }else{
            strategyResult.setType(RiskType.LOW);
        }
        strategyResult.setRuleResultList(ruleResults);
        strategyResult.setScore(sum);

        return strategyResult;
    }
}
