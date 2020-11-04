package com.yjfei.antibot.stream;

import com.yjfei.antibot.stream.engine.Context;
import com.yjfei.antibot.stream.engine.Expression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UdfTest {
    @Test
    public void cnt() {
        Expression expression = new Expression("cnt()");
        Context context = new Context();
        Object result = expression.calculate(context);
        assertEquals(1L, result);
    }

    @Test
    public void cntWithParam() {
        Expression expression = new Expression("cnt(5)");
        Context context = new Context();
        Object result = expression.calculate(context);
        assertEquals(5L, result);
    }

    @Test
    public void test() {
        String s = "format(\"yyyy-MM-dd HH:mm:ss\", dt)";
        Expression expression = new Expression(s);
        Context context = new Context();
        context.put("dt", "2020-01-22 11:15:27");
        Object result = expression.calculate(context);
        System.out.println(result);
    }
}
