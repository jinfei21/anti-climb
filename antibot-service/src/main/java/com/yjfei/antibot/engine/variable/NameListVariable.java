package com.yjfei.antibot.engine.variable;

import com.yjfei.antibot.bean.NameListItemBean;
import com.yjfei.antibot.bean.VariableBean;
import com.yjfei.antibot.config.ApplicationContextProvider;
import com.yjfei.antibot.engine.Context;
import com.yjfei.antibot.engine.Expression;
import com.yjfei.antibot.service.NameListService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 名单类变量，1代表在名单中，0代表不在名单中
 */
@Slf4j
public class NameListVariable extends Variable<Integer> {
    private static final int INVALID = -1;

    private final long namelistId;

    private final Expression keyExpression;

    public NameListVariable(VariableBean variableBean){
        super(variableBean);
        this.namelistId = variableBean.getSourceId();
        this.keyExpression = new Expression(variableBean.getKeyExpression());
    }


    @Override
    protected Integer evaluate(Context context) {
        Object key = keyExpression.calculate(context);
        log.error("namelistkey:"+key);
        if (key == null) {
            return INVALID;
        }

        NameListService nameListService = ApplicationContextProvider.getApplicationContext().getBean(NameListService.class);


        NameListItemBean nameListItemBean = nameListService.findNameListItem(this.namelistId,key.toString());

        Date now = new Date(context.getTimestamp());

        log.error("namelistId:{},key:{},value:{}",this.namelistId,key.toString(), nameListItemBean);

        return (nameListItemBean != null && isValid(nameListItemBean, now)) ? nameListItemBean.getType() : INVALID;
    }

    private boolean isValid(NameListItemBean nameListItemBean, Date now) {
        return nameListItemBean.getEffectDate().before(now) && nameListItemBean.getExpireDate().after(now);
    }
}
