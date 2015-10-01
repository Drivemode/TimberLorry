package com.drivemode.timberlorry.plug;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.NonNull;

import com.drivemode.timberlorry.buffer.AbstractBufferResolver;
import com.drivemode.timberlorry.internal.utils.Utils;
import com.drivemode.timberlorry.payload.Payload;
import com.drivemode.timberlorry.payload.Record;
import com.drivemode.timberlorry.payload.Serializer;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * @author KeishinYokomaku
 */
public class RealmBufferResolver extends AbstractBufferResolver {
    public static final String TAG = RealmBufferResolver.class.getSimpleName();
    private final RealmConfiguration realmConfig;

    public RealmBufferResolver(Application application, Account account, String name) {
        this(application, (AccountManager) application.getSystemService(Context.ACCOUNT_SERVICE), application.getContentResolver(), account, name);
    }

    protected RealmBufferResolver(Application application, @NonNull AccountManager accountManager, @NonNull ContentResolver resolver, Account account, String name) {
        super(accountManager, resolver, account);
        this.realmConfig = new RealmConfiguration.Builder(application.getApplicationContext())
                .name(name)
                .build();
    }

    @Override
    public void save(Serializer serializer, Payload payload) {
        Record record = new Record(payload.getClass(), serializer.serialize(payload));
        Realm realm = null;
        try {
            realm = Realm.getInstance(realmConfig);
            realm.beginTransaction();
            RecordObject obj = realm.createObject(RecordObject.class);
            obj.setClassName(record.getClazz().getCanonicalName());
            obj.setBody(record.getBody());
            realm.commitTransaction();
        } finally {
            if (realm != null)
                realm.close();
        }
    }

    @Override
    public void save(Serializer serializer, Payload... payloads) {
        for (Payload payload : payloads) {
            save(serializer, payload);
        }
    }

    @Override
    public List<Record> fetch() {
        List<Record> records = new ArrayList<>();
        Realm realm = null;
        try {
            realm = Realm.getInstance(realmConfig);
            RealmQuery<RecordObject> query = realm.where(RecordObject.class);
            RealmResults<RecordObject> results = query.findAll();
            for (RecordObject obj : results) {
                records.add(new Record(Utils.forName(obj.getClassName()), obj.getBody()));
            }
            return records;
        } finally {
            if (realm != null)
                realm.close();
        }
    }

    @Override
    public void remove(Record record) {
        Realm realm = null;
        try {
            realm = Realm.getInstance(realmConfig);
            realm.beginTransaction();
            RealmQuery<RecordObject> query = realm.where(RecordObject.class);
            query.equalTo("className", record.getClazz().getCanonicalName())
                    .equalTo("body", record.getBody()).findAll().remove(0);
            realm.commitTransaction();
        } finally {
            if (realm != null)
                realm.close();
        }
    }

    @Override
    public void clear() {
        Realm realm = null;
        try {
            realm = Realm.getInstance(realmConfig);
            realm.beginTransaction();
            realm.where(RecordObject.class).findAll().clear();
            realm.commitTransaction();
        } finally {
            if (realm != null)
                realm.close();
        }
    }
}
