package com.yjfei.antibot.engine.rule;

import com.yjfei.antibot.engine.Calculator;
import com.yjfei.antibot.engine.Context;

public class Rule implements Calculator<RuleResult> {
    private final long id;
    private final RuleCondition condition;
    private final int score;
    private final int priority;

    private Rule(long id,RuleCondition condition,int priority,int score){
        this.id = id;
        this.condition = condition;
        this.priority = priority;
        this.score = score;
    }


    @Override
    public RuleResult calculate(Context context) {
        int score = condition.apply(context)?this.score:0;
        return new RuleResult(id,score);
    }

    @Override
    public String toString() {
        return "Rule(id=" + id +
                ", priority=" + priority +
                ") {\n" +
                "if (" + condition +
                ")\nthen " + score +
                "\n}";
    }

    public long getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }


    /**
     * 规则的条件部分
     *
     * @return 规则构造器
     */
    public static RuleBuilder builder() {
        return new RuleBuilder();
    }

    public String toLogic() {
        return "if (" + condition +
                ")\nthen " + score;
    }

    /**
     * 规则构造器
     */
    public static class RuleBuilder {
        private long id;
        private RuleCondition condition;
        private int score;
        private int priority;

        public RuleBuilder id(long id) {
            this.id = id;
            return this;
        }

        public RuleBuilder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public RuleBuilder when(RuleCondition condition){
            this.condition = condition;
            return this;
        }

        public RuleBuilder then(int score){
            this.score = score;
            return this;
        }

        public Rule build(){
            return new Rule(id,condition,priority,score);
        }

    }

}
