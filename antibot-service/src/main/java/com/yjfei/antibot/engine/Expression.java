package com.yjfei.antibot.engine;

import com.yjfei.antibot.util.Udf;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.*;

import java.util.Collections;

/**
 * 表达式
 */
@Slf4j
@EqualsAndHashCode
public class Expression implements Calculator<Object> {
    private static final JexlEngine ENGINE = new JexlBuilder()
            .debug(log.isDebugEnabled())
            .silent(false)
            .strict(false)
            .namespaces(Collections.singletonMap("udf", new Udf()))
            .create();

    private final JexlExpression jexlExpression;

    public Expression(String expression) {
        if (expression == null) {
            expression = "";
        }
        this.jexlExpression = ENGINE.createExpression(expression);
    }

    /**
     * 表达式内容，使用string表达
     *
     * @return 表达式内容
     */
    public String getExpression() {
        return jexlExpression.getSourceText();
    }

    @Override
    public Object calculate(Context context) {
        try {
            JexlContext jexlContext = new ObjectContext<>(ENGINE, context);

            Object value = jexlExpression.evaluate(jexlContext);
            log.debug("context is {}, expression is {}, value is {}", context, this, value);

            context.putExpression(this, value);
            return value;
        } catch (Exception e) {
            log.error("exception when calculating expression: {}, context: {}", this.getExpression(), context, e);
            context.putExpression(this, null);
            return null;
        }
    }

    @Override
    public String toString() {
        return getExpression();
    }
}
