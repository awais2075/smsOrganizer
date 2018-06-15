package com.awais2075gmail.awais2075.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.awais2075gmail.awais2075.Manifest;

public class Permission {
    private Context context;
    private int multiplePermissionsRequestCode;
    private String[] multiplePermissions;

    public Permission(Context context, int multiplePermissionsRequestCode, String[] multiplePermissions) {
        this.context = context;
        this.multiplePermissionsRequestCode = multiplePermissionsRequestCode;
        this.multiplePermissions = multiplePermissions;
    }


    public boolean checkPermissions() {
        for (int i=0; i<multiplePermissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, multiplePermissions[i]) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions((Activity) context, multiplePermissions, multiplePermissionsRequestCode);
    }

    public boolean checkResults(int requestCOde, int[] grantResults) {
        return requestCOde == multiplePermissionsRequestCode && grantResults.length >0 && check(grantResults);

    }

    private boolean check(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
