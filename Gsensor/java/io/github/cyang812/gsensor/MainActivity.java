package io.github.cyang812.gsensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends Activity implements SensorEventListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView textviewX;
    private TextView textviewY;
    private TextView textviewZ;
    private TextView textviewF;
    private TextView textview5;
    private TextView textview6;
    private TextView textview7;
    private TextView textview8;
    private TextView textview9;

    Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textviewX = (TextView) findViewById(R.id.text2);
        textviewY = (TextView) findViewById(R.id.text3);
        textviewZ = (TextView) findViewById(R.id.text4);
        textviewF = (TextView) findViewById(R.id.text1);


        textview5 = (TextView) findViewById(R.id.text5);
        textview6 = (TextView) findViewById(R.id.text6);
        textview7 = (TextView) findViewById(R.id.text7);
        textview8 = (TextView) findViewById(R.id.text8);
        textview9 = (TextView) findViewById(R.id.text9);


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
        if (null == mSensorManager) {
            Log.d(TAG, "deveice not support SensorManager");
        }
        // 参数三，检测的精准度
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAME

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];
            mCalendar = Calendar.getInstance();
            long stamp = mCalendar.getTimeInMillis();

            textviewX.setText(String.valueOf(x));
            textviewY.setText(String.valueOf(y));
            textviewZ.setText(String.valueOf(z));

            if (y < 0 && z < 10) {
                textview5.setText("up");
            } else {
                textview5.setText("wait");
            }

            if (y > 0 && z < 10) {
                textview6.setText("down");
            } else {
                textview6.setText("wait");
            }

            if (x > 0 && z < 10) {
                textview7.setText("left");
            } else {
                textview7.setText("wait");
            }

            if (x < 0 && z < 10) {
                textview8.setText("right");
            } else {
                textview8.setText("wait");
            }

            if ((x < 2 && z > 9) || (y < 2 && z > 9)) {
                textview9.setText("stop");
            } else {
                textview9.setText("wait");
            }
        }
    }
}