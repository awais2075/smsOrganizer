package com.awais2075gmail.awais2075.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Awais Rashi on 18-Jan-18.
 */

public class GroupDB {
    private DatabaseReference databaseReference;
    private List<Group> groupList;

    public GroupDB() {
    }

    public void init() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Group");
        groupList = new ArrayList<>();
    }

    public boolean addGroup(String groupName, String userId) {
        String groupId = databaseReference.push().getKey();
        databaseReference.child(groupId).setValue(new Group(groupId, groupName, userId));
        return true;
    }

    public boolean editGroup(String groupId, String groupName) {
        return true;
    }

    public boolean deleteGroup(String groupId) {
        DatabaseReference groupReference = FirebaseDatabase.getInstance().getReference("Group").child(groupId);

        //removing artist
        groupReference.removeValue();

        DatabaseReference contactReference = FirebaseDatabase.getInstance().getReference("Contact").child(groupId);
        contactReference.removeValue();

        return true;
    }

    public DatabaseReference getAllGroups() {
        /*try {
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getValue(Group.class).getUserId().contains(userId)) {
                            Group group = snapshot.getValue(Group.class);
                            groupList.add(group);
                            count++;
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            return 0;
        }*/
        return databaseReference;
    }
}

