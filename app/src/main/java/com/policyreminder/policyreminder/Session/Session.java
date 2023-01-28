package com.policyreminder.policyreminder.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {


    private static final String TAG = Session.class.getSimpleName();
    private static final String PREF_NAME = "sharedPreferences2";
    private static final String IS_LOGGEDIN = "isLoggedIn";
    private static final String FAV = "fav";
    private static final String Mobile = "mobile";
    private static final String Email = "email";
    private static final String UserId = "user_id";
    private static final String User_name = "user_name";
    private static final String Pro_Image = "pro_img";
    private static final String role_ = "user_role";
    private static final String LOGEDIN = "logedIn";
    private static final String LOGEDOUT = "logedout";

    private Context _context;
    public SharedPreferences sharedPreferences;
    private SharedPreferences.Editor  editor;
    Session session;

    public Session(Context context) {
        this._context = context;
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }




    public void setMobile(String mobile, String email) {
        editor.putString(Mobile, mobile);
        editor.putString(Email, email);
        editor.apply();
        editor.commit();
    }

    public String getMobile() {
        return sharedPreferences.getString(Mobile, "");

    }
    public  String getUser_name() {
        return sharedPreferences.getString(User_name, "");

    }
    public void setUserId(String userId) {
        editor.putString(UserId, userId);
        this.editor.apply();
    }

    public void set_role(String role) {
        editor.putString(role_, role);
        editor.apply();
        editor.commit();
    }

    public String get_role()
    {
        return sharedPreferences.getString(role_, "");
    }

    public String getUserId() {
        return sharedPreferences.getString(UserId, "");
    }




    public void setUser_name(String user_name) {
        editor.putString(User_name, user_name);
        this.editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(Email, "");
    }




/*
    public void logout() {
        editor.clear();
        editor.apply();
        Intent showLogin = new Intent(_context, LoginActivity.class);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(showLogin);
    }*/




    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGEDIN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGEDIN, false);
    }


    public void setValue(String key, String value ){
        editor.putString(key,value);
        editor.apply();
    }


    public String getValue(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void setmyLogin (boolean tf) {
        editor.putBoolean(LOGEDIN,tf);
        editor.apply();

    }


    public boolean getmyLogin () {

        return sharedPreferences.getBoolean(LOGEDIN, false);

    }

    public void setmyLogout (boolean tf) {
        editor.putBoolean(LOGEDOUT,tf);
        editor.clear();
        editor.apply();

    }


}
