package com.awais2075gmail.awais2075.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.adapter.ContactAdapter;
import com.awais2075gmail.awais2075.database.ContactDB;
import com.awais2075gmail.awais2075.decoration.MyDividerItemDecoration;
import com.awais2075gmail.awais2075.model.Contact;
import com.awais2075gmail.awais2075.util.Constants;
import com.awais2075gmail.awais2075.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener<Contact>{

    private List<Contact> contactList;
    private ContactAdapter contactAdapter;

    private DatabaseReference databaseReference;
    private String groupId;
    private ContactDB contactDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        init();
    }

    private void init() {
        groupId = getIntent().getStringExtra("groupId");
        databaseReference = FirebaseDatabase.getInstance().getReference("Contact").child(groupId);
        contactDB = new ContactDB(databaseReference);

        contactList = new ArrayList<>();
        contactAdapter = new ContactAdapter(contactList, this);
        findViewById(R.id.fab).setOnClickListener(this);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Contacts");
        ((RecyclerView)findViewById(R.id.recyclerView)).setLayoutManager(new LinearLayoutManager(this));
        ((RecyclerView)findViewById(R.id.recyclerView)).setItemAnimator(new DefaultItemAnimator());
        ((RecyclerView) findViewById(R.id.recyclerView)).addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        ((RecyclerView) findViewById(R.id.recyclerView)).setAdapter(contactAdapter);

        getContactList(databaseReference);
    }

    private void getContactList(final DatabaseReference dRef) {
        dRef.orderByChild("contactName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    contactList.add(snapshot.getValue(Contact.class));
                    contactAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        contactAdapter.notifyDataSetChanged();
    }

    @Override
    public void itemClicked(Contact contact, int position) {

        //databaseReference.child(contact.getContactId()).removeValue();
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("smsNumber", contact.getContactNumber());
        intent.putExtra("smsAddress", contact.getContactName());
        intent.putExtra(Constants.threadId, getThreadId(this ,"address='" + contact.getContactNumber() + "'"));
        startActivity(intent);

    }

    public String getThreadId(Context context, String phoneNumber) {
        String threadId = "null";
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(Constants.GENERAL_SMS_URI, null, phoneNumber, null, null);
        while (cursor.moveToNext()) {
            threadId = cursor.getLong(cursor.getColumnIndexOrThrow("thread_id")) + "";
            //break;
        }
        return "thread_id='" + threadId + "'".toString();
    }

    @Override
    public void itemLongClicked(Contact contact, int position) {
        contextMenu(contact.getContactId(), position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(getBaseContext(), PhoneActivity.class).putExtra("groupId", groupId));
                break;
        }
    }

    private void contextMenu(final String contactId, final int position) {
        String[] items = {"Delete", "Edit"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this
                , android.R.layout.simple_list_item_1, android.R.id.text1, items);

        new AlertDialog.Builder(this)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                dialogInterface.dismiss();
                                deleteDialog(contactId, position);
                                break;
                            case 1:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                })
                .show();

    }

    private void deleteDialog(final String contactId, final int position) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Are you sure you want to delete this message?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                contactDB.deleteContact(contactId);
                contactAdapter.notifyItemRemoved(position);
            }

        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alert.create();
        alert.show();
    }

}
