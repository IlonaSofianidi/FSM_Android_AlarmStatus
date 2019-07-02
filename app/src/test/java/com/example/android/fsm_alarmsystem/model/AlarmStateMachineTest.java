package com.example.android.fsm_alarmsystem.model;

import com.example.android.fsm_alarmsystem.utils.JsonUtilsFSM;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AlarmStateMachineTest {


    private JsonUtilsFSM mUtils;
    private AlarmStateMachine unit;

    @Before
    public void setUp() throws IOException {
        unit = new AlarmStateMachine();
        mUtils = Mockito.mock(JsonUtilsFSM.class);
        unit.configureFSM(fromJson());

    }

    @After
    public void tearDown() {
        unit = null;
    }

    @Test
    public void whenPropagateStateCurrentStateNullReturnInitial() {
        unit.resetCurrentState();
        unit.propagateState("LOCK");
        String expected = "AlarmDisarmed_AllUnlocked";
        String actual = unit.getCurrentState().getName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void whenPropagateStateEventNotExistsReturnFalse() {
        unit.setCurrentState("AlarmDisarmed_AllUnlocked");
        boolean actual = unit.propagateState("NOT_VALID_EVENT");
        Assert.assertFalse(actual);
    }

    @Test
    public void testTransitions() {
        unit.setCurrentState("AlarmArmed_AllLocked");
        String expected = "AlarmDisarmed_AllUnlocked";
        String trigger = "UNLOCKx2";
        unit.propagateState(trigger);
        String actual = unit.getCurrentState().getName();

        unit.setCurrentState("AlarmDisarmed_DriverUnlocked");
        String expected1 = "AlarmDisarmed_DriverUnlocked";
        String trigger1 = "UNLOCK";
        unit.propagateState(trigger1);
        String actual1 = unit.getCurrentState().getName();

        unit.setCurrentState("AlarmDisarmed_AllUnlocked");
        String expected2 = "AlarmDisarmed_AllLocked";
        String trigger2 = "LOCK";
        unit.propagateState(trigger2);
        String actual2 = unit.getCurrentState().getName();

        unit.setCurrentState("AlarmDisarmed_DriverUnlocked");
        String expected3 = "AlarmArmed_AllLocked";
        String trigger3 = "LOCKx2";
        unit.propagateState(trigger3);
        String actual3 = unit.getCurrentState().getName();


        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected1, actual1);
        Assert.assertEquals(expected2, actual2);
        Assert.assertEquals(expected3, actual3);
    }

    @Test
    public void whenGetCurrentStateFirstTimeReturnInitialState() {
        String expected = "AlarmDisarmed_AllUnlocked";
        unit.resetCurrentState();
        String actual = unit.getCurrentState().getName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCurrentState() {
        unit.setCurrentState("AlarmArmed_AllLocked");
        String expected = "AlarmArmed_AllLocked";
        String actual = unit.getCurrentState().getName();
        Assert.assertEquals(expected, actual);

    }

    private static String fromJson() throws IOException {
        String json;
        FileInputStream is = new FileInputStream(new File("src/test/res/test_config"));
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");
        return json;
    }
}