package com.example.firebase.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.firebase.R;
import com.example.firebase.activity.BaseActivity;
import com.example.firebase.activity.MainActivity;
import com.example.firebase.model.UserDataModel;
import com.example.firebase.utils.Const;
import com.example.firebase.utils.SharePref;
import com.example.firebase.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private EditText edtUser;
    private EditText edtPass;
    private Button btnLogin;
    private ProgressBar progressBar;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        edtUser = view.findViewById(R.id.edt_user);
        edtPass = view.findViewById(R.id.edt_pass);
        btnLogin = view.findViewById(R.id.btn_login);
        progressBar = view.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loginEvent();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    private boolean checkNull() {
        if (edtUser.getText().toString().isEmpty() || edtPass.getText().toString().isEmpty())
            return false;
        return true;
    }

    private void loginEvent() {
        if (checkNull()) {
            progressBar.setVisibility(View.VISIBLE);
            Utils.databaseReference(Const.UserData).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String user = edtUser.getText().toString();
                        String pass = edtPass.getText().toString();
                        UserDataModel account = data.getValue(UserDataModel.class);
                        if (account != null) {
                            if (user.equals(account.getUser()) && pass.equals(account.getPass())) {
                                progressBar.setVisibility(View.GONE);
                                SharePref.setUserRole(getContext(), account.getRole());
                                ((BaseActivity) getActivity()).gotoActivity(MainActivity.class);
                                getActivity().finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Thông tin chưa chính xác.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            Toast.makeText(getContext(), "Chưa nhập đủ thông tin.", Toast.LENGTH_SHORT).show();
        }
    }
}
