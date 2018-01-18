package com.awais2075gmail.awais2075.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075.adapter.MessageAdapter;
import com.awais2075gmail.awais2075.model.SMS;
import com.awais2075gmail.awais2075.receiver.DeliverReceiver;
import com.awais2075gmail.awais2075.receiver.SentReceiver;
import com.awais2075gmail.awais2075.receiver.SmsReceiver;
import com.awais2075gmail.awais2075.util.Constants;
import com.awais2075gmail.awais2075.util.Utils;
import com.google.android.gms.common.ConnectionResult;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MessageActivity extends BaseActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<SMS> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private String cursorFilter;
    private String messageId;
    private String messageAddress;
    private EditText edit_textMessage;
    private Button button_sendMessage;
    private static final int SMS_DRAFT = 2;
    private static final String TAG = "msg";
    private static final Uri URI_SENT = Uri.parse("content://sms/sent");
    private static final String MESSAGE_SENT_ACTION = "com.android.mms.transaction.MESSAGE_SENT";



    /**
     * Projection for getting the id.
     */
    private static final String[] PROJECTION_ID = new String[]{BaseColumns._ID};

    /**
     * SMS DB: address.
     */
    private static final String ADDRESS = "address";

    /**
     * SMS DB: read.
     */
    private static final String READ = "read";

    /**
     * SMS DB: type.
     */
    public static final String TYPE = "type";

    /**
     * SMS DB: body.
     */
    private static final String BODY = "body";

    /**
     * SMS DB: date.
     */
    private static final String DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_message);
        init();


    }

    @Override
    protected int setView() {
        return R.layout.activity_message;
    }

    private void init() {
        messageId = getIntent().getStringExtra(Constants.threadId).toString();
        messageAddress = getIntent().getStringExtra("messageAddress");
        recyclerView =  findViewById(R.id.recycler_view);
        edit_textMessage = findViewById(R.id.edit_textMessage);
        button_sendMessage = findViewById(R.id.button_sendMessage);
        button_sendMessage.setOnClickListener(this);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);


        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);

/*        messageAdapter = new MessageAdapter(this, messageList);
        recyclerView.setAdapter(new MessageAdapter());*/

        getSupportLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);

    }

    @Override
    public void onClick(View v) {
        //Toast.makeText(this, "Clicked in Message Activity", Toast.LENGTH_SHORT).show();
        switch (v.getId()) {
            case R.id.button_sendMessage:
                sendMessage();
                break;
        }

    }

    private void sendMessage() {
        send("03419022273",edit_textMessage.getText().toString());
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = messageId;
        String[] selectedArgs = null;

        if (cursorFilter != null) {
            selection = Constants.SMS_SELECTION_SEARCH;
            selectedArgs = new String[]{"%", cursorFilter, "%", "%" + cursorFilter + "%"};
        }
        Toast.makeText(this, "Cursor Loader", Toast.LENGTH_SHORT).show();
        return new CursorLoader(this, Constants.GENERAL_SMS_URI, null, selection, selectedArgs, Constants.SORT_DESC);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            getAllSms(cursor);
        } else {
            Toast.makeText(this, "No SMS", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        messageList = null;
        messageAdapter.notifyDataSetChanged();
    }

    private void getAllSms(Cursor cursor) {
        List<SMS> listSms = new ArrayList<SMS>();
        SMS sms = null;
        int smsCount = cursor.getCount();
        Toast.makeText(this, "SMS Count is " + smsCount, Toast.LENGTH_SHORT).show();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < smsCount; i++) {
                try {

                    long smsThreadId = cursor.getLong(cursor.getColumnIndexOrThrow("thread_id"));

                    long smsId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                    //long smsThreadId = cursor.getLong(cursor.getColumnIndexOrThrow("thread_id"));
                    String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    String smsAddress = address;//Constants.getName(address, this);

                    String smsBody = cursor.getString(cursor.getColumnIndexOrThrow("body")).trim();
                    String smsReadState = cursor.getString(cursor.getColumnIndexOrThrow("read"));
                    String smsDate = Constants.getDate(cursor.getLong(cursor.getColumnIndexOrThrow("date")));
                    String smsType = Utils.getSmsType(cursor.getString(cursor.getColumnIndexOrThrow("type")));



                    sms = new SMS(smsId, smsThreadId, smsAddress, smsBody, smsReadState, smsDate, smsType);



                } catch (Exception e) {
                    Toast.makeText(this, "Exception getAllSMS", Toast.LENGTH_SHORT).show();
                } finally {
                    listSms.add(sms);
                    messageAdapter.notifyDataSetChanged();
                    cursor.moveToNext();
                    //messageAdapter.notifyDataSetChanged();
                }
            }
        }
        cursor.close();

        messageList = listSms;
/*
        messageAdapter = new MessageAdapter(this, messageList);
        messageAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(messageAdapter);
        Toast.makeText(this, messageAdapter.getItemCount()+" "+messageList.size(), Toast.LENGTH_SHORT).show();
*/


        sortAndSetToRecycler(listSms);
    }

    private void sortAndSetToRecycler(List<SMS> listSms) {
        Set<SMS> smsSet = new LinkedHashSet<>(listSms);
        messageList = new ArrayList<>(smsSet);

        setRecyclerView(messageList);
    }

    private void setRecyclerView(List<SMS> messageList) {

        messageAdapter = new MessageAdapter(this, messageList);
        recyclerView.setAdapter(messageAdapter);

        Toast.makeText(this, messageAdapter.getItemCount()+" "+messageList.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void sendSMSNow(Context context, String smsBody, String smsAddress) {

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
            sms.sendTextMessage("03419022273", null, smsBody, sentPI, deliveredPI);
        }catch (Exception e){
            Toast.makeText(context,("Sms Not sent"),Toast.LENGTH_SHORT).show();
        }
    }

    private void send(final String recipient, final String message) {
        Log.d(TAG, "text: "+recipient);

        // save draft
        final ContentResolver cr = getContentResolver();
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

                final Intent sent = new Intent(MESSAGE_SENT_ACTION, draft, this, SentReceiver.class);
                sentIntents.add(PendingIntent.getBroadcast(this, 0, sent, 0));
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

}