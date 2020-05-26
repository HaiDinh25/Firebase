package com.example.firebase.activity;

import android.os.Bundle;
import com.example.firebase.R;
import com.example.firebase.fragment.LoginFragment;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gotoFragment(new LoginFragment(), R.id.container);
    }

}
