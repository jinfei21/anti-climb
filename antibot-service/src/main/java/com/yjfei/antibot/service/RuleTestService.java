package com.yjfei.antibot.service;


import com.yjfei.antibot.bean.RuleConditionBean;
import com.yjfei.antibot.bean.VariableBean;
import com.yjfei.antibot.common.RiskException;
import com.yjfei.antibot.common.VariableType;
import com.yjfei.antibot.dao.RuleConditionMapper;
import com.yjfei.antibot.dao.VariableMapper;
import com.yjfei.antibot.engine.Context;
import com.yjfei.antibot.engine.rule.*;
import com.yjfei.antibot.engine.variable.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@SuppressWarnings("all")
public class RuleTestService {

    @Autowired
    private RuleConditionMapper ruleConditionMapper;

    @Autowired
    private VariableMapper variableMapper;


    public RuleResult testRule(Long ruleId, Context context) {

        List<RuleConditionBean> ruleConditionBeanList = ruleConditionMapper.selectConditionByRuleId(ruleId);

        if (CollectionUtils.isEmpty(ruleConditionBeanList)) {
            throw new RuntimeException("规则条件不存在!");
        }


        Rule rule = Rule.builder().id(ruleId)
                .priority(1)
                .when(conditions(ruleConditionBeanList))
                .then(0)
                .build();

        RuleResult ruleResult = rule.calculate(context);

        return ruleResult;
    }

    public Object testVariable(Long variableId,Context context){

        Variable variable = getVariableById(variableId);

        return variable.calculate(context);
    }


    private RuleCondition conditions(List<RuleConditionBean> conditionBeanList) {
        conditionBeanList.sort(Comparator.comparing(RuleConditionBean::getPreConditionId, Comparator.nullsFirst(Comparator.naturalOrder())));

        RuleCondition ruleCondition = null;

        for (RuleConditionBean ruleConditionBean : conditionBeanList) {

            Variable variable = getVariableById(ruleConditionBean.getVariableId());

            Operation operation = Operation.of(ruleConditionBean.getOperation()).orElseThrow(() -> new RiskException("不支持操作类型:" + ruleConditionBean.getOperation()));

            RuleCondition curCondition = operation.apply(variable, ruleConditionBean.getFactor());

            if (ruleCondition == null) {
                ruleCondition = curCondition;
            } else {
                Relation relation = Relation.of(ruleConditionBean.getRelation()).orElseThrow(() -> new RiskException("不支持的条件关系:" + ruleConditionBean.getRelation()));
                ruleCondition = relation.apply(ruleCondition, curCondition);
            }
        }
        return ruleCondition;
    }


    private Variable getVariableById(long variableId) {
        VariableBean variableBean = variableMapper.selectByPrimaryKey(variableId);
        VariableType variableType = VariableType.toType(variableBean.getType());
        Class<? extends Variable> variableClazz = null;
        Variable variable = null;

        switch (variableType) {
            case ROLLUP:
                variableClazz = TumbleVariable.class;
                break;
            case RADD:
                variableClazz = RaddVariable.class;
                break;
            case OTF:
                variableClazz = OTFVariable.class;
                break;
            case NAMELIST:
                variableClazz = NameListVariable.class;
                break;
        }

        try {
            Constructor<? extends Variable> constructor = variableClazz.getConstructor(VariableBean.class);
            constructor.setAccessible(true);
            variable = constructor.newInstance(variableBean);
        } catch (Exception e) {
            log.error("create variable error,id={}", variableId, e);
            throw new RiskException("failed to create variable,id:" + variableId);

        }
        return variable;
    }

}
