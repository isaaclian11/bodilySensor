package com.iastate.bodilysensonble.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

    private static final int STATE_PER_SEC = 0;
    private static final int STATE_PER_MINUTE = 1;
    private static final int STATE_PER_HR = 2;
    private static final int STATE_PER_DAY = 3;

    private static final int STATE_LBS = 0;
    private static final int STATE_KG = 1;

    private int HEART_RATE_STATE = STATE_PER_MINUTE;
    private int RESPIRATION_RATE_STATE = STATE_PER_MINUTE;
    private int WEIGHT_STATE = STATE_LBS;

    private boolean IS_RECORDING = false;

    private BleDevice thisBLEDevice;
    Button btn_record;
    TextView respiration, weight, heart;

    private String currentHeartRateInHex;
    private String currentRespirationRateInHex;
    private String currentWeightInHex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        thisBLEDevice = intent.getParcelableExtra(CONNECTED_DEVICE_DETAIL);

        respiration = findViewById(R.id.text_view_resp);
        weight = findViewById(R.id.text_view_weight);
        heart = findViewById(R.id.text_view_heart);

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Heart rate state: " + HEART_RATE_STATE);
                switch(HEART_RATE_STATE){
                    case STATE_PER_SEC:
                        HEART_RATE_STATE = STATE_PER_MINUTE;
                        break;
                    case STATE_PER_MINUTE:
                        HEART_RATE_STATE = STATE_PER_HR;
                        break;
                    case STATE_PER_HR:
                        HEART_RATE_STATE = STATE_PER_DAY;
                        break;
                    case STATE_PER_DAY:
                        HEART_RATE_STATE = STATE_PER_SEC;
                        break;
                }
                Log.d(TAG, "onClick: New heart rate state: " + HEART_RATE_STATE);
                updateHeartRateView();
            }
        });

        respiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Respiration Clicked");
                switch(RESPIRATION_RATE_STATE){
                    case STATE_PER_SEC:
                        RESPIRATION_RATE_STATE = STATE_PER_MINUTE;
                        break;
                    case STATE_PER_MINUTE:
                        RESPIRATION_RATE_STATE = STATE_PER_HR;
                        break;
                    case STATE_PER_HR:
                        RESPIRATION_RATE_STATE = STATE_PER_DAY;
                        break;
                    case STATE_PER_DAY:
                        RESPIRATION_RATE_STATE = STATE_PER_SEC;
                        break;
                }
                updateRespView();
            }
        });

        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(WEIGHT_STATE){
                    case STATE_LBS:
                        WEIGHT_STATE = STATE_KG;
                    case STATE_KG:
                        WEIGHT_STATE = STATE_LBS;
                }
                updateWeightView();
            }
        });

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

    @SuppressLint("SetTextI18n")
    private void displayData(String hexString){
        int dataEnd = hexString.length()-2;
        Log.d(TAG, "displayData: " + hexString.substring(2,3));

        boolean isRespirationInfo = hexString.substring(0, 2).equals("02") && hexString.substring(4, 6).equals("00");
        boolean isHeartInfo = hexString.substring(0, 2).equals("03") && hexString.substring(4, 6).equals("00");
        boolean isWeightInfo = hexString.substring(0, 2).equals("04") && hexString.substring(4, 6).equals("00");

        if(isRespirationInfo){
            currentRespirationRateInHex = hexString;
            updateRespView();
        }else if(isHeartInfo){
            currentHeartRateInHex = hexString;
            updateHeartRateView();
        }else if(isWeightInfo){
            currentWeightInHex = hexString;
            updateWeightView();
        }

        //String
        StringBuilder dataToString = new StringBuilder();
        for (int i = 0; i < hexString.substring(6, dataEnd).length(); i+=2) {
            String str = hexString.substring(6, dataEnd).substring(i, i+2);
            dataToString.append((char)Integer.parseInt(str, 16));
        }

    }

    @SuppressLint("SetTextI18n")
    private void updateRespView(){
        int dataEnd = currentRespirationRateInHex.length() - 2;
        if (currentRespirationRateInHex.substring(2, 4).equals("01")) { //integer
            int dataToInt = Integer.decode("0x" + currentRespirationRateInHex.substring(6, dataEnd));
            switch (RESPIRATION_RATE_STATE) {
                case 0:
                    respiration.setText("Respiration Rate: " + dataToInt/60 + "/second");
                    break;
                case 1:
                    respiration.setText("Respiration Rate: " + dataToInt + "/minute");
                    break;
                case 2:
                    respiration.setText("Respiration Rate: " + dataToInt * 60 + "/hour");
                    break;
                case 3:
                    respiration.setText("Respiration Rate: " + dataToInt * 60 * 24 + "/day");
                    break;
            }
        } else if (currentRespirationRateInHex.substring(2, 4).equals("02")) { //double
            long longValue = hexToLong(currentRespirationRateInHex.substring(6, dataEnd));
            double dataToDouble = Double.longBitsToDouble(longValue);
            switch(RESPIRATION_RATE_STATE){
                case 0:
                    heart.setText("Respiration Rate: " + dataToDouble/60 + "/second");
                    break;
                case 1:
                    heart.setText("Respiration Rate: " + dataToDouble + "/minute");
                    break;
                case 2:
                    heart.setText("Respiration Rate: " + dataToDouble * 60 + "/hour");
                    break;
                case 3:
                    heart.setText("Respiration Rate: " + dataToDouble * 60 * 24 + "/day");
                    break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateHeartRateView() {
        int dataEnd = currentHeartRateInHex.length() - 2;
        if (currentHeartRateInHex.substring(2, 4).equals("01")) { //integer
            int dataToInt = Integer.decode("0x" + currentHeartRateInHex.substring(6, dataEnd));
            switch (HEART_RATE_STATE) {
                case STATE_PER_SEC:
                    heart.setText("Heart Rate: " + dataToInt/60 + "/second");
                    break;
                case STATE_PER_MINUTE:
                    heart.setText("Heart Rate: " + dataToInt + "/minute");
                    break;
                case STATE_PER_HR:
                    heart.setText("Heart Rate: " + dataToInt * 60 + "/hour");
                    break;
                case STATE_PER_DAY:
                    heart.setText("Heart Rate: " + dataToInt * 60 * 24 + "/day");
                    break;
            }
        } else if (currentHeartRateInHex.substring(2, 4).equals("02")) { //double
            long longValue = hexToLong(currentHeartRateInHex.substring(6, dataEnd));
            double dataToDouble = Double.longBitsToDouble(longValue);
            switch(HEART_RATE_STATE){
                case STATE_PER_SEC:
                    heart.setText("Heart Rate: " + dataToDouble/60 + "/second");
                    break;
                case STATE_PER_MINUTE:
                    heart.setText("Heart Rate: " + dataToDouble + "/minute");
                    break;
                case STATE_PER_HR:
                    heart.setText("Heart Rate: " + dataToDouble * 60 + "/hour");
                    break;
                case STATE_PER_DAY:
                    heart.setText("Heart Rate: " + dataToDouble * 60 * 24 + "/day");
                    break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateWeightView() {
        int dataEnd = currentWeightInHex.length() - 2;
        if (currentWeightInHex.substring(2, 4).equals("01")) { //integer
            int dataToInt = Integer.decode("0x" + currentHeartRateInHex.substring(6, dataEnd));
            switch (WEIGHT_STATE) {
                case STATE_LBS:
                    weight.setText("Weight: " + dataToInt + "lbs");
                    break;
                case STATE_KG:
                    weight.setText("Weight: " + dataToInt * 0.454 + "kg");
                    break;
            }
        } else if (currentHeartRateInHex.substring(2, 4).equals("02")) { //double
            long longValue = hexToLong(currentHeartRateInHex.substring(6, dataEnd));
            double dataToDouble = Double.longBitsToDouble(longValue);
            switch(WEIGHT_STATE){
                case STATE_LBS:
                    weight.setText("Weight: "+ dataToDouble + "lbs");
                    break;
                case STATE_KG:
                    weight.setText("Weight: " + dataToDouble * 0.454 + "kg");
                    break;
            }
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