package com.example.android.fsm_alarmsystem.utils;

import com.example.android.fsm_alarmsystem.model.entities.Transition;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JsonUtilsFSMTest {
    private static String json;

    @BeforeClass
    public static void setUp() throws IOException {
        json = fromJson();
    }

    @Test
    public void whenParseEventShouldReturnListOfStrings() {
        List<String> expected = new ArrayList<>(Arrays.asList("LOCK", "LOCKx2", "UNLOCK", "UNLOCKx2"));
        List<String> actual = JsonUtilsFSM.parseEvents(json);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void whenParseStatesReturnListOfStrings() {
        List<String> expected = new ArrayList<>();
        expected.add("AlarmDisarmed_AllUnlocked");
        expected.add("AlarmDisarmed_DriverUnlocked");
        expected.add("AlarmDisarmed_AllLocked");
        expected.add("AlarmArmed_AllLocked");

        List<String> actual = JsonUtilsFSM.parseStates(json);
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void whenParseTransitionsReturnTransitionList() {
        List<Transition> expected = new LinkedList<>();
        expected.add(new Transition("AlarmArmed_AllLocked", "AlarmDisarmed_AllUnlocked", "UNLOCKx2"));
        expected.add(new Transition("AlarmDisarmed_DriverUnlocked", "AlarmDisarmed_DriverUnlocked", "UNLOCK"));
        expected.add(new Transition("AlarmDisarmed_AllUnlocked", "AlarmDisarmed_AllLocked", "LOCK"));
        expected.add(new Transition("AlarmDisarmed_DriverUnlocked", "AlarmArmed_AllLocked", "LOCKx2"));
        expected.add(new Transition("AlarmDisarmed_AllUnlocked", "AlarmArmed_AllLocked", "LOCKx2"));
        expected.add(new Transition("AlarmArmed_AllLocked", "AlarmArmed_AllLocked", "LOCKx2"));

        List<Transition> actual = JsonUtilsFSM.parseTransitions(json);
        Assert.assertTrue(actual.containsAll(expected));
    }

    @Test
    public void whenParseInitialStateReturnString() {
        String expected = "AlarmDisarmed_AllUnlocked";
        String actual = JsonUtilsFSM.parseInitialState(json);

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
