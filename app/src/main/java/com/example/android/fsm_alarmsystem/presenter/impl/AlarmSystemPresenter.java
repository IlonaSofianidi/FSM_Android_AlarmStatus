package com.example.android.fsm_alarmsystem.presenter.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.android.fsm_alarmsystem.model.AlarmStateMachine;
import com.example.android.fsm_alarmsystem.presenter.IAlarmSystemPresenter;
import com.example.android.fsm_alarmsystem.view.IMainView;

import java.io.IOException;
import java.io.InputStream;

public class AlarmSystemPresenter implements IAlarmSystemPresenter {
    public static final String TAG = AlarmSystemPresenter.class.getSimpleName();
    private AlarmStateMachine alarmStateMachine;
    private IMainView mainView;
    private Handler handler;


    public AlarmSystemPresenter(Context context, IMainView view) {
        this.mainView = view;
        alarmStateMachine = new AlarmStateMachine();
        alarmStateMachine.configureFSM(loadJSONFromAsset(context));
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onEvent(final String event) {
        alarmStateMachine.propagateState(event);
        final String currentState;
        currentState = alarmStateMachine.getCurrentState().getName();
        handler.post(new Runnable() {
            @Override
            public void run() {
                mainView.onResult(currentState);
            }
        });
    }

    @Override
    public String loadAlarmStatus() {
        return alarmStateMachine.getCurrentState().getName();
    }

    @Override
    public boolean setAlarmStatus(String status) {

        return alarmStateMachine.setCurrentState(status);
    }

    private String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("AlarmFSM");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.d(TAG, ex.toString());
            return null;
        }
        Log.d(TAG, "JSON: " + json);
        return json;
    }
}
