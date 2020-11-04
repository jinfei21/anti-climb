package com.yjfei.antibot.stream.service;

import com.yjfei.antibot.common.VariableType;
import com.yjfei.antibot.stream.dao.VariableDao;
import com.yjfei.antibot.stream.engine.VariableValue;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class VariableService {

    protected static final String VERSION = "version";

    protected final VariableDao variableDao;
    protected final VariableType variableType;


    protected VariableService(VariableDao variableDao,VariableType variableType){
        this.variableDao = variableDao;
        this.variableType = variableType;
    }


    /**
     * 保存变量值
     * 待保存的变量必须是同一个时间戳
     *
     * @param timestamp         时间戳
     * @param variableValueList 变量值列表
     */
    public abstract void save(long timestamp, List<VariableValue> variableValueList);

    /**
     * 支持的变量类型
     *
     * @return 支持的变量类型
     */
    public VariableType supportType() {
        return variableType;
    }

    /**
     * 是否直接支持相关类型
     *
     * @return 是否直接支持相关类型
     */
    public abstract boolean directSupport();
}
