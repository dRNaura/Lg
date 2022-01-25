package com.lg_project.Session;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.lg_project.activity.Login;

public class SessionManageent {
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Lg";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_PINASKED = "ISPinasked";

    // User name (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_AGE = "age";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_STATE= "state";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ID = "id";
    public static final String KEY_CUST_ID = "cust_id";
    public static final String KEY_Credit = "credit";
    public static final String KEY_ACTIVE_PIN = "pin";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_SOCKET_ID = "socket_id";
    public static final String KEY_APP_LANGUAGE = "app_language";
    public static final String KEY_APP_ICON = "app_icon";
    public static final String KEY_STEALTH_CODE = "stealth_code";
    public static final String KEY_STEALTH_MODE = "stealth_mode";
    public static final String KEY_STEALTH_MSG = "stealth_msg";

//    SharedPreferences sharedPreferences;
    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManageent(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String firstName,String password,String email,int id,String credit,int cust_id,String active_pin,int type,String token){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_FIRSTNAME, firstName);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        editor.putInt(KEY_ID, id);
        editor.putInt(KEY_CUST_ID, cust_id);
        editor.putString(KEY_Credit,credit);
        editor.putString(KEY_ACTIVE_PIN,active_pin);
        editor.putString(KEY_TOKEN,token);
        editor.putInt(KEY_USER_TYPE,type);

        // commit changes
        editor.commit();
    }

    public void preferedSetting(String app_icon,int stealth_code,int stealth_mode,String Stealthmsg){
        editor.putString(KEY_APP_ICON, app_icon);
        editor.putInt(KEY_STEALTH_CODE, stealth_code);
        editor.putInt(KEY_STEALTH_MODE, stealth_mode);
        editor.putString(KEY_STEALTH_MSG, Stealthmsg);
        editor.commit();
    }

    public void getInfoFromLoginSession(int age,String phone,int country,String state){

        editor.putInt(KEY_AGE, age);
        editor.putString(KEY_PHONE, phone);
        editor.putInt(KEY_COUNTRY, country);
        editor.putString(KEY_STATE,state);
        // commit changes
        editor.commit();

    }

    public void pinAskedSession(boolean isPnAsked){
        editor.putBoolean(IS_PINASKED, isPnAsked);
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
//        else if(this.isPinAsked()){
//                Intent i = new Intent(_context, EnterPinActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                // Add new Flag to start new Activity
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                _context.startActivity(i);
//
//        }else {
//            Intent intent = new Intent(_context, MainActivity.class);
//            // Add new Flag to start new Activity
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            _context.startActivity(intent);
//        }

//        else{
//            sharedPreferences = _context.getSharedPreferences("check_pin_option", MODE_PRIVATE);
//            boolean pinAsked = sharedPreferences.getBoolean("pinAsked", false);
//            if(pinAsked){
//                Intent i = new Intent(_context, EnterPinActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                _context.startActivity(i);
//            }else{
//                Intent intent=new Intent(_context, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                _context.startActivity(intent);
//            }
//
//        }

    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_FIRSTNAME, pref.getString(KEY_FIRSTNAME, null));
        user.put(KEY_ID, String.valueOf(pref.getInt(KEY_ID, 0)));
        user.put(KEY_CUST_ID, String.valueOf(pref.getInt(KEY_CUST_ID, 0)));
        user.put(KEY_USER_TYPE, String.valueOf(pref.getInt(KEY_USER_TYPE, 0)));
        user.put(KEY_Credit,pref.getString(KEY_Credit, null));
        user.put(KEY_ACTIVE_PIN,pref.getString(KEY_ACTIVE_PIN, null));
        user.put(KEY_TOKEN,pref.getString(KEY_TOKEN, null));
        user.put(KEY_AGE, String.valueOf(pref.getInt(KEY_AGE, 0)));
        user.put(KEY_PHONE,pref.getString(KEY_PHONE, null));
        user.put(KEY_COUNTRY, String.valueOf(pref.getInt(KEY_COUNTRY, 0)));
        user.put(KEY_STATE,pref.getString(KEY_STATE, null));

        // return user
        return user;
    }

    public HashMap<String, String> getPreferedSetting(){
        HashMap<String, String> setting = new HashMap<String, String>();
        setting.put(KEY_APP_ICON,pref.getString(KEY_APP_ICON, null));
        setting.put(KEY_STEALTH_CODE,String.valueOf(pref.getInt(KEY_STEALTH_CODE, 0)));
        setting.put(KEY_STEALTH_MODE,String.valueOf(pref.getInt(KEY_STEALTH_MODE, 0)));
        setting.put(KEY_STEALTH_MSG,pref.getString(KEY_STEALTH_MSG, ""));

        return setting;
    }


    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);


    }

    public void socketID(String socketID){
        editor.putString(KEY_SOCKET_ID, socketID);
        editor.commit();
    }

    public void AppLanguage(String applanguage){
        editor.putString(KEY_APP_LANGUAGE, applanguage);
        editor.apply();
    }

    public HashMap<String, String> getAppLanguage(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_APP_LANGUAGE, pref.getString(KEY_APP_LANGUAGE, "en"));

        // return user
        return user;
    }



    public HashMap<String, String> getSocketID(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_SOCKET_ID, pref.getString(KEY_SOCKET_ID, ""));

        // return user
        return user;
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public boolean isPinAsked(){
        return pref.getBoolean(IS_PINASKED, true);
    }
}