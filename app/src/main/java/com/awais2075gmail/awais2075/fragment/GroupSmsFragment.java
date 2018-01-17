package com.awais2075gmail.awais2075.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import static java.security.AccessController.getContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupSmsFragment extends BaseFragment {

    private FloatingActionMenu materialDesignFAM;
    private FloatingActionButton floatingActionButton1, floatingActionButton3;


    public GroupSmsFragment() {
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

        return view;
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_group_sms;
    }

    @Override
    protected void getAllSms(Cursor cursor) {

    }
}