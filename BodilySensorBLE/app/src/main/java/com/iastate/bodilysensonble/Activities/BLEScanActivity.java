package com.iastate.bodilysensonble.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import com.iastate.bodilysensonble.Adapters.BluetoothListAdapter;
import com.iastate.bodilysensonble.R;

import java.util.ArrayList;
import java.util.List;

public class BLEScanActivity extends AppCompatActivity {

    private static final String TAG = "Home";
    private static final int PERMISSION_LOC_REQ_CODE = 2;
    private static final int PERMISSION_GPS_REQ_CODE = 1;

    private FirebaseAuth mAuth;

    Button btn_scan, btn_cancel, btn_clear, btn_signout;
    RecyclerView ble_recyclerview;
    BluetoothListAdapter bluetoothListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_scan);

        mAuth = FirebaseAuth.getInstance();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                String token = task.getResult();
                Log.d(TAG, "onComplete: Token: " + token);
            }
        });

        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleManager.getInstance().cancelScan();
            }
        });

        btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothListAdapter.clear();
                bluetoothListAdapter.notifyDataSetChanged();
            }
        });

        ble_recyclerview = findViewById(R.id.ble_recyclerview);
        bluetoothListAdapter = new BluetoothListAdapter(this);
        bluetoothListAdapter.setOnDeviceClickListener(new BluetoothListAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                if(!BleManager.getInstance().isConnected(bleDevice)){
                    BleManager.getInstance().cancelScan();
                    connect(bleDevice);
                }
            }

            @Override
            public void onDisConnect(BleDevice bleDevice) {
                if(BleManager.getInstance().isConnected(bleDevice)){
                    BleManager.getInstance().disconnect(bleDevice);
                }
            }

            @Override
            public void onDetail(BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra(DetailActivity.CONNECTED_DEVICE_DETAIL, bleDevice);
                    startActivity(intent);
                }
            }
        });

        ble_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        ble_recyclerview.setAdapter(bluetoothListAdapter);

        btn_signout = findViewById(R.id.btn_signOut);
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent signInIntent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signInIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void checkPermissions(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null){
            Toast.makeText(this, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        }else{
            if(!bluetoothAdapter.isEnabled()){
                Toast.makeText(this, "Please turn Bluetooth on", Toast.LENGTH_SHORT).show();
            }else{
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                List<String> permissionDeniedList = new ArrayList<>();
                for (String permission : permissions) {
                    int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        onPermissionGranted(permission);
                    } else {
                        permissionDeniedList.add(permission);
                    }
                }
                if (!permissionDeniedList.isEmpty()) {
                    String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
                    ActivityCompat.requestPermissions(this, deniedPermissions, PERMISSION_LOC_REQ_CODE);
                }
            }
        }
    }

    private void onPermissionGranted(String permission) {
        if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isGPSOn()) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.alert_title)
                        .setMessage(R.string.alert_msg)
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                        .setPositiveButton(R.string.setting,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivityForResult(intent, PERMISSION_GPS_REQ_CODE);
                                    }
                                })

                        .setCancelable(false)
                        .show();
            } else {
                scan();
            }
        }
    }

    private Boolean isGPSOn(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    private void scan(){
        BleManager.getInstance().scan(new BleScanCallback() {

            @Override
            public void onScanStarted(boolean success) {
                bluetoothListAdapter.clearNonConnectedDevices();
                Log.d(TAG, "onScanStarted: Item count " + bluetoothListAdapter.getItemCount());
                bluetoothListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                bluetoothListAdapter.add(bleDevice);
                bluetoothListAdapter.notifyDataSetChanged();
            }


            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {

            }
        });
    }

    private void connect(BleDevice bleDevice){
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {

            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                bluetoothListAdapter.add(bleDevice);
                bluetoothListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                bluetoothListAdapter.remove(device);
                bluetoothListAdapter.notifyDataSetChanged();
            }
        });
    }
}