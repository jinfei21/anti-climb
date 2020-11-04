package com.yjfei.antibot.engine.rule;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RuleResult {
    private long ruleId;

    private int score;

    public RuleResult(long ruleId,int score){
        this.ruleId = ruleId;
        this.score = score;
    }
}
