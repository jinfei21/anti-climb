package com.yjfei.antibot.engine.rule;

import com.yjfei.antibot.common.RiskType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StrategyResult {

    private long strategyId;

    private List<RuleResult> ruleResultList;

    private RiskType type;

    private int score;

    public StrategyResult(List<RuleResult> ruleResultList,RiskType type){
        this.ruleResultList = ruleResultList;
        this.type = type;
    }
}
