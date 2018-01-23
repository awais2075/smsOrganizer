

package com.awais2075gmail.awais2075.activity;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import com.awais2075gmail.awais2075.MyDividerItemDecoration;
import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.adapter.PhoneAdapter;
import com.awais2075gmail.awais2075.model.Contact;
import com.awais2075gmail.awais2075.util.Constants;
import com.awais2075gmail.awais2075.util.Utils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class PhoneActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener<Contact>{

    private RecyclerView recyclerView_phone;
    private PhoneAdapter phoneAdapter;
    private List<Contact> phoneList;
    private Toolbar toolbar;
    //private Cursor cursor;
    private FloatingActionMenu materialDesignFAM;
    private FloatingActionButton floatingActionButton1, floatingActionButton3;
    private int mCount = 0;
    private SearchView searchView;
    private FancyButton button_selectAll;
    private FancyButton button_done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        Toast.makeText(this, getIntent().getStringExtra("groupId").toString()+" is groupId", Toast.LENGTH_SHORT).show();
        setRecyclerView();
        //getSupportLoaderManager().initLoader(1, null, this);
        //getAllPhoneContacts();
    }


    private void setRecyclerView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Phone Contacts");

        button_selectAll = findViewById(R.id.button_selectAll);
        button_selectAll.setOnClickListener(this);
        button_done = findViewById(R.id.button_done);
        button_done.setEnabled(false);
        button_done.setOnClickListener(this);


        recyclerView_phone = findViewById(R.id.recyclerview);
        phoneList = new ArrayList<>(Utils.phoneContactsList);
        phoneAdapter = new PhoneAdapter(this, phoneList, this);
        whiteNotificationBar(recyclerView_phone);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView_phone.setLayoutManager(mLayoutManager);
        recyclerView_phone.setItemAnimator(new DefaultItemAnimator());
        recyclerView_phone.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView_phone.setAdapter(phoneAdapter);
    }

    private void getAllPhoneContacts(Cursor cursor) {

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        Contact contact;
        HashSet<String> hs = new HashSet<>();
        while (cursor.moveToNext()) {
            String name = (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            String number = Utils.isValidNumer(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)), Utils.getCountry(this));
            if (!hs.contains(number)) {
                contact = new Contact(name, number, false);
                phoneList.add(contact);
                hs.add(number);
            }
        }
        cursor.close();

        phoneAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_selectAll:
                phoneAdapter.selectAll();
                button_done.setEnabled(true);
                break;
            case R.id.button_done:
                break;
        }
    }


    //startActivity(new Intent(this, ContactActivity.class));

/*
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Constants.showProgressDialog(this);
        Uri CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC ";
        return new CursorLoader(this, CONTENT_URI, null, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //getAllPhoneContacts(cursor);
        phoneList = Utils.phoneContactsList;
        cursor.close();
        Constants.hideProgressDialog();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        phoneList = null;
        phoneAdapter.notifyDataSetChanged();

    }*/

    @Override
    public void itemClicked(Contact contact) {
        Toast.makeText(this, contact.getContactName() + " selected", Toast.LENGTH_SHORT).show();
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

/*

package com.awais2075gmail.awais2075.activity;

        import android.app.SearchManager;
        import android.content.Context;
        import android.database.Cursor;
        import android.graphics.Color;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.provider.ContactsContract;
        import android.support.v4.app.LoaderManager;
        import android.support.v4.content.CursorLoader;
        import android.support.v4.content.Loader;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.SearchView;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Toast;

        import com.awais2075gmail.awais2075.MyDividerItemDecoration;
        import com.awais2075gmail.awais2075.R;
        import com.awais2075gmail.awais2075.adapter.PhoneAdapter;
        import com.awais2075gmail.awais2075.model.Contact;
        import com.awais2075gmail.awais2075.util.Constants;
        import com.awais2075gmail.awais2075.util.Utils;
        import com.github.clans.fab.FloatingActionButton;
        import com.github.clans.fab.FloatingActionMenu;

        import java.util.ArrayList;
        import java.util.HashSet;
        import java.util.List;

public class PhoneActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, PhoneAdapter.PhoneAdapterListener{

    private RecyclerView recyclerView_phone;
    private PhoneAdapter phoneAdapter;
    private List<Contact> phoneList;
    private SearchView searchView;
    private Toolbar toolbar;
    //private Cursor cursor;
    private FloatingActionMenu materialDesignFAM;
    private FloatingActionButton floatingActionButton1, floatingActionButton3;
    private int mCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        setRecyclerView();
        getSupportLoaderManager().initLoader(1, null, this);
        //getAllPhoneContacts();
    }



    private void setRecyclerView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Phone Contacts");


        recyclerView_phone = findViewById(R.id.recyclerview);
        phoneList = new ArrayList<>();
        phoneAdapter = new PhoneAdapter(this, phoneList, this);
        whiteNotificationBar(recyclerView_phone);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView_phone.setLayoutManager(mLayoutManager);
        recyclerView_phone.setItemAnimator(new DefaultItemAnimator());
        recyclerView_phone.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView_phone.setAdapter(phoneAdapter);
    }

    private void getAllPhoneContacts(Cursor cursor) {

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        Contact contact;
        HashSet<String> hs = new HashSet<>();
        while (cursor.moveToNext()) {
            String name = (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            String number = Utils.isValidNumer(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)), Utils.getCountry(this));
            if (!hs.contains(number)) {
                contact = new Contact(name, number, false);
                phoneList.add(contact);
                hs.add(number);
            }
        }
        cursor.close();

        phoneAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        */
/*DatabaseReference databaseReference = Utils.contactDatabaseReference;
        List<Contact> list = phoneAdapter.selectedContacts();
        String groupId = Utils.groupId;
        for (int i=0; i<phoneAdapter.selectedContacts().size(); i++) {
            String contactId = databaseReference.push().getKey();

            //Contact contact = new Contact(contactId, list.get(i).getContactName(), list.get(i).getContactNumber(), groupId);
            Contact contact = new Contact(contactId, list.get(i).getContactName(), list.get(i).getContactNumber(), groupId);
            databaseReference.child(contactId).setValue(contact);
        }*//*


        //startActivity(new Intent(this, ContactActivity.class));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Constants.showProgressDialog(this);
        Uri CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC ";
        return new CursorLoader(this, CONTENT_URI, null,null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        getAllPhoneContacts(cursor);
        //phoneList = Utils.phoneContactsList;
        cursor.close();
        Constants.hideProgressDialog();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        phoneList = null;
        phoneAdapter.notifyDataSetChanged();

    }

    @Override
    public void onContactSelected(Contact contact) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

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


}*/
