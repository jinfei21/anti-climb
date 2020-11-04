package com.yjfei.antibot.stream.service;

import com.yjfei.antibot.common.RollupType;
import com.yjfei.antibot.common.VariableType;
import com.yjfei.antibot.stream.dao.VariableDao;
import com.yjfei.antibot.stream.engine.VariableValue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import static java.util.function.Function.identity;
import java.util.stream.Collectors;

@Service
public class ComplexRollupVariableService extends VariableService{


    private final Map<RollupType,RollupVariableService> variableServiceMap;

    public ComplexRollupVariableService(VariableDao variableDao, List<RollupVariableService> variableServiceList) {
        super(variableDao, VariableType.ROLLUP );
        this.variableServiceMap = variableServiceList.stream()
        .collect(Collectors.toMap(RollupVariableService::supportRollupType, identity()));
    }

    @Override
    public void save(long timestamp, List<VariableValue> variableValueList) {
        variableValueList.stream()
                .collect(Collectors.groupingBy(v -> v.getBean().getSubType()))
                .forEach((type, valueList) -> {
                    RollupType rollupType = RollupType.of(type);
                    variableServiceMap.get(rollupType).save(timestamp, valueList);
                });
    }

    @Override
    public boolean directSupport() {
        return true;
    }
}


