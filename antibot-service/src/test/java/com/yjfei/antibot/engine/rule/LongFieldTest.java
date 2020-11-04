package com.yjfei.antibot.engine.rule;

import com.yjfei.antibot.bean.ModelFieldBean;
import com.yjfei.antibot.common.DataType;
import com.yjfei.antibot.engine.model.LongField;
import com.yjfei.antibot.engine.model.ModelField;
import com.yjfei.antibot.service.DataModelService;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.ValidationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class LongFieldTest {
    @Test
    public void basic() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setCode("code");
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue("1");
        dataModelFieldBean.setDataType(0);

        DataModelService dataModelService = mock(DataModelService.class);
        LongField longField = (LongField) ModelField.create(dataModelService, dataModelFieldBean);

        Assert.assertEquals(DataType.LONG, longField.getDataType());
        assertEquals(new Long(1L), longField.getDefValue());
        assertEquals("code", longField.getCode());
    }

    @Test
    public void castNullWithoutDefaultValue() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(0);

        DataModelService dataModelService = mock(DataModelService.class);
        LongField longField = (LongField) ModelField.create(dataModelService, dataModelFieldBean);

        Long result = longField.cast(null);

        assertNull(result);
    }

    @Test
    public void castNullWithDefaultValue() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue("1");
        dataModelFieldBean.setDataType(0);
        DataModelService dataModelService = mock(DataModelService.class);
        LongField longField = (LongField) ModelField.create(dataModelService, dataModelFieldBean);

        Long result = longField.cast(null);

        assertEquals(new Long(1L), result);
    }

    @Test
    public void castNumber() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(0);

        DataModelService dataModelService = mock(DataModelService.class);
        LongField longField = (LongField) ModelField.create(dataModelService, dataModelFieldBean);

        Long result = longField.cast(1L);

        assertEquals(new Long(1L), result);
    }

    @Test
    public void castString() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(0);

        DataModelService dataModelService = mock(DataModelService.class);
        LongField longField = (LongField) ModelField.create(dataModelService, dataModelFieldBean);

        Long result = longField.cast("1");

        assertEquals(new Long(1L), result);
    }

    @Test(expected = ClassCastException.class)
    public void castNonNumberString() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(0);

        DataModelService dataModelService = mock(DataModelService.class);
        LongField longField = (LongField) ModelField.create(dataModelService, dataModelFieldBean);

        longField.cast("abc");
    }

    @Test(expected = ValidationException.class)
    public void validateRequired() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(0);

        DataModelService dataModelService = mock(DataModelService.class);
        LongField longField = (LongField) ModelField.create(dataModelService, dataModelFieldBean);
        Long result = longField.cast(null);

        longField.validate(result);
    }

    @Test
    public void validateNonRequired() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(false);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(0);

        DataModelService dataModelService = mock(DataModelService.class);
        LongField longField = (LongField) ModelField.create(dataModelService, dataModelFieldBean);
        Long result = longField.cast(null);

        longField.validate(result);
    }
}