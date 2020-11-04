package com.yjfei.antibot.service;


import com.yjfei.antibot.bean.RuleBean;
import com.yjfei.antibot.bean.RuleConditionBean;
import com.yjfei.antibot.bean.VariableBean;
import com.yjfei.antibot.common.RiskException;
import com.yjfei.antibot.common.VariableType;
import com.yjfei.antibot.dao.RuleConditionMapper;
import com.yjfei.antibot.dao.RuleMapper;
import com.yjfei.antibot.dao.VariableMapper;
import com.yjfei.antibot.dto.RuleConditionDTO;
import com.yjfei.antibot.dto.StrategyRuleDTO;
import com.yjfei.antibot.util.ConvertUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yjfei.antibot.engine.rule.Operation;
import com.yjfei.antibot.engine.rule.Relation;
import com.yjfei.antibot.engine.rule.Rule;
import com.yjfei.antibot.engine.rule.RuleCondition;
import com.yjfei.antibot.engine.variable.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@SuppressWarnings("all")
public class RuleService {

    @Autowired
    private RuleMapper ruleMapper;

    @Autowired
    private RuleConditionMapper ruleConditionMapper;

    @Autowired
    private VariableMapper variableMapper;


    public List<Rule> getRuleByStrategyId(long strategyId){
        List<RuleBean> ruleBeanList = ruleMapper.selectByStrategyId(strategyId);

        List<RuleConditionBean> ruleConditionBeanList = ruleConditionMapper.selectConditionByStrategyId(strategyId);

        Map<Long,List<RuleConditionBean>> ruleConditionMap = ruleConditionBeanList.stream().collect(Collectors.groupingBy(RuleConditionBean::getRuleId));

        List<Rule> ruleList = ruleBeanList.stream().filter(RuleBean::isValid)
                                        .map(ruleBean -> {
                                            List<RuleConditionBean> conditions = ruleConditionMap.getOrDefault(ruleBean.getId(), Collections.emptyList());

                                            return Rule.builder()
                                                    .id(ruleBean.getId())
                                                    .priority(ruleBean.getPriority())
                                                    .when(conditions(conditions))
                                                    .then(ruleBean.getScore())
                                                    .build();
                                        }).collect(Collectors.toList());

        return ruleList;
    }

    private RuleCondition conditions(List<RuleConditionBean> conditionBeanList){
        conditionBeanList.sort(Comparator.comparing(RuleConditionBean::getPreConditionId,Comparator.nullsFirst(Comparator.naturalOrder())));

        RuleCondition ruleCondition = null;

        for(RuleConditionBean ruleConditionBean:conditionBeanList){

            Variable variable = getVariableById(ruleConditionBean.getVariableId());

            Operation operation = Operation.of(ruleConditionBean.getOperation()).orElseThrow(()->new RiskException("不支持操作类型:"+ruleConditionBean.getOperation()));

            RuleCondition curCondition = operation.apply(variable,ruleConditionBean.getFactor());

            if (ruleCondition == null){
                ruleCondition = curCondition;
            }else{
                Relation relation = Relation.of(ruleConditionBean.getRelation()).orElseThrow(()->new RiskException("不支持的条件关系:"+ruleConditionBean.getRelation()));
                ruleCondition = relation.apply(ruleCondition,curCondition);
            }
        }
        return ruleCondition;
    }

