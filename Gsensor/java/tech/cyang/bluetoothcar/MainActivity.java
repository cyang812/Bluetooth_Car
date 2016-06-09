package tech.cyang.bluetoothcar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mSensor;
    Calendar mCalendar;

    public byte[] message = new byte[1];
    private Vibrator vibrator;

    private int ENABLE_BLUETOOTH = 2;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket = null;
    OutputStream outputStream = null;
    private static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String blueAddress = "98:D3:31:70:9B:CA";//蓝牙模块的MAC地址

    private TextView textX;
    private TextView textY;
    private TextView textZ;
    private TextView textUp;
    private TextView textLeft;
    private TextView textRgiht;
    private TextView textDown;
    private TextView textStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textX = (TextView) findViewById(R.id.textX);
        textY = (TextView) findViewById(R.id.textY);
        textZ = (TextView) findViewById(R.id.textZ);
        textUp = (TextView) findViewById(R.id.textUp);
        textLeft = (TextView) findViewById(R.id.textLeft);
        textRgiht = (TextView) findViewById(R.id.textRight);
        textDown = (TextView) findViewById(R.id.textDown);
        textStop = (TextView) findViewById(R.id.textStop);


        if (bluetoothAdapter == null) {
            Toast.makeText(this, "不支持蓝牙", Toast.LENGTH_LONG).show();
            finish();
        } else if (!bluetoothAdapter.isEnabled()) {
            Log.d("true", "开始连接");
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, ENABLE_BLUETOOTH);
        }
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
        if (null == mSensorManager) {
            Log.d(TAG, "deveice not support SensorManager");
        }
        // 参数三，检测的精准度
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAME
    }

    public void bluesend(byte[] message){
        try{
            outputStream = bluetoothSocket.getOutputStream();
            Log.d("send", Arrays.toString(message));
            outputStream.write(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void vibrator(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }


//    //发送数据演示
//    switch (v.getId()){
//        case R.id.imagebutton1:
//            message[0]= (byte) 0x41;
//            vibrator();
//            Toast.makeText(this,"前进",Toast.LENGTH_SHORT).show();
//            bluesend(message);
//            Log.d("cy08",""+message[0]);
//            break;

    //重力传感逻辑
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

            textX.setText(String.valueOf(x));
            textY.setText(String.valueOf(y));
            textZ.setText(String.valueOf(z));

            if (y < 0 && z < 10) {
                textUp.setText("up");
                message[0]= (byte) 0x41;
                //vibrator();
                Toast.makeText(this,"前进",Toast.LENGTH_SHORT).show();
                bluesend(message);
                Log.d("cy08",""+message[0]);
            } else {
                textUp.setText("wait");
            }

            if (y > 0 && z < 10) {
                textDown.setText("down");
                message[0]= (byte) 0x42;
                //vibrator();
                Toast.makeText(this,"后退",Toast.LENGTH_SHORT).show();
                bluesend(message);
                Log.d("cy08",""+message[0]);
            } else {
                textDown.setText("wait");
            }

            if (x > 0 && z < 10) {
                textLeft.setText("left");
                message[0]= (byte) 0x44;
                //vibrator();
                Toast.makeText(this,"左转",Toast.LENGTH_SHORT).show();
                bluesend(message);
                Log.d("cy08",""+message[0]);
            } else {
                textLeft.setText("wait");
            }

            if (x < 0 && z < 10) {
                textRgiht.setText("right");
                message[0]= (byte) 0x43;
                //vibrator();
                Toast.makeText(this,"右转",Toast.LENGTH_SHORT).show();
                bluesend(message);
                Log.d("cy08",""+message[0]);
            } else {
                textRgiht.setText("wait");
            }

            if ((x < 5 && z > 5) || (y < 5 && z > 5)) {
                textStop.setText("stop");
                message[0]= (byte) 0x40;
                //vibrator();
                Toast.makeText(this,"停止",Toast.LENGTH_SHORT).show();
                bluesend(message);
                Log.d("cy08",""+message[0]);
            } else {
                textStop.setText("wait");
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try{
            bluetoothSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(blueAddress);
        try{
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
            Log.d("true","开始连接");
            bluetoothSocket.connect();
            Log.d("true","完成连接");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}