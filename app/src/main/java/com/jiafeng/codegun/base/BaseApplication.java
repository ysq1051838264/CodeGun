package com.jiafeng.codegun.base;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by yangshuquan on 2018/3/19.
 */

public class BaseApplication extends Application{

    public static Realm REALM_INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        REALM_INSTANCE = Realm.getInstance(config);
    }
}
