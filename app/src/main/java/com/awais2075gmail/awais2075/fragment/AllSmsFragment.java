package com.awais2075gmail.awais2075.fragment;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075.activity.CreateSmsActivity;
import com.awais2075gmail.awais2075.activity.MessageActivity;
import com.awais2075gmail.awais2075.activity.PhoneActivity;
import com.awais2075gmail.awais2075.adapter.ConversationAdapter;
import com.awais2075gmail.awais2075.classification.Spam;
import com.awais2075gmail.awais2075.decoration.MyDividerItemDecoration;
import com.awais2075gmail.awais2075.model.SMS;
import com.awais2075gmail.awais2075.util.Constants;
import com.awais2075gmail.awais2075.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AllSmsFragment extends BaseFragment implements View.OnClickListener, ItemClickListener<SMS> {

    private int smsCount;
    private List<SMS> smsList;
    private View view;
    private ConversationAdapter conversationAdapter;
    private RecyclerView.LayoutManager mlinearLayoutManager;

    public AllSmsFragment() {
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
        this.view = view;
        //Toast.makeText(getContext(), "All Sms Fragment", Toast.LENGTH_SHORT).show();
        init();
        //getLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);
        return view;
    }

    private void init() {
        view.findViewById(R.id.appBarLayout).setVisibility(View.GONE);

        smsList = new ArrayList<>();
        conversationAdapter = new ConversationAdapter(getContext(), smsList, this);
        ((RecyclerView) view.findViewById(R.id.recyclerView)).setLayoutManager(new LinearLayoutManager(getContext()));
        ((RecyclerView) view.findViewById(R.id.recyclerView)).setItemAnimator(new DefaultItemAnimator());
        ((RecyclerView) view.findViewById(R.id.recyclerView)).addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        ((RecyclerView) view.findViewById(R.id.recyclerView)).setAdapter(conversationAdapter);


        getLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);


        view.findViewById(R.id.fab).setOnClickListener(this);


    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_layout;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Constants.showProgressDialog(getContext());
        return super.onCreateLoader(id, args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Constants.hideProgressDialog();
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
        Spam spam = new Spam(getContext());
        List<SMS> unknownSmsList = new ArrayList<>();
        List<SMS> listSms = new ArrayList<>();
        HashSet<Long> hashSet = new HashSet<>();
        SMS sms = null;

        smsCount = cursor.getCount();
        //Toast.makeText(getContext(), "SMS Count is " + smsCount, Toast.LENGTH_SHORT).show();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < smsCount; i++) {
                boolean knownCheck = false;
                boolean unknownCheck = false;
                boolean isSpam = false;
                long smsThreadId;
                try {

                    smsThreadId = cursor.getLong(cursor.getColumnIndexOrThrow("thread_id"));


                    if (!hashSet.contains(smsThreadId)) {
                        long smsId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                        //long smsThreadId = cursor.getLong(cursor.getColumnIndexOrThrow("thread_id"));
                        String smsNumber = Utils.isValidNumer(cursor.getString(cursor.getColumnIndexOrThrow("address")), Utils.getCountry(getContext()));
                        String smsAddress = Utils.getName(smsNumber);
                        String smsBody = cursor.getString(cursor.getColumnIndexOrThrow("body")).trim();
                        if (smsNumber != smsAddress) {
                            knownCheck = true;
                        } else if (smsNumber == smsAddress) {
                            unknownCheck = true;
                            isSpam = spam.getValue(smsBody);
                        }
                        String smsReadState = cursor.getString(cursor.getColumnIndexOrThrow("read"));
                        String smsDate = Constants.getDate(cursor.getLong(cursor.getColumnIndexOrThrow("date")), false);
                        String smsType;

                        hashSet.add(smsThreadId);

                        smsType = Utils.getSmsType(cursor.getString(cursor.getColumnIndexOrThrow("type")));


                        sms = new SMS(smsId, smsThreadId, smsNumber, smsAddress, smsBody, smsReadState, smsDate, smsType, isSpam);


                    } else {
                        continue;
                    }


                } catch (Exception e) {
                    Toast.makeText(getContext(), "Exception getAllSMS", Toast.LENGTH_SHORT).show();
                } finally {
                    if (knownCheck) {
                        listSms.add(sms);
                        //conversationAdapter.notifyDataSetChanged();
                    } else if (unknownCheck) {
                        unknownSmsList.add(sms);
                        //conversationAdapter.notifyDataSetChanged();
                    }
                    cursor.moveToNext();
                }
            }
        }
        cursor.close();

        //smsList = listSms;


        sortAndSetToRecycler(listSms);
        Utils.unknownSmsList = unknownSmsList;
    }


    private void sortAndSetToRecycler(List<SMS> listSms) {
        Set<SMS> smsSet = new LinkedHashSet<>(listSms);
        smsList = new ArrayList<>(smsSet);

        setRecyclerView(smsList);
    }

    private void setRecyclerView(List<SMS> smsList) {
        conversationAdapter = new ConversationAdapter(getContext(), smsList, this);
        ((RecyclerView) view.findViewById(R.id.recyclerView)).setAdapter(conversationAdapter);

    }


    @Override
    public void itemClicked(SMS sms, int position) {
        Intent intent = new Intent(getContext(), MessageActivity.class);
        intent.putExtra("smsNumber", sms.getSmsNumber());
        intent.putExtra("smsAddress", sms.getSmsAddress());
        intent.putExtra(Constants.threadId, "thread_id='" + sms.getSmsThreadId() + "'".toString());
        startActivity(intent);

    }

    @Override
    public void itemLongClicked(SMS sms, int position) {
        Toast.makeText(getContext(),sms.isSpam()+" check", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                //startActivity(new Intent(getContext(), CreateSmsActivity.class));
                Spam spam = new Spam(getContext());
                Toast.makeText(getContext(), spam.getValue("Dear OLX User Your Ad Top Quality  published prohibited items content Check Posting Rules httpswwwolxcompkqr2vhbcc8") + "", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}