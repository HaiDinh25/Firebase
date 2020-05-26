package com.example.firebase.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.firebase.R;
import com.example.firebase.activity.BaseActivity;
import com.example.firebase.activity.MainActivity;
import com.example.firebase.model.UserDataModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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
    private List<UserDataModel> userDataList = new ArrayList<>();
    private boolean checkUser = false;

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
        if (checkNull()) {
            progressBar.setVisibility(View.VISIBLE);
            //Get data in field document from user collection (select * from A).
            //Later compared to user & password to login
            firebaseFirestore.collection("user")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            progressBar.setVisibility(View.GONE);
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                UserDataModel userData = document.toObject(UserDataModel.class);
                                userDataList.add(userData);
                                for (int i = 0; i < userDataList.size(); i++) {
                                    if (edtUser.getText().toString().equals(userDataList.get(i).getUser())) {
                                        checkUser = true;
                                        if (edtPass.getText().toString().equals(userDataList.get(i).getPass())) {
                                            ((BaseActivity) getActivity()).gotoActivity(MainActivity.class);
                                            break;
                                        } else {
                                            Toast.makeText(getContext(), "Mật khẩu chưa chính xác.", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    } else {
                                        checkUser = false;
                                    }
                                }
                            }
                            if (!checkUser) {
                                Toast.makeText(getContext(), "User name chưa chính xác.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Chưa nhập đủ thông tin.", Toast.LENGTH_SHORT).show();
        }

//        if (checkNull()) {
//            progressBar.setVisibility(View.VISIBLE);
//            //Get data from user collection by field (select A from where B).
//            //Later compared to user & password to login
//            firebaseFirestore.collection("user")
//                    .whereEqualTo("user", edtUser.getText().toString())
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            progressBar.setVisibility(View.GONE);
//                            if (task.isSuccessful()) {
//                                for (DocumentSnapshot document : task.getResult()) {
//                                    //Convert sang đối tượng UserData
//                                    UserDataModel userData = document.toObject(UserDataModel.class);
//                                    if (edtPass.getText().toString().equals(userData.getPass())) {
//                                        ((BaseActivity) getActivity()).gotoActivity(MainActivity.class);
//                                    } else {
//                                        Toast.makeText(getContext(), "Mật khẩu chưa chính xác.", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            } else {
//                                Toast.makeText(getContext(), "User name chưa chính xác.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        } else {
//            Toast.makeText(getContext(), "Chưa nhập đủ thông tin.", Toast.LENGTH_SHORT).show();
//        }
    }
}