    private Variable getVariableById(long variableId){
        VariableBean variableBean = variableMapper.selectByPrimaryKey(variableId);
        VariableType variableType = VariableType.toType(variableBean.getType());
        Class<? extends Variable> variableClazz = null;
        Variable variable = null;

        switch (variableType){
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

        try{
            Constructor<? extends Variable> constructor = variableClazz.getConstructor(VariableBean.class);
            constructor.setAccessible(true);
            variable = constructor.newInstance(variableBean);
        }catch (Exception e){
            log.error("create variable error,id={}",variableId,e);
            throw new RiskException("failed to create variable,id:" + variableId);

        }
        return variable;
    }


    public List<RuleConditionBean> listRuleConditonByVariableId(Long variableId){
        return ruleConditionMapper.selectConditionByVariableId(variableId);
    }

    //TODO
    @Transactional
    public void addRule(RuleBean ruleBean,List<RuleConditionBean> ruleConditionBeanList){
        ruleBean.setStatus(1);
        if (ruleBean.getId() == null){
            ruleBean.setCreateBy("admin");
            ruleBean.setUpdateBy(ruleBean.getCreateBy());
            ruleMapper.insert(ruleBean);
        }else {
            ruleBean.setUpdateBy("admin");
            ruleMapper.updateByPrimaryKeySelective(ruleBean);
        }

        List<RuleConditionBean> oldRuleConditionList = ruleConditionMapper.selectConditionByRuleId(ruleBean.getId());

        Map<Long,RuleConditionBean> needDeleteRuleConditionMap = oldRuleConditionList.stream().collect(Collectors.toMap(RuleConditionBean::getId,ruleConditionBean -> ruleConditionBean));

        Long preId = null;
        for(RuleConditionBean ruleConditionBean:ruleConditionBeanList) {
            ruleConditionBean.setRuleId(ruleBean.getId());
            ruleConditionBean.setPreConditionId(preId);
            if (ruleConditionBean.getId() == null){

                ruleConditionBean.setCreateBy("admin");
                ruleConditionBean.setUpdateBy(ruleConditionBean.getCreateBy());
                ruleConditionMapper.insert(ruleConditionBean);
            }else {
                needDeleteRuleConditionMap.remove(ruleConditionBean.getId());
                ruleConditionBean.setUpdateBy("admin");

                ruleConditionMapper.updateByPrimaryKeySelective(ruleConditionBean);
            }
            preId = ruleConditionBean.getId();
        }

        if (!CollectionUtils.isEmpty(needDeleteRuleConditionMap)){
            List<Long> deleteIds = needDeleteRuleConditionMap.keySet().stream().collect(Collectors.toList());

            ruleConditionMapper.deleteConditionByIds(deleteIds);
        }
    }

    public void updateRulePriority(List<Long> ids){

        for(int i=0;i<ids.size();i++){
            RuleBean ruleBean = ruleMapper.selectByPrimaryKey(ids.get(i));
            ruleBean.setPriority(i);
            ruleBean.setUpdateBy("admin");
            ruleMapper.updateByPrimaryKeySelective(ruleBean);
        }
    }

    public void updateRuleStatus(List<Long> ids,Integer status){

        ruleMapper.updateStatusByIds(ids,status);
    }

    public Page<RuleBean> list(Long strategyId,String name,Integer page, Integer pageSize){

        Page<RuleBean> pageResult = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> ruleMapper.selectByRuleNameAndStrategyId(strategyId,name));

        return pageResult;

    }

    public List<StrategyRuleDTO> getAllRuleByStrategyId(Long strategyId){

        List<RuleBean> ruleBeanList = ruleMapper.selectByStrategyId(strategyId);


        return ruleBeanList.stream()
                .map(this::buildStrategyRule)
                .sorted(Comparator.comparingInt(StrategyRuleDTO::getPriority))
                .collect(Collectors.toList());
    }

    private StrategyRuleDTO buildStrategyRule(RuleBean ruleBean){
        StrategyRuleDTO strategyRuleDTO = ConvertUtil.convert(ruleBean,StrategyRuleDTO.class);

        List<RuleConditionBean> ruleConditionList = ruleConditionMapper.selectConditionByRuleId(ruleBean.getId());


        String logicString = Rule.builder()
                .id(ruleBean.getId())
                .priority(ruleBean.getPriority())
                .when(conditions(ruleConditionList))
                .then(ruleBean.getScore())
                .build().toLogic();
        strategyRuleDTO.setLogicString(logicString);

        List<RuleConditionDTO> ruleConditionDTOList = ruleConditionList.stream()
                .map(ruleConditionBean -> {
                    RuleConditionDTO ruleConditionDTO = ConvertUtil.convert(ruleConditionBean,RuleConditionDTO.class);

                    return ruleConditionDTO;
                }).collect(Collectors.toList());

        strategyRuleDTO.setConditionList(ruleConditionDTOList);
        return strategyRuleDTO;
    }

    public String getLogicStringByRule(RuleBean ruleBean){
        List<RuleConditionBean> ruleConditionList = ruleConditionMapper.selectConditionByRuleId(ruleBean.getId());

        String logicString = Rule.builder()
                .id(ruleBean.getId())
                .priority(ruleBean.getPriority())
                .when(conditions(ruleConditionList))
                .then(ruleBean.getScore())
                .build().toLogic();

        return logicString;
    }
}
