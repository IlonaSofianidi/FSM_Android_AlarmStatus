package com.example.android.fsm_alarmsystem.view.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.fsm_alarmsystem.R;
import com.example.android.fsm_alarmsystem.presenter.IAlarmSystemPresenter;
import com.example.android.fsm_alarmsystem.presenter.impl.AlarmSystemPresenter;
import com.example.android.fsm_alarmsystem.view.IMainView;

public class MainActivity extends AppCompatActivity implements IMainView {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String ALARM_STATUS_KEY = "alarm_status";

    private AlarmSystemPresenter presenter;
    private TextView tvAlarmStatus;
    private Button buttonLock;
    private Button buttonLockx2;
    private Button buttonUnlock;
    private Button buttonUnlockx2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new AlarmSystemPresenter(this, this);
        initViews();
        if (savedInstanceState != null) {
            String savedAlarmStatus = savedInstanceState.getString(ALARM_STATUS_KEY);
            presenter.setAlarmStatus(savedAlarmStatus);
        }
        String alarmStatus = presenter.loadAlarmStatus();
        tvAlarmStatus.setText(alarmStatus);
        setAlarmStatusTextViewColor(alarmStatus);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ALARM_STATUS_KEY, tvAlarmStatus.getText().toString());
    }

    private void initViews() {
        tvAlarmStatus = (TextView) findViewById(R.id.tv_alarmStatus);
        buttonLock = (Button) findViewById(R.id.button_lock);
        buttonLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.onEvent("LOCK");
                    }
                }).start();
            }
        });
        buttonLockx2 = (Button) findViewById(R.id.button_lockx2);
        buttonLockx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.onEvent("LOCKx2");
                    }
                }).start();

            }
        });
        buttonUnlock = (Button) findViewById(R.id.button_unlock);
        buttonUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.onEvent("UNLOCK");
                    }
                }).start();

            }
        });
        buttonUnlockx2 = (Button) findViewById(R.id.button_unlockx2);
        buttonUnlockx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.onEvent("UNLOCKx2");
                    }
                }).start();

            }
        });
    }

    @Override
    public void onResult(String eventStatus) {
        tvAlarmStatus.setText(eventStatus);
        setAlarmStatusTextViewColor(eventStatus);

    }

    private void setAlarmStatusTextViewColor(String eventStatus) {
        if (eventStatus.contains("AlarmArmed")) {

            tvAlarmStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));
        } else {
            tvAlarmStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
        }
    }

    public void setPresenter(AlarmSystemPresenter presenter) {
        this.presenter = presenter;
    }

    public IAlarmSystemPresenter getActivityPresenter() {
        return presenter;
    }
}
