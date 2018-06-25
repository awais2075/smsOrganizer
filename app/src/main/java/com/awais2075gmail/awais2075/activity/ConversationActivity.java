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
import android.provider.ContactsContract;
import android.provider.Settings;
import android.net.Uri;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.awais2075gmail.awais2075.permission.Permission;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075.adapter.MessageAdapter;
import com.awais2075gmail.awais2075.fragment.AllSmsFragment;
import com.awais2075gmail.awais2075.fragment.GroupSmsFragment;
import com.awais2075gmail.awais2075.fragment.UnknownSmsFragment;
import com.awais2075gmail.awais2075.model.Contact;
import com.awais2075gmail.awais2075.util.Utils;
import com.google.android.gms.common.ConnectionResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConversationActivity extends BaseActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout_conversation;
    private ViewPager viewPager_conversation;
    private BroadcastReceiver broadcastReceiver;
    private Permission permission;
    private final String[] MULTIPLE_PERMISSIONS = {Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS};
    private final int MULTIPLE_PERMISSION_REQUEST_CODE = 101;
    private boolean isDefaultApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permission = new Permission(this, MULTIPLE_PERMISSION_REQUEST_CODE, MULTIPLE_PERMISSIONS);
        if (permission.checkPermissions()) {
            //checkDefaultAppSettings();
            getPhoneContacts();
            init();
        } else {
            permission.requestPermissions();
        }
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
        setViewPager(viewPager_conversation);

        tabLayout_conversation = findViewById(R.id.tabLayout_conversation);
        tabLayout_conversation.setupWithViewPager(viewPager_conversation);
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllSmsFragment(), "All");
        adapter.addFragment(new GroupSmsFragment(), "Groups");
        adapter.addFragment(new UnknownSmsFragment(), "Unknown");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permission.checkResults(requestCode, grantResults)) {
            Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show();
            //checkDefaultSettings();
            //checkDefaultAppSettings();
            getPhoneContacts();
            init();
        } else {
            Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Application Permissions");

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to go to the Settings for Permissions???");

        // Setting Icon to Dialog

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                showAppSettings();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
                finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void showAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!permission.checkPermissions()) {
            permission.requestPermissions();

        }
    }

    private boolean checkDefaultAppSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !Telephony.Sms.getDefaultSmsPackage(this).equals(getPackageName())) {
            return showDefaultSmsAppDialog();
        } else {
            return false;
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
                                //checkPermissions();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @TargetApi(19)
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
                                startActivity(intent);
                                //checkPermissions();
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
    public void onBackPressed() {
        moveTaskToBack(true);
    }

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

    private void getPhoneContacts() {
        List<Contact> phoneList = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        Contact contact;
        HashMap<String, String> hm = new HashMap<>();
        while (cursor.moveToNext()) {
            String name = (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            String number = Utils.isValidNumer(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)), Utils.getCountry(this));
            if (!hm.containsKey(number)) {
                contact = new Contact(name, number, false);
                Utils.phoneContactsList.add(contact);
                hm.put(number, name);
            }
        }
        Utils.phoneContactsMap = hm;
    }

    public boolean showDefaultSmsAppDialog() {
        materialDialog = new MaterialDialog.Builder(this).tag(getLocalClassName()).backgroundColor(getResources().getColor(R.color.white)).contentColor(getResources().getColor(R.color.colorPrimary))
                .title(R.string.app_name).titleColor(getResources().getColor(R.color.colorPrimary))
                .content(R.string.content, true)
                .positiveText(R.string.agree)
                .positiveColor(getResources().getColor(R.color.colorPrimary)).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        isDefaultApp = true;
                        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
                        startActivity(intent);
                    }
                })
                .negativeText(R.string.disagree)
                .negativeColor(getResources().getColor(R.color.colorPrimary)).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        isDefaultApp = false;
                    }
                })
                .cancelable(false)
                .show();
        return isDefaultApp;
    }

}
