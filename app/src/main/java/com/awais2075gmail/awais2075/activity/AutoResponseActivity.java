package com.awais2075gmail.awais2075.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.adapter.AutoResponseAdapter;
import com.awais2075gmail.awais2075.decoration.MyDividerItemDecoration;
import com.awais2075gmail.awais2075.model.AutoResponse;

import java.util.ArrayList;
import java.util.List;

public class AutoResponseActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener<AutoResponse> {

    private List<AutoResponse> autoResponseList = new ArrayList<>();
    private AutoResponseAdapter autoResponseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_response);

        setRecyclerView();
    }


    private void setRecyclerView() {
        autoResponseList.add(new AutoResponse("Muhammad Awais", "+92 341 9022273", "Hello i will call u later"));
        autoResponseList.add(new AutoResponse("Muhammad Ubaid", "+92 341 9022273", "Hello i am busy"));
        autoResponseList.add(new AutoResponse("Muhammad Anas", "+92 341 9022273", "Hello in a meeting"));
        autoResponseList.add(new AutoResponse("Muhammad Rashid", "+92 341 9022273", "Hello call me later"));

        autoResponseAdapter = new AutoResponseAdapter(this, autoResponseList);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Phone Contacts");
        findViewById(R.id.fab).setOnClickListener(this);


        ((RecyclerView) findViewById(R.id.recyclerView)).setLayoutManager(new LinearLayoutManager(this));
        ((RecyclerView) findViewById(R.id.recyclerView)).setItemAnimator(new DefaultItemAnimator());
        ((RecyclerView) findViewById(R.id.recyclerView)).addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        ((RecyclerView) findViewById(R.id.recyclerView)).setAdapter(autoResponseAdapter);

    }

    @Override
    public void itemClicked(AutoResponse autoResponse, int position) {

    }

    @Override
    public void itemLongClicked(AutoResponse autoResponse, int position) {

    }

    @Override
    public void onClick(View v) {

    }

}
