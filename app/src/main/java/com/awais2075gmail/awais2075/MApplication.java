package com.awais2075gmail.awais2075;

import android.app.Application;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.Toast;

import com.awais2075gmail.awais2075.model.Contact;
import com.awais2075gmail.awais2075.util.Constants;
import com.awais2075gmail.awais2075.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Muhammad Awais Rashi on 02-Jan-18.
 */

public class MApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(this, "Application Class", Toast.LENGTH_SHORT).show();
        //getPhoneContacts();
    }


}
