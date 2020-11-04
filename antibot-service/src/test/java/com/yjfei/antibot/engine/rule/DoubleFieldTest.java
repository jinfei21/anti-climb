package com.yjfei.antibot.engine.rule;

import com.yjfei.antibot.bean.ModelFieldBean;
import com.yjfei.antibot.common.DataType;
import com.yjfei.antibot.engine.model.DoubleField;
import com.yjfei.antibot.engine.model.ModelField;
import com.yjfei.antibot.service.DataModelService;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.ValidationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class DoubleFieldTest {
    @Test
    public void basic() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setCode("code");
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue("1.0");
        dataModelFieldBean.setDataType(1);


        DataModelService dataModelService = mock(DataModelService.class);
        DoubleField doubleField = (DoubleField) ModelField.create(dataModelService, dataModelFieldBean);

        Assert.assertEquals(DataType.DOUBLE, doubleField.getDataType());
        assertEquals(new Double(1D), doubleField.getDefValue());
        assertEquals("code", doubleField.getCode());
    }

    @Test
    public void castNullWithoutDefaultValue() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(1);

        DataModelService dataModelService = mock(DataModelService.class);
        DoubleField doubleField = (DoubleField) ModelField.create(dataModelService, dataModelFieldBean);

        Double result = doubleField.cast(null);

        assertNull(result);
    }

    @Test
    public void castNullWithDefaultValue() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue("1.0");
        dataModelFieldBean.setDataType(1);

        DataModelService dataModelService = mock(DataModelService.class);
        DoubleField doubleField = (DoubleField) ModelField.create(dataModelService, dataModelFieldBean);

        Double result = doubleField.cast(null);

        assertEquals(new Double(1D), result);
    }

    @Test
    public void castNumber() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(1);

        DataModelService dataModelService = mock(DataModelService.class);
        DoubleField doubleField = (DoubleField) ModelField.create(dataModelService, dataModelFieldBean);

        Double result = doubleField.cast(1D);

        assertEquals(new Double(1D), result);
    }

    @Test
    public void castString() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(1);

        DataModelService dataModelService = mock(DataModelService.class);
        DoubleField doubleField = (DoubleField) ModelField.create(dataModelService, dataModelFieldBean);

        Double result = doubleField.cast("1.0");

        assertEquals(new Double(1D), result);
    }

    @Test(expected = ClassCastException.class)
    public void castNonNumberString() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(1);

        DataModelService dataModelService = mock(DataModelService.class);
        DoubleField doubleField = (DoubleField) ModelField.create(dataModelService, dataModelFieldBean);

        doubleField.cast("abc");
    }

    @Test(expected = ValidationException.class)
    public void validateRequired() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(true);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(1);

        DataModelService dataModelService = mock(DataModelService.class);
        DoubleField doubleField = (DoubleField) ModelField.create(dataModelService, dataModelFieldBean);
        Double result = doubleField.cast(null);

        doubleField.validate(result);
    }

    @Test
    public void validateNonRequired() {
        ModelFieldBean dataModelFieldBean = new ModelFieldBean();
        dataModelFieldBean.setRequired(false);
        dataModelFieldBean.setDefValue(null);
        dataModelFieldBean.setDataType(1);

        DataModelService dataModelService = mock(DataModelService.class);
        DoubleField doubleField = (DoubleField) ModelField.create(dataModelService, dataModelFieldBean);
        Double result = doubleField.cast(null);

        doubleField.validate(result);
    }
}