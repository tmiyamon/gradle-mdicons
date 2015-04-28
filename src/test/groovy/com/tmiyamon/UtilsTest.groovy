package com.tmiyamon

import com.tmiyamon.mdicons.Utils
import org.junit.Test

/**
 * Created by tmiyamon on 12/26/14.
 */
class UtilsTest {
    class Dummy {
        String test
    }

    @Test
    public void capitalize_returnsCapitalizedString() {
        assert "Test" == Utils.capitalize("test")
    }
    @Test
    public void getGetterName_returnsGetterName() {
        assert "getTestA" == Utils.getGetterName("testA")
    }
    @Test
    public void getSetterName_returnSetterName() {
        assert "setTestA" == Utils.getSetterName("testA")
    }
    @Test
    public void getValue_resturnsValue() {
        def dummy = new Dummy()
        dummy.test = "value"
        assert "value", Utils.getValue(dummy, "test")
    }
    @Test
    public void setValue_setsValue() {
        def dummy = new Dummy()
        Utils.setValue(dummy, "test", "value")
        assert "value", dummy.test
    }

    @Test
    public void flatList_returnListHavingParam() {
        assert ["test"] == Utils.flatList("test")
    }

    @Test
    public void flatList_returnFlatListIfParamsIsList() {
        assert ["test"] == Utils.flatList(["test"])
    }
}
