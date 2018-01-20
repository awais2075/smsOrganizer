package com.awais2075gmail.awais2075.firebase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;
    private List<Group> groupList;
    private GroupDB groupDB;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        init();

    }

    private void init() {
        userId = Utils.userId;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerview);

        groupList = new ArrayList<>();
        groupDB = new GroupDB();

        groupAdapter = new GroupAdapter(this, groupList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(groupAdapter);

        groupDB.init();
        getAllGroups();

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox();
            }
        });

    }

    private void showDialogBox() {
        final EditText edit_groupName;
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_box_add_group, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        edit_groupName = mView.findViewById(R.id.edit_groupName);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String groupName = edit_groupName.getText().toString();
                        /*if (TextUtils.isEmpty(groupName) || hs.contains(groupName.toLowerCase())) {
                            edit_groupName.setError("Enter again");
                            edit_groupName.requestFocus();

                            return;
                        } else {
                            addGroup(groupName);
                        }*/
                        addGroup(groupName);
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();
    }

    private void addGroup(String groupName) {
        /*//String groupName = edit_groupName.getText().toString().trim();


       // if (!TextUtils.isEmpty(groupName) && !hs.contains(groupName.toLowerCase())) {
            String groupId = mDatabaseReference.push().getKey();


            mDatabaseReference.child(groupId).setValue(new Group(groupId, groupName, userId));

            edit_groupName.setText("");
            //groupAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Group Added Successfully", Toast.LENGTH_SHORT).show();

        //} else {
            edit_groupName.setError("Error");
        //}*/
        if (groupDB.addGroup(groupName, userId)) {
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Not added", Toast.LENGTH_SHORT).show();
        }
    }


    private void getAllGroups() {

        //Toast.makeText(this, "Groups are "+groupsDB.getAllGroups(Utils.userId)+"", Toast.LENGTH_SHORT).show();
        DatabaseReference mDatabaseReference = groupDB.getAllGroups();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //groupList.clear();
                Toast.makeText(GroupActivity.this, dataSnapshot.getChildrenCount() + " contacts", Toast.LENGTH_SHORT).show();
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue(Group.class).getUserId().contains(userId)) {
                        count++;
                        Group group = snapshot.getValue(Group.class);
                        groupList.add(group);
                        groupAdapter.notifyDataSetChanged();
                        //hs.add(group.getGroupName().toLowerCase());
                    }
//                    Toast.makeText(GroupActivity.this, group.getGroupName(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(GroupActivity.this, count + " items", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}