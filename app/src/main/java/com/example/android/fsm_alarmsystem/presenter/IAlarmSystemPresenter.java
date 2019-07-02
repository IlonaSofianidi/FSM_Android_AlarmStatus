package com.example.android.fsm_alarmsystem.presenter;

public interface IAlarmSystemPresenter {
    void onEvent(String event);

    String loadAlarmStatus();

    boolean setAlarmStatus(String status);

}
