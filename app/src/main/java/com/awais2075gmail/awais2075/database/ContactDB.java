package com.awais2075gmail.awais2075.database;

import android.widget.Toast;

import com.awais2075gmail.awais2075.model.Contact;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ContactDB {
    private DatabaseReference databaseReference;

    public ContactDB(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public boolean addContact(String groupId, List<Contact> contactList) {
        for (int i=0; i<contactList.size(); i++) {
            String contactId = databaseReference.push().getKey();
            databaseReference.child(contactId).setValue(new Contact(contactId, contactList.get(i).getContactName(),
                    contactList.get(i).getContactNumber(), groupId));
        }
        return true;
    }

    public boolean deleteContact (String contactId) {
        databaseReference.child(contactId).removeValue();
        return true;
    }

    public DatabaseReference getAllContacts() {
        return databaseReference;
    }

}
