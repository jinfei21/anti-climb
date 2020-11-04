package com.yjfei.antibot.engine.rule;

import com.yjfei.antibot.data.DataModel;
import com.yjfei.antibot.engine.model.DataSet;
import com.yjfei.antibot.engine.model.ModelField;
import com.yjfei.antibot.engine.model.StringField;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class DataSetTest {
    @Test
    public void basic() {
        assertNull(DataSet.empty().getDataModel());

        DataModel dataModel = mock(DataModel.class);
        when(dataModel.getName()).thenReturn("code");
        DataSet dataSet = new DataSet(dataModel);

        assertEquals("code", dataSet.getDataModelName());
    }

    @Test
    public void add() {
        DataModel dataModel = mock(DataModel.class);
        when(dataModel.hasField(any(ModelField.class))).thenReturn(true);

        StringField stringField = mock(StringField.class);
        when(stringField.getCode()).thenReturn("code");

        DataSet dataSet = new DataSet(dataModel);

        dataSet.add(stringField, "1");

        assertEquals("1", dataSet.get("code"));
    }

    @Test
    public void validate() {
        DataModel dataModel = mock(DataModel.class);
        doNothing().when(dataModel).validate(any(DataSet.class));

        StringField stringField = mock(StringField.class);
        when(stringField.getCode()).thenReturn("code");

        DataSet dataSet = new DataSet(dataModel);

        dataSet.validate();

        DataSet.empty().validate();
    }
}