package com.awais2075gmail.awais2075.application;

import android.app.Application;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.awais2075gmail.awais2075.model.Contact;
import com.awais2075gmail.awais2075.util.Utils;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Muhammad Awais Rashi on 18-Jan-18.
 */

public class MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //getPhoneContacts();
    }

    /*private void getPhoneContacts() {
        List<Contact> phoneList = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        Contact contact;
        HashMap<String, String> hm = new HashMap<>();
        while (cursor.moveToNext()) {
            String name = (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            String number = Utils.isValidNumer(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)), Utils.getCountry(this));
            if (!hm.containsKey(number)) {
                contact = new Contact(name, number, false);
                Utils.phoneContactsList.add(contact);
                hm.put(number, name);
            }
        }
        //Utils.phoneContactsList = phoneList;
        Utils.phoneContactsMap = hm;
    }*/



}
