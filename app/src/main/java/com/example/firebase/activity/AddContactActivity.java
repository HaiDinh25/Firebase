package com.example.firebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebase.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContactActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtContactId;
    EditText edtName;
    EditText edtEmail;
    EditText edtPhone;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        initUI();

        btnAdd.setOnClickListener(this);
    }

    private void initUI () {
        edtContactId = findViewById(R.id.edt_contactId);
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phoneNumber);
        btnAdd = findViewById(R.id.btn_add);
    }

    private boolean checkNull() {
        if (edtContactId.getText().toString().isEmpty() || edtName.getText().toString().isEmpty()
            || edtEmail.getText().toString().isEmpty() || edtPhone.getText().toString().isEmpty())
            return false;
        return true;
    }

    private void addContacts () {
        if (checkNull()) {
            //Lấy đối tượng FirebaseDatabase
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            //Kết nối tới node có tên là contacts
            DatabaseReference databaseReference = firebaseDatabase.getReference("contacts");
            
            String id = edtContactId.getText().toString();
            String name = edtName.getText().toString();
            String email = edtEmail.getText().toString();
            String phone = edtPhone.getText().toString();

            databaseReference.child(id).child("name").setValue(name);
            databaseReference.child(id).child("email").setValue(email);
            databaseReference.child(id).child("phone").setValue(phone);
            finish();
        } else {
            Toast.makeText(this, "Chưa nhập đủ thông tin.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                addContacts();
                break;
        }
    }
}
