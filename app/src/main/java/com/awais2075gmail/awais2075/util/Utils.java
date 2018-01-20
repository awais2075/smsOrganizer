package com.awais2075gmail.awais2075.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.awais2075gmail.awais2075.firebase.Group;
import com.awais2075gmail.awais2075.model.Contact;
import com.awais2075gmail.awais2075.model.SMS;
import com.awais2075gmail.awais2075.receiver.DeliverReceiver;
import com.awais2075gmail.awais2075.receiver.SentReceiver;
import com.google.firebase.database.DatabaseReference;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

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
/*
    public static void setDefaults(Context context, String userIdKey, String userIdValue, String loginIdKey, String loginIdValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(userIdKey, userIdValue);
        editor.putString(loginIdKey, loginIdValue);
        editor.commit();
    }*/


    /*public static void setDefaults(Context context, String userIdKey, String userIdValue) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(userIdKey, userIdValue);
//        editor.putString(loginIdKey, loginIdValue);
        editor.apply();
    }

    public static String getUserId(String userIdKey, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(userIdKey, null);

    }


    public static String getLoginId(String loginIdKey, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(loginIdKey, null);
    }

    public static void logout(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        //.startActivity(new Intent(mCtx, LoginActivity.class));
    }*/
}
