package com.yjfei.antibot.engine.rule;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * 条件之间的关系
 */
public enum Relation {
    AND("&",RuleCondition::and),
    OR("|",RuleCondition::or),
    XOR("X",RuleCondition::xor);

    @Getter
    private final String symbol;

    private final Rel rel;

    Relation(String symbol,Rel rel){
        this.symbol = symbol;
        this.rel = rel;
    }

    public static Optional<Relation> of(String relation){
        return Arrays.stream(values())
                .filter(r -> r.symbol.equals(relation))
                .findFirst();
    }

    public RuleCondition apply(RuleCondition first,RuleCondition second){
        return this.rel.apply(first,second);
    }

    interface Rel extends BiFunction<RuleCondition,RuleCondition,RuleCondition>{

    }

}
