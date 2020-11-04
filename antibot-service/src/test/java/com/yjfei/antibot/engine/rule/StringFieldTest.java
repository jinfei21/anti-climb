package com.yjfei.antibot.engine.rule;

import com.yjfei.antibot.bean.ModelFieldBean;
import com.yjfei.antibot.common.DataType;
import com.yjfei.antibot.engine.model.ModelField;
import com.yjfei.antibot.engine.model.StringField;
import com.yjfei.antibot.service.DataModelService;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.ValidationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class StringFieldTest {
    @Test
    public void basic() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setCode("code");
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue("abc");
        dataModelFieldBean.setDataType(2);

        DataModelService dataModelService = mock(DataModelService.class);
        StringField stringField = (StringField) ModelField.create(dataModelService, dataModelFieldBean);

        Assert.assertEquals(DataType.STRING, stringField.getDataType());
        assertEquals("abc", stringField.getDefValue());
        assertEquals("code", stringField.getCode());
    }

    @Test
    public void castNullWithoutDefaultValue() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(2);

        DataModelService dataModelService = mock(DataModelService.class);
        StringField stringField = (StringField) ModelField.create(dataModelService, dataModelFieldBean);

        String result = stringField.cast(null);

        assertNull(result);
    }

    @Test
    public void castNullWithDefaultValue() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue("abc");
        dataModelFieldBean.setDataType(2);

        DataModelService dataModelService = mock(DataModelService.class);
        StringField stringField = (StringField) ModelField.create(dataModelService, dataModelFieldBean);

        String result = stringField.cast(null);

        assertEquals("abc", result);
    }

    @Test
    public void castChar() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(2);

        DataModelService dataModelService = mock(DataModelService.class);
        StringField stringField = (StringField) ModelField.create(dataModelService, dataModelFieldBean);

        String result = stringField.cast('a');

        assertEquals("a", result);
    }

    @Test
    public void castString() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(2);

        DataModelService dataModelService = mock(DataModelService.class);
        StringField stringField = (StringField) ModelField.create(dataModelService, dataModelFieldBean);

        String result = stringField.cast("abc");

        assertEquals("abc", result);
    }

    @Test(expected = ClassCastException.class)
    public void castNonString() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(2);

        DataModelService dataModelService = mock(DataModelService.class);
        StringField stringField = (StringField) ModelField.create(dataModelService, dataModelFieldBean);

        String result = stringField.cast(1);

        assertEquals("abc", result);
    }

    @Test(expected = ValidationException.class)
    public void validateRequired() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(2);

        DataModelService dataModelService = mock(DataModelService.class);
        StringField stringField = (StringField) ModelField.create(dataModelService, dataModelFieldBean);
        String result = stringField.cast(null);

        stringField.validate(result);
    }

    @Test
    public void validateNonRequired() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(false);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(2);

        DataModelService dataModelService = mock(DataModelService.class);
        StringField stringField = (StringField) ModelField.create(dataModelService, dataModelFieldBean);
        String result = stringField.cast(null);

        stringField.validate(result);
    }
}