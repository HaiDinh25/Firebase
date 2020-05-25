package com.example.firebase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase.R;
import com.example.firebase.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtContactId;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPhone;
    private Button btnUpdate;
    private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initUI();

        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    private void initUI() {
        edtContactId = findViewById(R.id.edt_contactId);
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phoneNumber);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);

        getContactDetail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                updateContract();
                break;
            case R.id.btn_delete:
                deleteContact();
                break;
        }
    }

    private void deleteContact() {
        String key = edtContactId.getText().toString();
        Utils.databaseReference().child(key).removeValue();
        finish();
    }

    private void updateContract() {
        String key = edtContactId.getText().toString();
        String phone = edtPhone.getText().toString();
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        Utils.databaseReference().child(key).child("phone").setValue(phone);
        Utils.databaseReference().child(key).child("email").setValue(email);
        Utils.databaseReference().child(key).child("name").setValue(name);
        finish();
    }

    private void getContactDetail() {
        Intent intent = getIntent();
        final String key = intent.getStringExtra("KEY");

        /* Truy xuất và lắng nghe sự thay đổi dữ liệu
         * chỉ truy xuất node được cọn trên listview databaseReference.child(key)
         * addListenerForSingleValueEvent để lấy dữ liệu đơn*/
        Utils.databaseReference().child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //nó trả về 1 DataSnapShot, mà giá trị đơn nên gọi getValue trả về 1 HashMap
                HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                /* HashMap này sẽ có kích  thước bằng số Node con bên trong node truy vấn
                 * mỗi phần tử có key là name được định nghĩa trong cấu trúc Json của Firebase*/
                edtContactId.setText(key);
                edtName.setText(hashMap.get("name").toString());
                edtEmail.setText(hashMap.get("email").toString());
                edtPhone.setText(hashMap.get("phone").toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
