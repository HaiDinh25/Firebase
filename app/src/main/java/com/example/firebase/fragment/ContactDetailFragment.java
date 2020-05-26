package com.example.firebase.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.firebase.R;
import com.example.firebase.activity.BaseActivity;
import com.example.firebase.utils.Const;
import com.example.firebase.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactDetailFragment extends BaseFragment implements View.OnClickListener {
    private EditText edtContactId;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPhone;
    private Button btnUpdate;
    private Button btnDelete;
    private String key;

    public ContactDetailFragment(String key) {
        this.key = key;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_detail, container, false);
        initUI(view);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        return view;
    }

    private void initUI(View view) {
        edtContactId = view.findViewById(R.id.edt_contactId);
        edtName = view.findViewById(R.id.edt_name);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPhone = view.findViewById(R.id.edt_phoneNumber);
        btnUpdate = view.findViewById(R.id.btn_update);
        btnDelete = view.findViewById(R.id.btn_delete);

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
        Utils.databaseReference(Const.Contact).child(key).removeValue();
        ((BaseActivity) getActivity()).gotoFragment(new ContactFragment(), R.id.container);
    }

    private void updateContract() {
        String key = edtContactId.getText().toString();
        String phone = edtPhone.getText().toString();
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        Utils.databaseReference(Const.Contact).child(key).child("name").setValue(name);
        Utils.databaseReference(Const.Contact).child(key).child("email").setValue(email);
        Utils.databaseReference(Const.Contact).child(key).child("phone").setValue(phone);
        ((BaseActivity) getActivity()).gotoFragment(new ContactFragment(), R.id.container);
    }

    private void getContactDetail() {
        /* Truy xuất và lắng nghe sự thay đổi dữ liệu
         * chỉ truy xuất node được chọn trên listview databaseReference.child(key)
         * addListenerForSingleValueEvent để lấy dữ liệu đơn*/
        Utils.databaseReference(Const.Contact).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //nó trả về 1 DataSnapShot, mà giá trị đơn nên gọi getValue trả về 1 HashMap
                HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                /* HashMap này sẽ có kích  thước bằng số Node con bên trong node truy vấn
                 * mỗi phần tử có key là name được định nghĩa trong cấu trúc Json của Firebase*/
                edtContactId.setText(key);
                edtName.setText((CharSequence) hashMap.get("name"));
                edtEmail.setText((CharSequence) hashMap.get("email"));
                edtPhone.setText((CharSequence) hashMap.get("phone"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
