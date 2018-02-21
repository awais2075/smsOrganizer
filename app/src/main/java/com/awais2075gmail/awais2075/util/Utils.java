package com.awais2075gmail.awais2075.util;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.awais2075gmail.awais2075.activity.MessageActivity;
import com.awais2075gmail.awais2075.model.Contact;
import com.awais2075gmail.awais2075.model.SMS;
import com.awais2075gmail.awais2075.receiver.SentReceiver;
import com.google.firebase.database.DatabaseReference;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Muhammad Awais Rashi on 30-Nov-17.
 */

public class Utils {

    public static HashMap<String, String> phoneContactsMap;
    public static String userId;
    public static String groupId;
    public static boolean check;
    public static DatabaseReference contactDatabaseReference;
    public static List<SMS> unknownSmsList = new ArrayList<>();
    public static List<Contact> phoneContactsList = new ArrayList<>();

    public static String getCountry(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        return telephonyManager.getNetworkCountryIso().toUpperCase();
    }

    public static String getPhoneNumberFormat(String phoneNumber, String country) {
        //String swissNumberStr = "044 668 18 00";
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(phoneNumber, country);
            // return phoneUtil.isValidNumber(phoneNumberProto) == true ? "valid" : "phone no not valid";
            return phoneUtil.format(phoneNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            //System.err.println("NumberParseException was thrown: " + e.toString());
            return phoneNumber;

        }
    }

    public static String isValidNumer(String phoneNumber, String country) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber numberProto = null;
        try {
            numberProto = phoneUtil.parse(phoneNumber, country);
            if (phoneUtil.isValidNumber(numberProto)) {
                return getPhoneNumberFormat(phoneNumber, country);
            }

        } catch (NumberParseException e) {
            return phoneNumber;
        }
        return phoneNumber;
    }

   /* public void sendSMSNow(Context context, String smsAddress, String smsBody) {

        BroadcastReceiver sendBroadcastReceiver = new SentReceiver();
        BroadcastReceiver deliveryBroadcastReciever = new DeliverReceiver();

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);

        registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));
        registerReceiver(deliveryBroadcastReciever, new IntentFilter(DELIVERED));

        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(smsAddress, null, smsBody, sentPI, deliveredPI);
        }catch (Exception e){
            Toast.makeText(context,("Sms Not sent"),Toast.LENGTH_SHORT).show();
        }
    }*/

    public static String getSmsType(String smsType) {
        switch (smsType) {
            case "1":
                smsType = "Inbox";
                break;
            case "2":
                smsType = "Sent";
                break;
            case "3":
                smsType = "Draft";
                break;
            case "4":
                smsType = "Outbox";
                break;
            case "5":
                smsType = "Failed";
                break;
            case "6":
                smsType = "Queued/Undelivered";
                break;
        }
        return smsType;
    }

    public static String getName (String number) {
        if (phoneContactsMap.containsKey(number)) {
            return phoneContactsMap.get(number);
        } else {
            return number;
        }
    }

    public static void send(Context context, String recipient, String message) {
        final int SMS_DRAFT = 2;
        final String TAG = "msg";
        final Uri URI_SENT = Uri.parse("content://sms/sent");
        final String MESSAGE_SENT_ACTION = "com.android.mms.transaction.MESSAGE_SENT";
        final String[] PROJECTION_ID = new String[]{BaseColumns._ID};
        final String ADDRESS = "address";
        final String READ = "read";
        final String TYPE = "type";
        final String BODY = "body";
        final String DATE = "date";

        Log.d(TAG, "text: "+recipient);

        // save draft
        final ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(TYPE, SMS_DRAFT);
        values.put(BODY, message);
        values.put(READ, 1);
        values.put(ADDRESS, recipient);
        Uri draft = null;
        // save sms to content://sms/sent
        Cursor cursor = cr.query(Uri.parse("content://sms/sent"), PROJECTION_ID,
                TYPE + " = " + SMS_DRAFT + " AND " + ADDRESS + " = '" + recipient
                        + "' AND " + BODY + " like '" + message.replace("'", "_") + "'", null, DATE
                        + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            draft = URI_SENT.buildUpon().appendPath(cursor.getString(0)).build();
            Log.d(TAG, "skip saving draft: "+ draft);
        } else {
            try {
                draft = cr.insert(URI_SENT, values);
                Log.d(TAG, "draft saved: "+ draft);
            } catch (IllegalArgumentException | SQLiteException | NullPointerException e) {
                Log.e(TAG, "unable to save draft", e);
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        Log.d(TAG, "send messages to: "+ recipient);

        final ArrayList<PendingIntent> sentIntents = new ArrayList<>();
        try {
            SmsManager smsmgr = SmsManager.getDefault();

            final ArrayList<String> messages = smsmgr.divideMessage(message);
            for (String m : messages) {
                Log.d(TAG, "divided messages: "+ m);

                final Intent sent = new Intent(MESSAGE_SENT_ACTION, draft, context, SentReceiver.class);
                sentIntents.add(PendingIntent.getBroadcast(context, 0, sent, 0));
            }
            smsmgr.sendMultipartTextMessage(recipient, null, messages, sentIntents, null);
            Log.i(TAG, "message sent");

        } catch (Exception e) {
            Log.e(TAG, "unexpected error", e);
            for (PendingIntent pi : sentIntents) {
                if (pi != null) {
                    try {
                        pi.send();
                    } catch (PendingIntent.CanceledException e1) {
                        Log.e(TAG, "unexpected error", e1);
                    }
                }
            }
        }
    }

    public static int contextMenu(Context context) {
        String[] items = {"Delete", "Forward"};
        final int[] position = {-1};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context
                , android.R.layout.simple_list_item_1, android.R.id.text1, items);

        new AlertDialog.Builder(context)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        position[0] = i;
                    }
                })
                .show();
        return position[0];
    }
}
