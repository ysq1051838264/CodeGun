package com.jiafeng.codegun.base;

import android.app.Application;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by yangshuquan on 2018/3/19.
 */

public class BaseApplication extends Application {

    public static Realm REALM_INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        // RealmConfiguration config = new RealmConfiguration.Builder().build();
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new MyMigration())
                .build();

        Realm.setDefaultConfiguration(config);
        REALM_INSTANCE = Realm.getInstance(config);
    }

    class MyMigration implements RealmMigration {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();

//            if (newVersion == 2) {
//                schema.get("User").addField("desc", String.class);
//            }
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof MyMigration;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
