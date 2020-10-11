package com.iastate.bodilysensonble.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iastate.bodilysensonble.Helpers.DynamoDBAccess;
import com.iastate.bodilysensonble.R;


public class SignUpActivity extends AppCompatActivity {

    public final String TAG = "SignUpActivity";

    private FirebaseAuth mAuth;

    private String user_gender;
    private String user_type;

    Button signUpBtn;
    EditText email, password, confirmPassword, phoneNumber, birthday, firstName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        final Spinner genderSpinner = findViewById(R.id.signUp_gender);
        ArrayAdapter<CharSequence> genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderSpinnerAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner userTypeSpinner = findViewById(R.id.signUp_user_type);
        ArrayAdapter<CharSequence> userTypeSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.user_types, android.R.layout.simple_spinner_item);
        userTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(userTypeSpinnerAdapter);
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        firstName = findViewById(R.id.signUp_first_name);
        lastName = findViewById(R.id.signUp_last_name);
        email = findViewById(R.id.signUp_email);
        password = findViewById(R.id.signUp_password);
        confirmPassword = findViewById(R.id.signUp_confirm_password);
        phoneNumber = findViewById(R.id.signUp_ph);
        signUpBtn = findViewById(R.id.signUp_btn);
        birthday = findViewById(R.id.signUp_birthday);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstNameString = firstName.getText().toString();
                final String lastNameString = lastName.getText().toString();
                final String emailString = email.getText().toString();
                final String passwordString = password.getText().toString();
                String confirmPasswordString = confirmPassword.getText().toString();
                final String phoneNumberString = phoneNumber.getText().toString();
                final String birthdayString = birthday.getText().toString();

                if(firstNameString.isEmpty() || lastNameString.isEmpty() || emailString.isEmpty() || passwordString.isEmpty() || confirmPasswordString.isEmpty() ||
                         phoneNumberString.isEmpty() || user_gender.isEmpty() || user_type.isEmpty() || birthdayString.isEmpty() && passwordString.equals(confirmPasswordString)){
                    Toast.makeText(getApplicationContext(), "One more more fields is blank or passwords do not match", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                String userId = currentUser.getUid();

                                final Document userDetails = new Document();
                                userDetails.put("user_id", userId);
                                userDetails.put("first_name", firstNameString);
                                userDetails.put("last_name", lastNameString);
                                userDetails.put("email", emailString);
                                userDetails.put("sex", user_gender.toUpperCase());
                                userDetails.put("user_type", user_type.toUpperCase());
                                userDetails.put("phone_number", phoneNumberString);
                                userDetails.put("birthday", birthdayString);

                                AddUserAsyncTask userAsyncTask = new AddUserAsyncTask();
                                userAsyncTask.execute(userDetails);

                                Intent detailIntent = new Intent(getApplicationContext(), BLEScanActivity.class);
                                startActivity(detailIntent);
                            }else {
                                Toast.makeText(getApplicationContext(), task.getException().toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent detailIntent = new Intent(getApplicationContext(), BLEScanActivity.class);
            startActivity(detailIntent);
        }
    }

    private class AddUserAsyncTask extends AsyncTask<Document, Void, Boolean> {
        Boolean putSuccess = false;
        @Override
        protected Boolean doInBackground(Document... documents) {
            DynamoDBAccess access = DynamoDBAccess.getInstance(getApplicationContext());
            try{
                putSuccess = access.putItem(documents[0]);
            }catch (Exception e){
                Log.d(TAG, "doInBackground: Put item failed");
            }
            return putSuccess;
        }
    }

}