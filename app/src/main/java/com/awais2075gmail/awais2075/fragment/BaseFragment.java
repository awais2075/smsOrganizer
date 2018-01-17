package com.awais2075gmail.awais2075.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.*;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.awais2075gmail.awais2075.activity.ConversationActivity;
import com.awais2075gmail.awais2075.adapter.ConversationAdapter;
import com.awais2075gmail.awais2075.model.SMS;
import com.awais2075gmail.awais2075.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Muhammad Awais Rashi on 21-Dec-17.
 */

public abstract class BaseFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private String cursorFilter;
    //private ConversationAdapter conversationAdapter;
    private int count = 0;
    private List<SMS> unknownSmsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        return view;
    }

    protected abstract int getFragmentLayout();

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Constants.showProgressDialog(getContext());
        String selection = null;
        String[] selectedArgs = null;

        if (cursorFilter != null) {
            selection = Constants.SMS_SELECTION_SEARCH;
            selectedArgs = new String[]{"%", cursorFilter, "%", "%" + cursorFilter + "%"};
        }
        Toast.makeText(getContext(), "Cursor Loader", Toast.LENGTH_SHORT).show();
        return new CursorLoader(getContext(), Constants.GENERAL_SMS_URI, null, selection, selectedArgs, Constants.SORT_DESC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //pg_gcLoader.setVisibility(View.GONE);
        //Constants.hideProgressDialog();
        if (cursor != null && cursor.getCount() > 0) {
            getAllSms(cursor);
            cursor.close();
        } else {
            Toast.makeText(getContext(), "No SMS", Toast.LENGTH_SHORT).show();
        }
    }

    protected abstract void getAllSms(Cursor cursor);


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //smsList = null;
        //conversationAdapter.notifyDataSetChanged();
    }

    protected void setUnknownSmsList(List<SMS> unknownSmsList) {
        this.unknownSmsList = unknownSmsList;
    }

    protected List<SMS> getUnknownSmsList() {
        return unknownSmsList;
    }

}