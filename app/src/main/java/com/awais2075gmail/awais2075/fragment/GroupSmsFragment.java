package com.awais2075gmail.awais2075.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075._interface.ItemClickListener;
import com.awais2075gmail.awais2075.activity.ContactActivity;
import com.awais2075gmail.awais2075.activity.MessageActivity;
import com.awais2075gmail.awais2075.activity.PhoneActivity;
import com.awais2075gmail.awais2075.database.GroupDB;
import com.awais2075gmail.awais2075.decoration.MyDividerItemDecoration;
import com.awais2075gmail.awais2075.model.Group;
import com.awais2075gmail.awais2075.adapter.GroupAdapter;
import com.awais2075gmail.awais2075.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupSmsFragment extends BaseFragment implements View.OnClickListener, ItemClickListener<Group> {

    private List<Group> groupList;
    private GroupAdapter groupAdapter;
    private String userId;
    private GroupDB groupDB;
    private HashSet<String> hs;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;



    public GroupSmsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_layout;
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

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Group").child(Utils.userId);
        groupDB = new GroupDB(databaseReference);

        view.findViewById(R.id.appBarLayout).setVisibility(View.GONE);

        view.findViewById(R.id.fab).setOnClickListener(this);

        groupList = new ArrayList<>();
        groupAdapter = new GroupAdapter(groupList, this);

        ((RecyclerView) view.findViewById(R.id.recyclerView)).setLayoutManager(new LinearLayoutManager(getContext()));
        ((RecyclerView) view.findViewById(R.id.recyclerView)).setItemAnimator(new DefaultItemAnimator());
        ((RecyclerView) view.findViewById(R.id.recyclerView)).addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        ((RecyclerView) view.findViewById(R.id.recyclerView)).setAdapter(groupAdapter);


        setRecyclerView(databaseReference);

    }

    private void setRecyclerView(final DatabaseReference dRef) {
        dRef.orderByChild("groupName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupList.clear();
                hs = new HashSet<>();
                Toast.makeText(getContext(), dataSnapshot.child("Contact").getChildrenCount()+" contacts", Toast.LENGTH_SHORT).show();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    /*if (snapshot.getValue(Group.class).getUserId().contains(Utils.userId)) {
                        groupList.add(snapshot.getValue(Group.class));
                        hs.add(snapshot.getValue(Group.class).getGroupName().toLowerCase());
                    }
                    groupAdapter.notifyDataSetChanged();*/
                    groupList.add(snapshot.getValue(Group.class));
                    hs.add(snapshot.getValue(Group.class).getGroupName().toLowerCase());
                    groupAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                showAlertDialogBox();
                break;
        }
    }

    private void showAlertDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.alert_dialog, null);

        final EditText text_groupName = view.findViewById(R.id.edit_groupName);
        final FancyButton button_addGroup = view.findViewById(R.id.button_addGroup);

        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        button_addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hs.contains(text_groupName.getText().toString().toLowerCase())) {

                        Toast.makeText(getContext(), "Already Exists", Toast.LENGTH_SHORT).show();
                } else {
                    String groupId = databaseReference.push().getKey();
                    Group group = new Group(groupId, text_groupName.getText().toString(), Utils.userId);
                    if (groupDB.addGroup(group)) {
                        Toast.makeText(getContext(), "Group Added Successfully", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void itemClicked(Group group, int position) {
        startActivity(new Intent(getContext(), ContactActivity.class).putExtra("groupId", group.getGroupId()));
    }

    @Override
    public void itemLongClicked(Group group, int position) {
        contextMenu(group, position);
    }

    private void contextMenu(final Group group, final int position) {
        String[] items = {"Delete", "Edit"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext()
                , android.R.layout.simple_list_item_1, android.R.id.text1, items);

        new AlertDialog.Builder(getContext())
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                dialogInterface.dismiss();
                                deleteDialog(group.getGroupId(), position);
                                break;
                            case 1:
                                Toast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT).show();
                                editGroup(group);
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                })
                .show();

    }

    private void deleteDialog(final String groupId, final int position) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage("Are you sure you want to delete this message?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                groupDB.deleteGroup(groupId);
                groupAdapter.notifyItemRemoved(position);
            }

        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alert.create();
        alert.show();
    }

    private void editGroup(Group group) {
        //showAlertDialogBox();
        group.setGroupName("test");
        databaseReference.child(group.getGroupId()).setValue(group);
    }


}