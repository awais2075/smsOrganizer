package com.awais2075gmail.awais2075.database;

import com.awais2075gmail.awais2075.model.Group;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Awais Rashid on 18-Jan-18.
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

        /*DatabaseReference contactReference = FirebaseDatabase.getInstance().getReference("Contact").child(groupId);
        contactReference.removeValue();
*/
        return true;
    }

    public DatabaseReference getAllGroups() {
        return databaseReference;
    }
}

