package com.example.firebase.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.firebase.R;
import com.example.firebase.activity.BaseActivity;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private EditText edtUser;
    private EditText edtPass;
    private Button btnLogin;
    private Button btnRegister;
    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore;

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
        btnRegister = view.findViewById(R.id.btn_register);
        progressBar = view.findViewById(R.id.progressBar);

        firebaseFirestore = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.GONE);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loginEvent();
                break;
            case R.id.btn_register:
                ((BaseActivity) getActivity()).gotoFragment(new RegisterFragment(), R.id.container);
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

    }
}
