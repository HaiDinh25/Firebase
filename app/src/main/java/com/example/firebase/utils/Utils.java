package com.example.firebase.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Utils {

    public static DatabaseReference databaseReference() {
        //Lấy đối tượng FirebaseDatabase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //Kết nối tới node có tên là contacts
        DatabaseReference databaseReference = firebaseDatabase.getReference("contacts");
        return databaseReference;
    }
}
