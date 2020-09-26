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
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.iastate.bodilysensonble.R;


public class DetailActivity extends AppCompatActivity {

    public String TAG = "DetailActivity";

    public static final String CONNECTED_DEVICE_DETAIL = "CONNECTED_DEVICE_DETAIL";
    private static final String SERVICE_UUID = "00001810-0000-1000-8000-00805f9b34fb";
    private static final String READ_UUID = "5c2355f3-2884-466c-8d41-0f31cfa49f7e";

    private boolean IS_RECORDING = false;

    private BleDevice thisBLEDevice;
    Button btn_record;
    TextView respiration, weight, heart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        thisBLEDevice = intent.getParcelableExtra(CONNECTED_DEVICE_DETAIL);

        respiration = findViewById(R.id.text_view_resp);
        weight = findViewById(R.id.text_view_weight);
        heart = findViewById(R.id.text_view_heart);

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
                                    String hexString = HexUtil.formatHexString(data, false);
                                    displayData(hexString);
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

    private void displayData(String hexString){
        String dataToDisplay;
        int dataEnd = hexString.length()-2;

        Log.d(TAG, "displayData: " + hexString.substring(2,3));

        if(hexString.substring(2, 4).equals("01")){ //integer
            int dataToInt = Integer.decode("0x" + hexString.substring(6, dataEnd));
            dataToDisplay = String.valueOf(dataToInt);
        }else if(hexString.substring(2, 4).equals("02")){ //double
            Long longValue = hexToLong(hexString.substring(6, dataEnd));
            Double dataToDouble = Double.longBitsToDouble(longValue);
            dataToDisplay = String.valueOf(dataToDouble);
        }else{ //String
            StringBuilder dataToString = new StringBuilder();
            for (int i = 0; i < hexString.substring(6, dataEnd).length(); i+=2) {
                String str = hexString.substring(6, dataEnd).substring(i, i+2);
                dataToString.append((char)Integer.parseInt(str, 16));
            }
            dataToDisplay = dataToString.toString();
        }

        if(hexString.substring(0, 2).equals("02") && hexString.substring(4, 6).equals("00")){ //Respiration
            respiration.setText("Respiration: " + dataToDisplay + "/min");
        }else if(hexString.substring(0, 2).equals("03") && hexString.substring(4, 6).equals("00")){ //Heartbeat
            heart.setText("Heartbeat: " + dataToDisplay + "/min");
        }else if(hexString.substring(0, 2).equals("04") && hexString.substring(4, 6).equals("00")){ //Weight
            weight.setText("Weight: "+ dataToDisplay + "lbs");
        }
    }

    private static long hexToLong(String hex){
        if (hex.length() == 16) {
            return (hexToLong(hex.substring(0, 1)) << 60)
                    | hexToLong(hex.substring(1));
        }
        return Long.parseLong(hex, 16);
    }
}