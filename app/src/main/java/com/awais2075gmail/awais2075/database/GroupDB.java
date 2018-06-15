package com.awais2075gmail.awais2075.database;

import com.awais2075gmail.awais2075.model.Group;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Muhammad Awais Rashid on 25-Jan-18.
 */

public class GroupDB {
    private DatabaseReference databaseReference;

    public GroupDB(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }


    public boolean addGroup(Group group) {
        databaseReference.child(group.getGroupId()).setValue(group);
        return true;
    }

    public void editGroup (String groudId, String newGroupName) {
    }

    public void deleteGroup (String groupId) {
        databaseReference.child(groupId).removeValue();

        //getting the tracks reference for the specified artist
        FirebaseDatabase.getInstance().getReference("Contact").child(groupId).removeValue();


    }

    public DatabaseReference getAllGroups () {
        return databaseReference;
    }
}
