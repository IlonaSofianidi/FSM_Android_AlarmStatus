package com.example.android.fsm_alarmsystem.view.activities;

import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.fsm_alarmsystem.R;
import com.example.android.fsm_alarmsystem.presenter.impl.AlarmSystemPresenter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    @Mock
    private AlarmSystemPresenter mPresenter;
    private MainActivity mainActivity;
    private ActivityController<MainActivity> controller;

    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(MainActivity.class);
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        controller
                .pause()
                .stop()
                .destroy();
        Mockito.reset(mPresenter);
    }

    @Test
    public void shouldNotBeNull() {
        mainActivity = controller.create().start().resume().get();
        assertNotNull(mainActivity);
    }

    @Test
    public void shouldHaveCorrectAppName() {
        mainActivity = controller.create().start().resume().get();
        String expected = "FSM_AlarmSystem";
        String actual = mainActivity.getResources().getString(R.string.app_name);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void buttonLockClickedShouldCallPresenterOnEvent() {
        mainActivity = controller.create().start().resume().get();
        mainActivity.setPresenter(this.mPresenter);

        Button lock = (Button) mainActivity.findViewById(R.id.button_lock);
        Assert.assertNotNull(lock);


        lock.performClick();
        verify(mPresenter, times(1)).onEvent("LOCK");

    }

    @Test
    public void onResultShouldChangeTextViewText() {
        mainActivity = controller.create().start().resume().get();
        TextView alarmStatus = (TextView) mainActivity.findViewById(R.id.tv_alarmStatus);
        Assert.assertNotNull(alarmStatus);

        mainActivity.onResult("AlarmDisarmed_AllLocked");

        String expected = "AlarmDisarmed_AllLocked";
        String actual = alarmStatus.getText().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void whenOnResultAlarmDisarmedShouldChangeTextViewColor() {
        mainActivity = controller.create().start().resume().get();
        TextView alarmStatus = (TextView) mainActivity.findViewById(R.id.tv_alarmStatus);
        mainActivity.onResult("AlarmDisarmed_AllLocked");

        int expected = mainActivity.getResources().getColor(R.color.colorGreen);
        int actual = getColorTextView(alarmStatus);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void whenOnResultAlarmArmedShouldChangeTextViewColor() {
        mainActivity = controller.create().start().resume().get();
        TextView alarmStatus = (TextView) mainActivity.findViewById(R.id.tv_alarmStatus);
        mainActivity.onResult("AlarmArmed_AllLocked");

        int expected = mainActivity.getResources().getColor(R.color.colorRed);
        int actual = getColorTextView(alarmStatus);
        Assert.assertEquals(expected, actual);
    }


    private int getColorTextView(TextView textView) {
        if (textView.getBackground() instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) textView.getBackground();
            return cd.getColor();
        } else return 0;
    }
}