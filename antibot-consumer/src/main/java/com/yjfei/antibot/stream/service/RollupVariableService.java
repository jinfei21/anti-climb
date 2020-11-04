package com.yjfei.antibot.stream.service;

import com.yjfei.antibot.common.RollupType;
import com.yjfei.antibot.common.VariableType;
import com.yjfei.antibot.stream.dao.VariableDao;


public abstract class RollupVariableService extends VariableService{

    private final RollupType rollupType;


    protected RollupVariableService(VariableDao variableDao,RollupType rollupType){
        super(variableDao, VariableType.ROLLUP);
        this.rollupType = rollupType;
    }

    @Override
    public boolean directSupport() {
        return false;
    }

    public RollupType supportRollupType(){
        return rollupType;
    }
}
