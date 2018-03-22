package com.jiafeng.codegun.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yangshuquan on 2018/3/5.
 */

public class ShareHelper {
    public static final String CONFIG_SP_NAME = "code_gun";
    public static final String KEY_MAC = "key_mac";
    public static final String KEY_IMEI = "key_imei";
    public static final String KEY_SN = "key_sn";
    public static final String KEY_COMPANY_NO = "key_company_no";
    private SharedPreferences configSp;

    private static ShareHelper instance;

    public ShareHelper(Context context) {
        configSp = context.getSharedPreferences(CONFIG_SP_NAME, Context.MODE_PRIVATE);
    }

    public static ShareHelper initInstance(Context context) {
        if (instance == null) {
            instance = new ShareHelper(context);
        }
        return instance;
    }

    public static ShareHelper getInstance() {
        return instance;
    }

    public String getString(String key, String defaultValue) {
        return configSp.getString(key, defaultValue);
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = getConfigEditor();
        editor.putString(key, value);
        editor.apply();
    }

    public void clearData() {
        SharedPreferences.Editor editor = getConfigEditor();
        editor.clear();
        editor.apply();
    }

    public SharedPreferences.Editor getConfigEditor() {
        return configSp.edit();
    }


}
