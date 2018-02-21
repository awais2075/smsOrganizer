package com.awais2075gmail.awais2075.fragment;


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

import com.awais2075gmail.awais2075.activity.CreateSmsActivity;
import com.awais2075gmail.awais2075.decoration.MyDividerItemDecoration;
import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.activity.MessageActivity;
import com.awais2075gmail.awais2075.adapter.ConversationAdapter;
import com.awais2075gmail.awais2075.model.SMS;
import com.awais2075gmail.awais2075.util.Constants;
import com.awais2075gmail.awais2075.util.Utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UnknownSmsFragment extends BaseFragment implements ItemClickListener<SMS>, View.OnClickListener{

    private List<SMS> smsList;
    private ConversationAdapter conversationAdapter;
    private View view;
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
        this.view = view;
        init();
        //getLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);
        return view;
    }

    private void init() {
        view.findViewById(R.id.appBarLayout).setVisibility(View.GONE);

        ((RecyclerView)view.findViewById(R.id.recyclerView)).setLayoutManager(new LinearLayoutManager(getContext()));
        ((RecyclerView)view.findViewById(R.id.recyclerView)).setItemAnimator(new DefaultItemAnimator());
        ((RecyclerView)view.findViewById(R.id.recyclerView)).addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));

        getLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);

        view.findViewById(R.id.fab).setOnClickListener(this);

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_layout;
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

    private void sortAndSetToRecycler(List<SMS> listSms) {
        Set<SMS> smsSet = new LinkedHashSet<>(listSms);
        smsList = new ArrayList<>(smsSet);

        setRecyclerView(smsList);
    }

    private void setRecyclerView(List<SMS> smsList) {
        conversationAdapter = new ConversationAdapter(getContext(), smsList, this);

        ((RecyclerView)view.findViewById(R.id.recyclerView)).setAdapter(conversationAdapter);
    }



    @Override
    public void itemClicked(SMS sms, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("smsNumber", sms.getSmsNumber());
        bundle.putString("smsAddress", sms.getSmsAddress());
        bundle.putString(Constants.threadId, "thread_id='" + sms.getSmsThreadId()+ "'".toString());
        startActivity(new Intent(getContext(), MessageActivity.class).putExtras(bundle));
    }

    @Override
    public void itemLongClicked(SMS sms, int position) {
        Toast.makeText(getContext(),sms.isSpam()+" check", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(getContext(), CreateSmsActivity.class));
                //Toast.makeText(getContext(), "Unknown SMS", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
