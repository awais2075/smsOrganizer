

package com.awais2075gmail.awais2075.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.awais2075gmail.awais2075.database.ContactDB;
import com.awais2075gmail.awais2075.database.GroupDB;
import com.awais2075gmail.awais2075.decoration.MyDividerItemDecoration;
import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.adapter.PhoneAdapter;
import com.awais2075gmail.awais2075.model.Contact;
import com.awais2075gmail.awais2075.model.Group;
import com.awais2075gmail.awais2075.util.Utils;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class PhoneActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener<Contact> {

    private PhoneAdapter phoneAdapter;
    private List<Contact> phoneList = new ArrayList<>(Utils.phoneContactsList);
    private SearchView searchView;
    private FancyButton button_selectAll;
    private FancyButton button_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        setRecyclerView();
    }


    private void setRecyclerView() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Phone Contacts");
        findViewById(R.id.fab).setVisibility(View.GONE);


        button_selectAll = findViewById(R.id.button_selectAll);
        button_selectAll.setOnClickListener(this);
        button_done = findViewById(R.id.button_done);
        button_done.setOnClickListener(this);

        phoneAdapter = new PhoneAdapter(this, phoneList, this);
        whiteNotificationBar(findViewById(R.id.recyclerView));

        ((RecyclerView) findViewById(R.id.recyclerView)).setLayoutManager(new LinearLayoutManager(this));
        ((RecyclerView) findViewById(R.id.recyclerView)).setItemAnimator(new DefaultItemAnimator());
        ((RecyclerView) findViewById(R.id.recyclerView)).addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        ((RecyclerView) findViewById(R.id.recyclerView)).setAdapter(phoneAdapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_selectAll:
                Toast.makeText(this, Utils.phoneContactsList.get(0).getContactNumber()+"", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_done:
                String groupId = getIntent().getStringExtra("groupId");
                ContactDB contactDB = new ContactDB(FirebaseDatabase.getInstance().getReference("Contact").child(groupId));
                contactDB.addContact(groupId, phoneAdapter.selectedContacts());
                phoneAdapter.selectedContacts().clear();
                phoneList.clear();
                finish();
                break;

        }
    }

    @Override
    public void itemClicked(Contact contact, int position) {
        Toast.makeText(this, contact.getContactName() + " selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemLongClicked(Contact contact, int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                phoneAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                phoneAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

}