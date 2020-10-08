package com.iastate.bodilysensonble.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.iastate.bodilysensonble.R;

public class SignInActivity extends AppCompatActivity {

    public String TAG = "SignInActivity";
    private String USER_ID = "USER_ID";

    private FirebaseAuth mAuth;

    EditText email, password;
    Button signInBtn, signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.signIn_email);
        password = findViewById(R.id.signIn_password);
        signInBtn = findViewById(R.id.signIn_btn);
        signUpBtn = findViewById(R.id.signIn_signup);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "One or more fields is empty.", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Intent bleScanIntent = new Intent(getApplicationContext(), BLEScanActivity.class);
                            bleScanIntent.putExtra(USER_ID, task.getResult().getUser().getUid());
                            startActivity(bleScanIntent);
                        }
                    });
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}