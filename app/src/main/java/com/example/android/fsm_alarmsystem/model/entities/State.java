package com.example.android.fsm_alarmsystem.model.entities;

import java.util.HashMap;
import java.util.Map;

public class State {
    private String name;
    private Map<String, State> transitionMap;

    public State(String name) {
        this.name = name;
        transitionMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, State> getTransitionMap() {
        return transitionMap;
    }

}
