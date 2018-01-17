package com.awais2075gmail.awais2075.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.awais2075gmail.awais2075.MApplication;
import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075.adapter.PhoneAdapter;
import com.awais2075gmail.awais2075.model.Contact;
import com.awais2075gmail.awais2075.util.Constants;
import com.awais2075gmail.awais2075.util.Utils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PhoneActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerView_phone;
    private PhoneAdapter phoneAdapter;
    private List<Contact> phoneList;
    //private Cursor cursor;
    private FloatingActionMenu materialDesignFAM;
    private FloatingActionButton floatingActionButton1, floatingActionButton3;
    private int mCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        setRecyclerView();
        getSupportLoaderManager().initLoader(1, null, this);
        //getAllPhoneContacts();
    }



    private void setRecyclerView() {
        recyclerView_phone = findViewById(R.id.recyclerview);
        phoneList = new ArrayList<>();
        phoneAdapter = new PhoneAdapter(phoneList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView_phone.setLayoutManager(mLayoutManager);
        recyclerView_phone.setItemAnimator(new DefaultItemAnimator());
        recyclerView_phone.setAdapter(phoneAdapter);
    }

    private void getAllPhoneContacts(Cursor cursor) {
        phoneList = Utils.phoneContactsList;

        /*cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        Contact contact;
        HashSet<String> hs = new HashSet<>();
        while (cursor.moveToNext()) {
            String name = (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            String number = Utils.isValidNumer(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)), Utils.getCountry(this));
            if (!hs.contains(number)) {
                contact = new Contact(name, number, false);
                phoneList.add(contact);
                hs.add(number);
            }
        }
        cursor.close();*/

        phoneAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        /*DatabaseReference databaseReference = Utils.contactDatabaseReference;
        List<Contact> list = phoneAdapter.selectedContacts();
        String groupId = Utils.groupId;
        for (int i=0; i<phoneAdapter.selectedContacts().size(); i++) {
            String contactId = databaseReference.push().getKey();

            //Contact contact = new Contact(contactId, list.get(i).getContactName(), list.get(i).getContactNumber(), groupId);
            Contact contact = new Contact(contactId, list.get(i).getContactName(), list.get(i).getContactNumber(), groupId);
            databaseReference.child(contactId).setValue(contact);
        }*/

        //startActivity(new Intent(this, ContactActivity.class));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Constants.showProgressDialog(this);
        Uri CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC ";
        return new CursorLoader(this, CONTENT_URI, null,null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        getAllPhoneContacts(cursor);
        //phoneList = Utils.phoneContactsList;
        cursor.close();
        Constants.hideProgressDialog();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        phoneList = null;
        phoneAdapter.notifyDataSetChanged();

    }
}
