package com.awais2075gmail.awais2075.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075.util.Constants;
import com.awais2075gmail.awais2075.util.Utils;
import com.google.android.gms.common.ConnectionResult;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText edit_userEmail;
    private EditText edit_userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);

        checkPermissions();
        init();
        emailInit();
        gmailInit();
        Toast.makeText(this, Utils.userId+" is userId in Login Activity", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected int setView() {
        return R.layout.activity_login;
    }


    private void init() {
        edit_userEmail = findViewById(R.id.edit_userEmail);
        edit_userPassword = findViewById(R.id.edit_userPassword);

        findViewById(R.id.button_googleSignIn).setOnClickListener(this);
        findViewById(R.id.button_facebookSignIn).setOnClickListener(this);
        findViewById(R.id.button_emailSignIn).setOnClickListener(this);
        findViewById(R.id.button_forgotPassword).setOnClickListener(this);
        findViewById(R.id.button_register).setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_googleSignIn:
                gmailSignIn();
                break;
            case R.id.button_facebookSignIn:
                //startActivity(new Intent(this, GroupActivity.class));
                break;
            case R.id.button_emailSignIn:
                signIn();
                break;
            case R.id.button_forgotPassword:
                break;
            case R.id.button_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        isEmailSignedIn();
        isGoogleSignedIn();
    }

    private void signIn() {
        String userEmail = edit_userEmail.getText().toString().trim();
        String userPassword = edit_userPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)) {
            edit_userEmail.setError("Enter Email Address");
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            edit_userPassword.setError("Enter Password");
            return;
        }

        emailSignIn(userEmail, userPassword);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /*Runtime Permissions*/
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //Toast.makeText(this, "Lower Version Permission Granted", Toast.LENGTH_SHORT).show();
            //getSupportLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);
        } else if (requestPermissions()) {
            //Toast.makeText(this, "Upper Version Permission Granted", Toast.LENGTH_SHORT).show();
            //getSupportLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);
        }

    }

    private boolean requestPermissions() {
        int permissionReadContacts = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int permissionReadSms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int permissionReceiveSms = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int permissionSendSms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        //int permissionWriteSms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionReadContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (permissionReadSms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionReceiveSms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (permissionSendSms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), Constants.MULTIPLE_PERMISSIONS_REQUEST);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.MULTIPLE_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(this, "Permitted On Request Method", Toast.LENGTH_SHORT).show();
                    //getSupportLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);
                } else {
                    //You did not accept the request can not use the functionality.
                    //Toast.makeText(this, "Not Permitted", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }


}
