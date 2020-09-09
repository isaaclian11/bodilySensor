package com.iastate.bodilysensonble.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.iastate.bodilysensonble.R;

public class ServicesActivity extends AppCompatActivity {

    public static final String BLUETOOTH_DEVICE = "BLUETOOTH_DEVICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
    }
}