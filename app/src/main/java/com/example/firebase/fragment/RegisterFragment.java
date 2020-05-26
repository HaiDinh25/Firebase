package com.example.firebase.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.firebase.R;
import com.example.firebase.model.UserDataModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener {
    private EditText edtUser;
    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtRePass;
    private Button btnRegister;
    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        edtUser = view.findViewById(R.id.edt_user);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPass = view.findViewById(R.id.edt_pass);
        edtRePass = view.findViewById(R.id.edt_re_pass);
        btnRegister = view.findViewById(R.id.btn_register);
        progressBar = view.findViewById(R.id.progressBar);

        firebaseFirestore = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.GONE);
        btnRegister.setVisibility(View.VISIBLE);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                final UserDataModel userDataModel = setData();
                registerEvent(userDataModel);
                break;
        }
    }

    private void registerEvent(UserDataModel userDataModel) {
        if (checkNull()) {
            if (checkPass()) {
                progressBar.setVisibility(View.VISIBLE);
                btnRegister.setVisibility(View.GONE);
                //Thêm mới collection (UserData) vào fireBaseStore
                firebaseFirestore.collection("user")
                        .add(userDataModel)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                clearData();
                                progressBar.setVisibility(View.GONE);
                                btnRegister.setVisibility(View.VISIBLE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            } else {
                Toast.makeText(getContext(), "Mật khẩu không trùng nhau.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Chưa nhập đủ thông tin.", Toast.LENGTH_SHORT).show();
        }
    }

    private UserDataModel setData() {
        UserDataModel userDataModel = new UserDataModel();
        userDataModel.setUser(edtUser.getText().toString());
        userDataModel.setEmail(edtEmail.getText().toString());
        userDataModel.setPass(edtPass.getText().toString());
        userDataModel.setRole("customer");

        return userDataModel;
    }

    private void clearData() {
        edtUser.getText().clear();
        edtEmail.getText().clear();
        edtPass.getText().clear();
        edtRePass.getText().clear();
    }

    private boolean checkPass() {
        if (edtRePass.getText().toString().equals(edtPass.getText().toString()))
            return true;
        return false;
    }

    private boolean checkNull() {
        if (!edtEmail.getText().toString().isEmpty() && !edtUser.getText().toString().isEmpty()
                && !edtPass.getText().toString().isEmpty() && !edtRePass.getText().toString().isEmpty())
            return true;
        return false;
    }
}
