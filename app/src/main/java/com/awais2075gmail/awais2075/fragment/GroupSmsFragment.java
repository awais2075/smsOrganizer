package com.awais2075gmail.awais2075.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.activity.PhoneActivity;
import com.awais2075gmail.awais2075.model.Group;
import com.awais2075gmail.awais2075.adapter.GroupAdapter;
import com.awais2075gmail.awais2075.database.GroupDB;
import com.awais2075gmail.awais2075.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupSmsFragment extends BaseFragment implements ItemClickListener<Group>{

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;
    private List<Group> groupList;
    private GroupDB groupDB;
    private String userId;


    public GroupSmsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = Utils.userId;
    }



    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_group_sms;
    }

    @Override
    protected void getAllSms(Cursor cursor) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(getFragmentLayout(), container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        Toast.makeText(getContext(), "User ID :"+userId, Toast.LENGTH_SHORT).show();
        toolbar = view.findViewById(R.id.toolbar);
        //view.setSupportActionBar(toolbar);
        recyclerView = view.findViewById(R.id.recyclerview);

        groupList = new ArrayList<>();
        groupDB = new GroupDB();

        groupAdapter = new GroupAdapter(getContext(), groupList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(groupAdapter);

        groupDB.init();
        getAllGroups();

        view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox();
            }
        });

    }

    private void showDialogBox() {
        final EditText edit_groupName;
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_box_add_group, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
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
            Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Not added", Toast.LENGTH_SHORT).show();
        }
    }


    private void getAllGroups() {

        //Toast.makeText(this, "Groups are "+groupsDB.getAllGroups(Utils.userId)+"", Toast.LENGTH_SHORT).show();
        DatabaseReference mDatabaseReference = groupDB.getAllGroups();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupList.clear();
                //Toast.makeText(getContext(), dataSnapshot.getChildrenCount() + " contacts", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(getContext(), count + " items", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void itemClicked(Group group) {
        //Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
        //groupDB.deleteGroup(group.getGroupId());
        //groupList.remove(group);
        //groupAdapter.notifyDataSetChanged();
        startActivity(new Intent(getContext(), PhoneActivity.class).putExtra("groupId", group.getGroupId()));
    }
}