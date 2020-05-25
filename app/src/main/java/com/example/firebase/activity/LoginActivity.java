package com.example.firebase.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.firebase.R;
import com.example.firebase.model.UserDataModel;
import com.example.firebase.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    EditText edtUser;
    EditText edtPass;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
    }

    private void initUI() {
        edtUser = findViewById(R.id.edt_user);
        edtPass = findViewById(R.id.edt_pass);
        btnLogin = findViewById(R.id.btn_login);

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

    private boolean checkNull() {
        if (edtUser.getText().toString().isEmpty() || edtPass.getText().toString().isEmpty())
            return false;
        return true;
    }

    private void loginEvent() {
        if (checkNull()) {
            Utils.databaseReferenceUser().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String user = edtUser.getText().toString();
                        String pass = edtPass.getText().toString();
                        UserDataModel account = data.getValue(UserDataModel.class);
                        if (account != null) {
                            if (user.equals(account.getUser()) && pass.equals(account.getPass())) {
                                gotoActivity(MainActivity.class);
                                finish();
                            } else {
                                Toast.makeText(getBaseContext(), "Thông tin chưa chính xác.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            Toast.makeText(this, "Chưa nhập đủ thông tin.", Toast.LENGTH_SHORT).show();
        }
    }
}
