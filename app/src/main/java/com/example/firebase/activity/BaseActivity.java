package com.example.firebase.activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public void gotoActivity(Class t) {
        Intent intent = new Intent(this, t);
        startActivity(intent);
    }

    public void gotoActivity(Class t, String key, String value) {
        Intent intent = new Intent(this, t);
        intent.putExtra(key, value);
        startActivity(intent);
    }
}
