package com.yjfei.antibot.engine.rule;

import com.yjfei.antibot.bean.ModelBean;
import com.yjfei.antibot.bean.ModelFieldBean;
import com.yjfei.antibot.data.DataModel;
import com.yjfei.antibot.engine.Context;
import com.yjfei.antibot.engine.Expression;
import com.yjfei.antibot.engine.model.DataSet;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {


    @Test
    public void test(){

        ModelBean modelBean = new ModelBean();
        modelBean.setName("test");
        modelBean.setRemark("test");

        List<ModelFieldBean> modelFieldBeanList = Arrays.asList(
                mockLongField(101L, 100L, "ff1"),
                mockDoubleField(102L, 100L, "ff2"),
                mockStringField(103L, 100L, "ff3")
        );

        DataModel dataModel = new DataModel(null,modelBean,modelFieldBeanList);

        Map<String,Object> map = new HashMap<>();

        map.put("ff1","1");
        map.put("ff2","3");
        map.put("ff3","4");

        DataSet dataSet = dataModel.cast(map);


        Context context = new Context("test",dataSet);


        Expression expression = new Expression("request.ff1+request.ff2");

        System.out.println(expression.calculate(context));

    }



    private ModelFieldBean mockLongField(long id, long modelId, String code) {
        ModelFieldBean fieldBean = new ModelFieldBean();
        fieldBean.setId(id);
        fieldBean.setModelId(modelId);
        fieldBean.setCode(code);
        fieldBean.setName(code + "_name");
        fieldBean.setDataType(0);
        fieldBean.setRequired(true);
        fieldBean.setDefValue("0");
        return fieldBean;
    }

    private ModelFieldBean mockDoubleField(long id, long modelId, String code) {
        ModelFieldBean fieldBean = new ModelFieldBean();
        fieldBean.setId(id);
        fieldBean.setModelId(modelId);
        fieldBean.setCode(code);
        fieldBean.setName(code + "_name");

        fieldBean.setDataType(1);
        fieldBean.setRequired(true);
        fieldBean.setDefValue("0.0");

        return fieldBean;
    }

    private ModelFieldBean mockStringField(long id, long modelId, String code) {
        ModelFieldBean fieldBean = new ModelFieldBean();
        fieldBean.setId(id);
        fieldBean.setModelId(modelId);
        fieldBean.setCode(code);
        fieldBean.setName(code + "_name");

        fieldBean.setDataType(2);
        fieldBean.setRequired(true);
        fieldBean.setDefValue("abc");
        return fieldBean;
    }

}
