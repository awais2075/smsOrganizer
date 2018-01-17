package com.awais2075gmail.awais2075.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 * Created by R Ankit on 30-12-2016.
 */

public class DeliverReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Toast.makeText(context, "Sms Delivered",
                        Toast.LENGTH_SHORT).show();
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(context, "Sms Not Delivered",
                        Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
