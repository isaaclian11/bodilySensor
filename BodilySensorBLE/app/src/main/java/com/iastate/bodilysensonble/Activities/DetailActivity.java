package com.iastate.bodilysensonble.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.iastate.bodilysensonble.R;

import java.util.Arrays;


public class DetailActivity extends AppCompatActivity {

    public String TAG = "DetailActivity";

    public static final String CONNECTED_DEVICE_DETAIL = "CONNECTED_DEVICE_DETAIL";
    private static final String SERVICE_UUID = "00001810-0000-1000-8000-00805f9b34fb";
    private static final String READ_UUID = "5c2355f3-2884-466c-8d41-0f31cfa49f7e";

    private boolean IS_RECORDING = false;

    private BleDevice thisBLEDevice;
    Button btn_record;
    TextView data_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        thisBLEDevice = intent.getParcelableExtra(CONNECTED_DEVICE_DETAIL);

        data_read = findViewById(R.id.data_read);

        btn_record = findViewById(R.id.btn_record);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!IS_RECORDING){
                    IS_RECORDING = true;
                    btn_record.setText(R.string.stop);
                    if(thisBLEDevice!=null){
                            BleManager.getInstance().indicate(thisBLEDevice, SERVICE_UUID, READ_UUID, new BleIndicateCallback() {
                                @Override
                                public void onIndicateSuccess() {

                                }

                                @Override
                                public void onIndicateFailure(BleException exception) {

                                }

                                @Override
                                public void onCharacteristicChanged(byte[] data) {
                                    Log.d(TAG, "onCharacteristicChanged: Characteristic Changed");
                                    char[] hexString = HexUtil.formatHexString(data, false).toCharArray();
                                    decodeHexValue(hexString);
//                                    data_read.append(Arrays.toString(hexString));
//                                    data_read.append("\n");
                                }
                            });

                    }
                }
                else{
                    IS_RECORDING = false;
                    btn_record.setText(R.string.record);
                }
            }
        });

    }

    private void decodeHexValue(char[] data){

    }

//    private void runThread(final byte[] data){
//        new Thread() {
//            public void run(){
//                while(IS_RECORDING){
//                    try{
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                data_read.append(HexUtil.formatHexString(data, true));
//                                data_read.append("\n");
//                                Log.d(TAG, "onReadSuccess: " + Arrays.toString(data));
//                            }
//                        });
//                        Thread.sleep(300);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
//    }
}