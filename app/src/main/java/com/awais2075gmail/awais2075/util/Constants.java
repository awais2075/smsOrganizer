package com.awais2075gmail.awais2075.util;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Muhammad Awais Rashi on 18-Dec-17.
 */

public class Constants {
    public static final int RC_SIGN_IN = 007;
    public static boolean emailLoginCheck = false;
    public static boolean gmailLoginCheck = false;

    public static final String CONTACT_NAME = "contact_name";
    public static final int ALL_SMS_LOADER = 123;
    public static final String FROM_SMS_RECIEVER = "from_sms_receiver";
    public static final String DATE_FORMAT = new SimpleDateFormat("dd MMM").format(new Date()).toString();
    public static final String TIME_FORMAT = new SimpleDateFormat("hh:mm a").format(new Date()).toString();
    public static final int MULTIPLE_PERMISSIONS_REQUEST = 1;
    public static final String SMS_SELECTION_SEARCH = "address LIKE ? OR body LIKE ?";
    public static final String SORT_DESC = "date DESC";
    public static final Uri GENERAL_SMS_URI = Uri.parse("content://sms/");
    public static final Uri PARTICULAR_SMS_URI = Uri.parse("content://mms-sms/conversations");
    public static final int VIEW_TYPE_MESSAGE_SENT = 1;
    public static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    public static final String threadId = "thread_id";

    public static String id;

    public static String getDate(long milliSeconds) {
        String dateFormat = "dd/MM/yyyy";

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getName(String phoneNumber, Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        ContentResolver resolver = context.getContentResolver();
        Cursor cur = resolver.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cur != null && cur.moveToFirst()) {
            String value = cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            if (value != null) {
                cur.close();
                return value;
            }
        }

        cur.close();
        return phoneNumber;
    }

    private static ProgressDialog mProgressDialog;
    public static void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Loading....");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }





}
