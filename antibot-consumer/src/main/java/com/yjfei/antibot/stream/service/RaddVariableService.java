package com.yjfei.antibot.stream.service;

import com.yjfei.antibot.common.VariableType;
import com.yjfei.antibot.stream.dao.VariableDao;
import com.yjfei.antibot.stream.engine.VariableValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaddVariableService extends VariableService{

    private static final long MAX_EXPIRE_MINUTE = 60*24*30L;

    @Autowired
    public RaddVariableService(VariableDao variableDao) {
        super(variableDao, VariableType.RADD);
    }

    @Override
    public void save(long timestamp, List<VariableValue> variableValueList) {

        //TODO
        variableValueList.forEach( variableValue -> {
            String key = buildPrimaryKey(variableValue.getBean().getId().toString(),variableValue.getKey());

            variableDao.set(key,variableValue.getValue(), MAX_EXPIRE_MINUTE);
        });
    }

    private String buildPrimaryKey(String prefix,String key){
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(key);
        return sb.toString();
    }

    @Override
    public boolean directSupport() {
        return true;
    }
}
