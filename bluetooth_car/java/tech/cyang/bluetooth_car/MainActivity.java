package tech.cyang.bluetooth_car;

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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public byte[] message = new byte[1];
    private Vibrator vibrator;

    private static final String TAG = MainActivity.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mSensor;
    public static boolean sensor_isOn = false;

    private TextView sensor_info;

    int ENABLE_BLUETOOTH=2;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket = null;
    OutputStream outputStream = null;
    static final UUID MY_UUID_SECURE=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String blueAddress="98:D3:31:70:9B:CA";//蓝牙模块的MAC地址


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //连接蓝牙
        if(bluetoothAdapter==null){
            Toast.makeText(this,"不支持蓝牙",Toast.LENGTH_LONG).show();
            finish();
        }else if(!bluetoothAdapter.isEnabled()){
            Log.d("true","开始连接");
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,ENABLE_BLUETOOTH);
        }

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
        if (null == mSensorManager) {
            Log.d(TAG, "deveice not support SensorManager");
        }
        // 参数三，检测的精准度
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAME

        ButtonListener bt = new ButtonListener();
        ImageButton qianjin = (ImageButton)findViewById(R.id.imagebutton1);
        ImageButton zuozhuan = (ImageButton)findViewById(R.id.imagebutton2);
        ImageButton youzhuan = (ImageButton)findViewById(R.id.imagebutton3);
        ImageButton houtui = (ImageButton)findViewById(R.id.imagebutton4);
        Button sensor_button = (Button)findViewById(R.id.button_change);
        TextView sensor_info = (TextView) findViewById(R.id.textview_sensor);

        qianjin.setOnTouchListener(bt);
        zuozhuan.setOnTouchListener(bt);
        youzhuan.setOnTouchListener(bt);
        houtui.setOnTouchListener(bt);
        sensor_button.setOnClickListener(bt);

//        ProgressBar progressBar_X = (ProgressBar) findViewById(R.id.progressBar_X);
//        ProgressBar progressBar_Y = (ProgressBar) findViewById(R.id.progressBar_Y);
//        ProgressBar progressBar_Z = (ProgressBar) findViewById(R.id.progressBar_Z);
    }


    class ButtonListener implements View.OnClickListener,View.OnTouchListener {

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_change:
//                    message[0] = (byte) 0x41;//设置要发送的数值
//                    vibrator();
//                    bluesend(message);//发送数值
//                    Log.e("cy08", "" + message[0]);
                    if (sensor_isOn == false){
                        sensor_isOn = true;
                        Log.e("sensor_state"," sensor is on");
                        sensor_info.setText("sensor is on");
                    }else {
                        sensor_isOn = false;
                        Log.e("sensor_state"," sensor is off");
                        sensor_info.setText("sensor is off");
                    }
                    break;
                default:
                    break;
            }
        }

        //通过按键控制小车
        public boolean onTouch(View v, MotionEvent event) {
            if (sensor_isOn == false){
                switch (v.getId()) {
                    case R.id.imagebutton1:
                        if (event.getAction() == MotionEvent.ACTION_UP) {//放开事件
                            message[0] = (byte) 0x40;//设置要发送的数值
                            bluesend(message);//发送数值
                            Log.e("cy0800000000000000000", "" + message[0]);
                        }
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下事件
                            message[0] = (byte) 0x41;//设置要发送的数值
                            bluesend(message);//发送数值
                            Log.e("cy0800000000000", "" + message[0]);
                        }
                        break;
                    case R.id.imagebutton2:
                        if (event.getAction() == MotionEvent.ACTION_UP) {//按下事件
                            message[0] = (byte) 0x40;//设置要发送的数值
                            bluesend(message);//发送数值
                            Log.d("cy08", "" + message[0]);
                        }
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {//放开事件
                            message[0] = (byte) 0x44;//设置要发送的数值
                            bluesend(message);//发送数值
                            Log.d("cy08", "" + message[0]);
                        }
                        break;
                    case R.id.imagebutton3:
                        if (event.getAction() == MotionEvent.ACTION_UP) {//按下事件
                            message[0] = (byte) 0x40;//设置要发送的数值
                            bluesend(message);//发送数值
                            Log.d("cy08", "" + message[0]);
                        }
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {//放开事件
                            message[0] = (byte) 0x43;//设置要发送的数值
                            bluesend(message);//发送数值
                            Log.d("cy08", "" + message[0]);
                        }
                        break;
                    case R.id.imagebutton4:
                        if (event.getAction() == MotionEvent.ACTION_UP) {//按下事件
                            message[0] = (byte) 0x40;//设置要发送的数值
                            bluesend(message);//发送数值
                            Log.d("cy08", "" + message[0]);
                        }
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {//放开事件
                            message[0] = (byte) 0x42;//设置要发送的数值
                            bluesend(message);//发送数值
                            Log.d("cy08", "" + message[0]);
                        }
                        break;
                    default:
                        break;
                }
            }
            return false;
        }
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

//            try{
//                sensor_info.setText(String.valueOf(x));
//            }catch (Exception e){
//                e.printStackTrace();
//            }

//            textX.setText(String.valueOf(x));
//            textY.setText(String.valueOf(y));
//            textZ.setText(String.valueOf(z));

            if (sensor_isOn == true) {
                if (y < 0 && z < 10) {
                    //textUp.setText("up");
                    message[0] = (byte) 0x41;
                    //vibrator();
                    //Toast.makeText(this,"前进",Toast.LENGTH_SHORT).show();
                    bluesend(message);
                    Log.e("sensor_data","up"+message[0]);
                } else {
                    //textUp.setText("wait");
                }

                if (y > 0 && z < 10) {
                    //textDown.setText("down");
                    message[0] = (byte) 0x42;
                    //vibrator();
                    //Toast.makeText(this,"后退",Toast.LENGTH_SHORT).show();
                    bluesend(message);
                    //Log.d("cy08",""+message[0]);
                } else {
                    //textDown.setText("wait");
                }

                if (x > 0 && z < 10) {
                    //textLeft.setText("left");
                    message[0] = (byte) 0x44;
                    //vibrator();
                    //Toast.makeText(this,"左转",Toast.LENGTH_SHORT).show();
                    bluesend(message);
                    //Log.d("cy08",""+message[0]);
                } else {
                    //textLeft.setText("wait");
                }

                if (x < 0 && z < 10) {
                    //textRgiht.setText("right");
                    message[0] = (byte) 0x43;
                    //vibrator();
                    //Toast.makeText(this,"右转",Toast.LENGTH_SHORT).show();
                    bluesend(message);
                    //Log.d("cy08",""+message[0]);
                } else {
                    //textRgiht.setText("wait");
                }

                if ((x < 5 && z > 5) || (y < 5 && z > 5)) {
                    //textStop.setText("stop");
                    message[0] = (byte) 0x40;
                    //vibrator();
                    //Toast.makeText(this,"停止",Toast.LENGTH_SHORT).show();
                    bluesend(message);
                    //Log.d("cy08",""+message[0]);
                } else {
                    //textStop.setText("wait");
                }
            }
        }
    }


    //发送数据
    public void bluesend(byte[] message){
        try{
            outputStream = bluetoothSocket.getOutputStream();
            Log.d("send", Arrays.toString(message));
            outputStream.write(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //振动器
    public void vibrator(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }


    @Override
    protected void onStop(){
        super.onStop();
        Log.d("cy950812","停止不可见活动");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
