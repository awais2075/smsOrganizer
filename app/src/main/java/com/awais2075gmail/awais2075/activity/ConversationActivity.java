package com.awais2075gmail.awais2075.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075.adapter.MessageAdapter;
import com.awais2075gmail.awais2075.fragment.AllSmsFragment;
import com.awais2075gmail.awais2075.fragment.GroupSmsFragment;
import com.awais2075gmail.awais2075.fragment.UnknownSmsFragment;
import com.awais2075gmail.awais2075.util.Constants;
import com.awais2075gmail.awais2075.util.Utils;
import com.google.android.gms.common.ConnectionResult;

import java.util.ArrayList;
import java.util.List;

public class ConversationActivity extends BaseActivity{

    private Toolbar toolbar;
    private TabLayout tabLayout_conversation;
    private ViewPager viewPager_conversation;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_conversation);
        //   checkPermissions();
        if (checkDefaultSettings()) {
            checkPermissions();
        }
        init();
        emailInit();
        gmailInit();

        //Toast.makeText(this, Utils.userId+" is userId in Conversation Activity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int setView() {
        return R.layout.activity_conversation;
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager_conversation = findViewById(R.id.viewPager_conversation);
        viewPager_conversation.setOffscreenPageLimit(2);
        setViewPager (viewPager_conversation);

        tabLayout_conversation = findViewById(R.id.tabLayout_conversation);
        tabLayout_conversation.setupWithViewPager(viewPager_conversation);
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllSmsFragment(), "All");
       // adapter.addFragment(new GroupSmsFragment(), "Groups");
        adapter.addFragment(new UnknownSmsFragment(), "Unknown");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (CharSequence) mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

    /*Runtime Permissions*/
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //Toast.makeText(this, "Lower Version Permission Granted", Toast.LENGTH_SHORT).show();
            //getSupportLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);
        } else if (requestPermissions()) {
            //Toast.makeText(this, "Upper Version Permission Granted", Toast.LENGTH_SHORT).show();
            //getSupportLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);
        }

    }

    private boolean requestPermissions() {
        int permissionReadContacts = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int permissionReadSms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int permissionReceiveSms = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int permissionSendSms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        //int permissionWriteSms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionReadContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (permissionReadSms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionReceiveSms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (permissionSendSms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), Constants.MULTIPLE_PERMISSIONS_REQUEST);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.MULTIPLE_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(this, "Permitted On Request Method", Toast.LENGTH_SHORT).show();
                    //getSupportLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);
                } else {
                    //You did not accept the request can not use the functionality.
                    Toast.makeText(this, "Not Permitted", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    private boolean checkDefaultSettings() {
        boolean isDefault = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!Telephony.Sms.getDefaultSmsPackage(this).equals(getPackageName())) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ConversationActivity.this);
                builder.setMessage("Not a Default SMS App, \n Want to set it as Default! ???")
                        .setCancelable(false).
                        setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                checkPermissions();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @TargetApi(19)
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
                                startActivity(intent);
                                checkPermissions();
                            }
                        });
                builder.show();
                isDefault = false;
            } else {
                isDefault = true;
            }
        }
        return isDefault;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        isGoogleSignedIn();
        isEmailSignedIn();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                boolean new_sms = intent.getBooleanExtra("new_sms", false);

                if (new_sms) {
                    Toast.makeText(context, "New Sms", Toast.LENGTH_SHORT).show();
                    new MessageAdapter().notifyDataSetChanged();
                }
                //getSupportLoaderManager().restartLoader(Constants.ALL_SMS_LOADER, null, ConversationActivity.this);
            }
        };

        this.registerReceiver(broadcastReceiver, intentFilter);
    }

}
