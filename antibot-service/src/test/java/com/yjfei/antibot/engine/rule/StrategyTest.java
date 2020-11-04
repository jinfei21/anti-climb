package com.yjfei.antibot.engine.rule;

import com.yjfei.antibot.engine.Context;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class StrategyTest {
    @Test
    public void calculate() {
        List<Rule> ruleList = Arrays.asList(
                Rule.builder().id(1).when(c -> false).then(1).build(),
                Rule.builder().id(2).when(c -> true).then(1).build()
        );
        Strategy strategy = new Strategy(0,ruleList,20,30);
        Context context = mock(Context.class);

        StrategyResult result = strategy.calculate(context);

        assertNotNull(result);
    }
}