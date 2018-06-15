package com.awais2075gmail.awais2075.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.adapter.ChipsAdapter;
import com.awais2075gmail.awais2075.model.Contact;
import com.awais2075gmail.awais2075.util.Utils;
import com.robertlevonyan.views.chip.Chip;
import com.robertlevonyan.views.chip.OnChipClickListener;
import com.robertlevonyan.views.chip.OnCloseClickListener;

import java.util.ArrayList;
import java.util.List;


public class CreateSmsActivity extends AppCompatActivity implements ItemClickListener<Contact>{

    private List<Contact> contactList;
    private ChipsAdapter chipsAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sms);
        init();
    }

    private void init() {
        contactList = new ArrayList<>();
        contactList.add(new Contact("Awais", "+923419022273"));
        contactList.add(new Contact("Anas", "+923419022273"));
        contactList.add(new Contact("Ubaid", "+923419022273"));
        contactList.add(new Contact("Aatiqa", "+923419022273"));

        chipsAdapter = new ChipsAdapter(this, contactList);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chipsAdapter);
        Toast.makeText(this, contactList.get(0).getContactName()+"", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void itemClicked(Contact contact, int position) {
        Toast.makeText(this, contact.getContactName()+"", Toast.LENGTH_SHORT).show();
        contactList.remove(position);
        chipsAdapter.notifyDataSetChanged();
    }

    @Override
    public void itemLongClicked(Contact contact, int position) {

    }
}
