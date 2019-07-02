package com.example.android.fsm_alarmsystem.model;

import android.util.Log;

import com.example.android.fsm_alarmsystem.model.entities.State;
import com.example.android.fsm_alarmsystem.model.entities.Transition;
import com.example.android.fsm_alarmsystem.utils.JsonUtilsFSM;

import java.util.ArrayList;
import java.util.List;

public class AlarmStateMachine {
    private static final String TAG = AlarmStateMachine.class.getSimpleName();
    private State mCurrentState;
    private State initialState;
    private List<String> events;
    private List<State> states;

    public AlarmStateMachine() {
    }

    public boolean propagateState(String event) {
        if (mCurrentState == null) {
            mCurrentState = initialState;
            Log.d(TAG, "Propagate state " + mCurrentState.getName());
            return true;
        }
        if (mCurrentState.getTransitionMap().containsKey(event)) {
            mCurrentState = mCurrentState.getTransitionMap().get(event);
            Log.d(TAG, "Propagate state " + mCurrentState.getName());
            return true;
        } else {
            return false;
        }
    }

    public State getCurrentState() {
        if (mCurrentState == null) {
            mCurrentState = initialState;
        }
        Log.d(TAG, "Get current state" + mCurrentState.getName());
        return mCurrentState;
    }

    public boolean configureFSM(String jsonConfig) {
        events = JsonUtilsFSM.parseEvents(jsonConfig);
        states = new ArrayList<>();

        List<String> statesStrings = JsonUtilsFSM.parseStates(jsonConfig);
        for (String state : statesStrings) {
            Log.d(TAG, "State: " + state);
            states.add(new State(state));
        }
        List<Transition> transitions = JsonUtilsFSM.parseTransitions(jsonConfig);
        for (Transition transition : transitions) {
            if (!addTransition(transition.getFrom(), transition.getTo(), transition.getTrigger())) {
                String msg = String.format("Failed adding transition from:%s to:%s trigger:%s", transition.getFrom(),
                        transition.getTo(), transition.getTrigger());
                Log.i(TAG, msg);
            }
        }
        initialState = getStateByName(JsonUtilsFSM.parseInitialState(jsonConfig));
        return true;
    }

    private State getStateByName(String name) {
        if (name != null) {
            for (State state : states) {
                if (state.getName().matches(name)) {
                    Log.d(TAG, "State by name" + state.getName());
                    return state;
                }
            }
        }
        return null;
    }

    private boolean addTransition(String from, String to, String trigger) {
        State sourceState = getStateByName(from);
        State targetState = getStateByName(to);

        boolean existsSource = sourceState != null;
        boolean existsTarget = targetState != null;

        if (existsSource && existsTarget && existsEvent(trigger)) {
            if (sourceState.getTransitionMap().containsKey(trigger)) {
                Log.d(TAG, "Source state contains key");
                return false;
            } else {
                Log.d(TAG, "Put target and trigger into transactionMap");
                sourceState.getTransitionMap().put(trigger, targetState);
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean existsEvent(String name) {
        for (String event : events) {
            if (event.matches(name)) {
                Log.d(TAG, "Event exists");
                return true;
            }
        }
        Log.d(TAG, "Event not exists");
        return false;
    }

    public boolean setCurrentState(String state) {
        State userState = getStateByName(state);
        if (userState != null) {
            mCurrentState = userState;
            Log.d(TAG, "Set User state " + userState);
            return true;
        }
        Log.d(TAG, "Wrong user state");
        return false;
    }

    public void resetCurrentState() {
        mCurrentState = null;
    }
}
