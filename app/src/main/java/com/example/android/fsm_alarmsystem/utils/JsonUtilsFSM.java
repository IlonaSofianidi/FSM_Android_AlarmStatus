package com.example.android.fsm_alarmsystem.utils;

import android.util.Log;

import com.example.android.fsm_alarmsystem.model.entities.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JsonUtilsFSM {

    private JsonUtilsFSM() {
    }

    static final String TAG = JsonUtilsFSM.class.getSimpleName();

    public static List<String> parseEvents(String json) {
        List<String> eventsList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray events = jsonObject.getJSONArray("events");
            for (int i = 0; i < events.length(); i++) {
                JSONObject event = events.getJSONObject(i);
                eventsList.add(event.getString("name"));
            }
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }
        Log.d(TAG, "Events list" + eventsList.toString());
        return eventsList;
    }

    public static List<String> parseStates(String json) {
        List<String> statesList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray states = jsonObject.getJSONArray("states");
            for (int i = 0; i < states.length(); i++) {
                JSONObject state = states.getJSONObject(i);
                statesList.add(state.getString("name"));
            }
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }
        Log.d(TAG, "States list" + statesList.toString());
        return statesList;
    }

    public static List<Transition> parseTransitions(String json) {
        List<Transition> transitionsList = new LinkedList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray transitions = jsonObject.getJSONArray("transitions");
            for (int i = 0; i < transitions.length(); i++) {
                JSONObject transition = transitions.getJSONObject(i);

                String from = transition.getString("from");
                String to = transition.getString("to");
                String trigger = transition.getString("trigger");

                transitionsList.add(new Transition(from, to, trigger));
            }
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }
        Log.d(TAG, "Transitions list" + transitionsList.toString());
        return transitionsList;
    }

    public static String parseInitialState(String json) {
        String initialState = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            initialState = jsonObject.getString("initialState");
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }
        Log.d(TAG, "Initial state " + initialState);
        return initialState;
    }
}