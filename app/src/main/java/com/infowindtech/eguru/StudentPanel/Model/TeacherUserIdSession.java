package com.infowindtech.eguru.StudentPanel.Model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class TeacherUserIdSession {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "teacherid";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_USERID = "userid";
    public TeacherUserIdSession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void createUseridSession(String id) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_USERID,id);
        editor.commit();
    }
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        return user;
    }
}
