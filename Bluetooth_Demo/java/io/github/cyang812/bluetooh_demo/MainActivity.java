//version 1.0  github

package io.github.cyang812.bluetooh_demo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public byte[] message = new byte[1];
    private Vibrator vibrator;

    private int ENABLE_BLUETOOTH=2;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket = null;
    OutputStream outputStream = null;
    private static final UUID MY_UUID_SECURE=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String blueAddress="98:D3:31:70:9B:CA";//蓝牙模块的MAC地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(bluetoothAdapter==null){
            Toast.makeText(this,"不支持蓝牙",Toast.LENGTH_LONG).show();
            finish();
        }else if(!bluetoothAdapter.isEnabled()){
            Log.d("true","开始连接");
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,ENABLE_BLUETOOTH);
            }


        ImageButton qianjin = (ImageButton)findViewById(R.id.imagebutton1);
        ImageButton zuozhuan = (ImageButton)findViewById(R.id.imagebutton2);
        ImageButton youzhuan = (ImageButton)findViewById(R.id.imagebutton3);
        ImageButton houtui = (ImageButton)findViewById(R.id.imagebutton4);
        ImageButton tingzhi = (ImageButton)findViewById(R.id.imagebutton5);

        qianjin.setOnClickListener(this);
        zuozhuan.setOnClickListener(this);
        youzhuan.setOnClickListener(this);
        houtui.setOnClickListener(this);
        tingzhi.setOnClickListener(this);

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


    public void onClick(View v){
        switch (v.getId()){
            case R.id.imagebutton1:
                message[0]= (byte) 0x41;
                vibrator();
                Toast.makeText(this,"前进",Toast.LENGTH_SHORT).show();
                bluesend(message);
                Log.d("cy08",""+message[0]);
                break;

            case R.id.imagebutton2:
                message[0]= (byte) 0x44;
                vibrator();
                Toast.makeText(this,"左转",Toast.LENGTH_SHORT).show();
                bluesend(message);
                Log.d("cy08",""+message[0]);
                break;

            case R.id.imagebutton3:
                message[0]= (byte) 0x43;
                vibrator();
                Toast.makeText(this,"右转",Toast.LENGTH_SHORT).show();
                bluesend(message);
                Log.d("cy08",""+message[0]);
                break;

            case R.id.imagebutton4:
                message[0]= (byte) 0x42;
                vibrator();
                Toast.makeText(this,"后退",Toast.LENGTH_SHORT).show();
                bluesend(message);
                Log.d("cy08",""+message[0]);
                break;

            case R.id.imagebutton5:
                message[0]= (byte) 0x40;
                vibrator();
                Toast.makeText(this,"停止",Toast.LENGTH_SHORT).show();
                bluesend(message);
                Log.d("cy08",""+message[0]);
                break;

            default:
                message[0]= (byte) 0x40;
                vibrator();
                Toast.makeText(this,"停止",Toast.LENGTH_SHORT).show();
                bluesend(message);
                Log.d("cy08",""+message[0]);
                break;
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
