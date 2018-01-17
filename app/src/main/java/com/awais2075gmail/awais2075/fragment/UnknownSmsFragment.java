package com.awais2075gmail.awais2075.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.activity.MessageActivity;
import com.awais2075gmail.awais2075.adapter.ConversationAdapter;
import com.awais2075gmail.awais2075.model.SMS;
import com.awais2075gmail.awais2075.util.Constants;
import com.awais2075gmail.awais2075.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UnknownSmsFragment extends BaseFragment implements ItemClickListener{

    private int smsCount;
    private List<SMS> smsList;
    private ConversationAdapter conversationAdapter;
    private RecyclerView recyclerview;
    private LinearLayoutManager mlinearLayoutManager;

    public UnknownSmsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getFragmentLayout(), container, false);
        //Toast.makeText(getContext(), "All Sms Fragment", Toast.LENGTH_SHORT).show();
        init(view);
        //getLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);
        return view;
    }

    private void init(View view) {
        //view.setSupportActionBar(toolBar_allSms);
        recyclerview = view.findViewById(R.id.recyclerview);
        mlinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(mlinearLayoutManager);
        getLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);
        view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getContext(), "in all sms", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_all_sm;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return super.onCreateLoader(id, args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        super.onLoadFinished(loader, cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        super.onLoaderReset(loader);
        smsList = null;
        conversationAdapter.notifyDataSetChanged();
    }

    @Override
    protected void getAllSms(Cursor cursor) {
        //sortAndSetToRecycler(getUnknownSmsList());
        sortAndSetToRecycler(Utils.unknownSmsList);
        cursor.close();
    }

    /*@Override
    protected void getAllSms(Cursor cursor) {
        super.getAllSms(cursor);
        List<SMS> listSms = new ArrayList<SMS>();
        HashSet<Long> hashSet = new HashSet<>();
        SMS sms = null;

        smsCount = cursor.getCount();
        //Toast.makeText(getContext(), "SMS Count is " + smsCount, Toast.LENGTH_SHORT).show();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < smsCount; i++) {
                boolean check = false;
                long smsThreadId;
                try {

                    smsThreadId = cursor.getLong(cursor.getColumnIndexOrThrow("thread_id"));


                    if (!hashSet.contains(smsThreadId))  {
                        long smsId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                        //long smsThreadId = cursor.getLong(cursor.getColumnIndexOrThrow("thread_id"));
                        String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                        String smsAddress = Constants.getName(address, getContext());
                        if (address == smsAddress) {
                            check = true;
                        }
                        String smsBody = cursor.getString(cursor.getColumnIndexOrThrow("body")).trim();
                        String smsReadState = cursor.getString(cursor.getColumnIndexOrThrow("read"));
                        String smsDate = Constants.getDate(cursor.getLong(cursor.getColumnIndexOrThrow("date")));
                        String smsType;

                        hashSet.add(smsThreadId);

                        if (cursor.getString(cursor.getColumnIndexOrThrow("type")).contains("1")) {

                            smsType = "INBOX";
                        } else {
                            smsType = "SENT";
                        }
                        sms = new SMS(smsId, smsThreadId, smsAddress, smsBody, smsReadState, smsDate, smsType);


                    } else {
                        continue;
                    }


                } catch (Exception e) {
                    Toast.makeText(getContext(), "Exception getAllSMS", Toast.LENGTH_SHORT).show();
                } finally {
                    if (check) {
                        listSms.add(sms);
                    }
                    cursor.moveToNext();
                }
            }
        }
        cursor.close();

        smsList = listSms;

        sortAndSetToRecycler(listSms);
        //Toast.makeText(getContext(), smsList.get(0).getSmsBody()+"", Toast.LENGTH_SHORT).show();
    }*/


    private void sortAndSetToRecycler(List<SMS> listSms) {
        Set<SMS> smsSet = new LinkedHashSet<>(listSms);
        smsList = new ArrayList<>(smsSet);

        setRecyclerView(smsList);
    }

    private void setRecyclerView(List<SMS> smsList) {
        conversationAdapter = new ConversationAdapter(getContext(), smsList);
        conversationAdapter.setItemClickListener(this);
        //item click

        recyclerview.setAdapter(conversationAdapter);
    }

    @Override
    public void itemClicked(long smsId, long smsThreadId, String smsAddress, String smsBody, String smsReadState, String smsDate, String smsType) {
        Toast.makeText(getContext(), smsType+"", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), MessageActivity.class).putExtra(Constants.threadId, "thread_id='" + smsThreadId + "'".toString()));
    }
}
