package com.example.firebase.activity;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

@SuppressLint("Registered")
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

    public void gotoFragment(Fragment fragment, int id) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(id, fragment, fragment.getClass().getSimpleName());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
