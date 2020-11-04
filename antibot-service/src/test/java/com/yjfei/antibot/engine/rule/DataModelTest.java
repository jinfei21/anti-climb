package com.yjfei.antibot.engine.rule;

import com.yjfei.antibot.bean.ModelBean;
import com.yjfei.antibot.bean.ModelFieldBean;
import com.yjfei.antibot.data.DataModel;
import com.yjfei.antibot.engine.model.DataSet;
import com.yjfei.antibot.engine.model.ModelField;
import com.yjfei.antibot.engine.model.StringField;
import com.yjfei.antibot.service.DataModelService;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class DataModelTest {
    @Test
    public void basic() {
        DataModelService dataModelService = mock(DataModelService.class);

        ModelBean dataModelBean = mockDataModelBean(1L, "outer");

        List<ModelFieldBean> dataModelFieldBeanList = Arrays.asList(
                mockLongField(101L, 100L, "ff1"),
                mockDoubleField(102L, 100L, "ff2"),
                mockStringField(103L, 100L, "ff3")
        );
        DataModel dataModel = new DataModel(dataModelService, dataModelBean, dataModelFieldBeanList);

        assertEquals(3, dataModel.getFieldCount());
        assertEquals("outer", dataModel.getName());

        ModelField modelField = new StringField(mockStringField(3L, 1L, "ff3"));
        assertTrue(dataModel.hasField(modelField));

        modelField = new StringField(mockStringField(999L, 1L, "f999"));
        assertFalse(dataModel.hasField(modelField));
    }

    @Test
    public void castNull() {
        DataModelService dataModelService = mock(DataModelService.class);
        DataModel dataModel = new DataModel(dataModelService, mockDataModelBean(1L, "outer"), Collections.emptyList());
        Assert.assertEquals(DataSet.empty(), dataModel.cast((Object) null));
        Assert.assertEquals(DataSet.empty(), dataModel.cast(null));
    }


    private ModelBean mockDataModelBean(long id, String code) {
        ModelBean dataModelBean = new ModelBean();
        dataModelBean.setId(id);
        dataModelBean.setName(code);
        return dataModelBean;
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