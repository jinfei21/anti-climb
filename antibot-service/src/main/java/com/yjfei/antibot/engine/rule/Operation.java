package com.yjfei.antibot.engine.rule;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum Operation {
    EQUAL("=", (BiOp) RuleVariable::same),
    NOT_EQUAL("!=", (BiOp) RuleVariable::notSame),
    GREATER_THEN(">", (BiOp) RuleVariable::greaterThan),
    GREATER_THEN_EQUAL(">=", (BiOp) RuleVariable::greaterThanEqual),
    LESS_THEN("<", (BiOp) RuleVariable::lessThan),
    LESS_THEN_EQUAL("<=", (BiOp) RuleVariable::lessThanEqual),
    IS_TRUE("T", (OneOp) RuleVariable::isTrue),
    IS_FALSE("F", (OneOp) RuleVariable::isFalse),
    IN("IN", (BiOp) RuleVariable::in),
    NOT_IN("NN", (BiOp) RuleVariable::notIn);


    private final String symbol;
    private final Op func;


    Operation(String symbol,Op func){
        this.symbol = symbol;
        this.func = func;
    }

    public static Optional<Operation> of(String operation){
        return Arrays.stream(values())
                .filter(o -> o.symbol.equals(operation))
                .findFirst();
    }

    public RuleCondition apply(RuleVariable variable,String factor){
        if (OneOp.class.isAssignableFrom(func.getClass())){
            return ((OneOp)func).apply(variable);
        }else{
            return ((BiOp)func).apply(variable,factor);
        }
    }

    interface Op{

    }

    interface OneOp extends Op, Function<RuleVariable,RuleCondition>{

    }

    interface BiOp extends Op, BiFunction<RuleVariable,String,RuleCondition>{

    }
}
