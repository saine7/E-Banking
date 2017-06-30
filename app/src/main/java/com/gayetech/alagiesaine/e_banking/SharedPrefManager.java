package com.gayetech.alagiesaine.e_banking;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private final static String SHARED_PREF = "shared_pref1";
    private final static String ACCOUNT_NO = "account_no";
    private final static String NAME = "name";
    private final static String EMAIL = "email";
    private final static String BALANCE = "balance";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }
    public static boolean userLogin(String account_no, String email,String name, int balance){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCOUNT_NO,account_no);
        editor.putString(EMAIL,email);
        editor.putString(NAME,name);
        editor.putInt(BALANCE,balance);

        editor.apply();
        return true;
    }
    public static boolean isUserLogin(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        String returnedEmail = sharedPreferences.getString(EMAIL,null);
        if (returnedEmail != null){
            return true;
        }
        return false;
    }
    public static boolean userLogout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public static String getAccountNo() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        String getAccount_no = sharedPreferences.getString(ACCOUNT_NO,null);
        return getAccount_no;
    }

    public static String getName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        String getName = sharedPreferences.getString(NAME,null);
        return getName;
    }
    public static int getBalance(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        int getBalance = sharedPreferences.getInt(BALANCE,0);
        //double realAmount = (double) getBalance;
        return getBalance;
    }
    /*
    public static void setBalance(int newBalance){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BALANCE,newBalance);
        editor.apply();
    }
    */
}

