package com.example.firebase.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class SharePref {
    private static final String USER_ROLE = "user_role";

    public static String getUserRole(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(USER_ROLE, "");
    }

    public static void setUserRole(Context context, String role) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(USER_ROLE, role)
                .apply();
    }
}
