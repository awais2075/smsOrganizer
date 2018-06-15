package com.awais2075gmail.awais2075.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075.activity.ConversationActivity;
import com.awais2075gmail.awais2075.service.SaveSmsService;
import com.awais2075gmail.awais2075.util.Constants;
import com.awais2075gmail.awais2075.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class SmsReceiver extends BroadcastReceiver {


    private String TAG = SmsReceiver.class.getSimpleName();
    private Bundle bundle;
    private SmsMessage currentSMS;
    private int mNotificationId = 101;
    private static List<String> autoResponseList = new ArrayList<>();


    @Override
    public void onReceive(Context context, Intent intent) {
        autoResponseList.add("+923368480135");
        autoResponseList.add("+923344558489");
        autoResponseList.add("+923317411113");


        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

            Log.e(TAG, "smsReceiver");  

            bundle = intent.getExtras();
            String senderNo = null;
            String message = null;
            if (bundle != null) {
                Object[] pdu_Objects = (Object[]) bundle.get("pdus");
                if (pdu_Objects != null) {
                    for (Object aObject : pdu_Objects) {
                        currentSMS = getIncomingMessage(aObject, bundle);
                        senderNo = currentSMS.getDisplayOriginatingAddress();
                        message = currentSMS.getDisplayMessageBody();
                        changeMode(context, message);
                        issueNotification(context, senderNo, message);
                        saveSmsInInbox(context, currentSMS);
                    }
                }
                this.abortBroadcast();

            }
            if (autoResponseList.contains(senderNo)) {
                Utils.send(context, senderNo, "Testing Phone");
            }
        }
    }


    private void saveSmsInInbox(Context context, SmsMessage sms) {

        Intent serviceIntent = new Intent(context, SaveSmsService.class);
        serviceIntent.putExtra("sender_no", sms.getDisplayOriginatingAddress());
        serviceIntent.putExtra("message", sms.getDisplayMessageBody());
        serviceIntent.putExtra("date", sms.getTimestampMillis());
        context.startService(serviceIntent);

    }

    private void issueNotification(Context context, String senderNo, String message) {

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.send_sms_button);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setLargeIcon(icon)
                        .setSmallIcon(R.mipmap.send_sms_button)
                        .setContentTitle(senderNo)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setAutoCancel(true)
                        .setContentText(message);

        Intent resultIntent = new Intent(context, ConversationActivity.class);
        resultIntent.putExtra(Constants.CONTACT_NAME, senderNo);
        resultIntent.putExtra(Constants.FROM_SMS_RECIEVER, true);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSMS;
    }

    private void changeMode(Context context, String text) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if ((audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) && (text.equals("123"))) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }
}
