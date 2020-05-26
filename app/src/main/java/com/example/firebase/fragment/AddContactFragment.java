package com.example.firebase.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebase.R;
import com.example.firebase.activity.BaseActivity;
import com.example.firebase.utils.Const;
import com.example.firebase.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContactFragment extends BaseFragment implements View.OnClickListener {
    private EditText edtContactId;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPhone;
    private Button btnAdd;

    public AddContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        initUI(view);
        btnAdd.setOnClickListener(this);
        return view;
    }

    private void initUI (View view) {
        edtContactId = view.findViewById(R.id.edt_contactId);
        edtName = view.findViewById(R.id.edt_name);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPhone = view.findViewById(R.id.edt_phoneNumber);
        btnAdd = view.findViewById(R.id.btn_add);
    }

    private boolean checkNull() {
        if (edtContactId.getText().toString().isEmpty() || edtName.getText().toString().isEmpty()
                || edtEmail.getText().toString().isEmpty() || edtPhone.getText().toString().isEmpty())
            return false;
        return true;
    }

    private void addContacts () {
        if (checkNull()) {
            String id = edtContactId.getText().toString();
            String name = edtName.getText().toString();
            String email = edtEmail.getText().toString();
            String phone = edtPhone.getText().toString();

            Utils.databaseReference(Const.Contact).child(id).child("name").setValue(name);
            Utils.databaseReference(Const.Contact).child(id).child("email").setValue(email);
            Utils.databaseReference(Const.Contact).child(id).child("phone").setValue(phone);
            ((BaseActivity) getActivity()).gotoFragment(new ContactFragment(), R.id.container);
        } else {
            Toast.makeText(getContext(), "Chưa nhập đủ thông tin.", Toast.LENGTH_SHORT).show();
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
