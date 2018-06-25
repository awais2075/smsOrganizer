package com.awais2075gmail.awais2075.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private EditText edit_registerUserEmail;
    private EditText edit_registerUserPassword;
    private EditText edit_confirmPassword;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);

        init();

    }

    @Override
    protected int setView() {
        return R.layout.activity_register;
    }

    private void init() {
        edit_registerUserEmail = findViewById(R.id.edit_registerUserEmail);
        edit_registerUserPassword = findViewById(R.id.edit_registerUserPassword);
        edit_confirmPassword = findViewById(R.id.edit_confirmPassword);

        findViewById(R.id.button_register).setOnClickListener(this);
        findViewById(R.id.textView_alreadyHaveAccount).setOnClickListener(this);

        /*Firebase Email Registration*/
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                register();
                break;
            case R.id.textView_alreadyHaveAccount:
                finish();
                break;
        }
    }


    private void register() {
        String userEmail = edit_registerUserEmail.getText().toString().trim();
        String userPassword = edit_registerUserPassword.getText().toString().trim();
        String confirmPassword = edit_confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)) {
            edit_registerUserEmail.setError("Enter Email Address");
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            edit_registerUserPassword.setError("Enter Password");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            edit_confirmPassword.setError("Confirm Password");
            return;
        }

        if (!userPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
            edit_confirmPassword.setText("");
            edit_registerUserPassword.setText("");
            return;
        }

        emailRegistration(userEmail, userPassword);

        /*mFirebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "User Not Created", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}