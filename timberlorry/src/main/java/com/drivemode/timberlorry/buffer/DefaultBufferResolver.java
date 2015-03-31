package com.drivemode.timberlorry.buffer;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.drivemode.timberlorry.internal.utils.Utils;
import com.drivemode.timberlorry.payload.Payload;
import com.drivemode.timberlorry.payload.Record;
import com.drivemode.timberlorry.payload.Serializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Buffer with content provider.
 * @author KeithYokoma
 */
public class DefaultBufferResolver extends AbstractBufferResolver {
    public DefaultBufferResolver(@NonNull AccountManager accountManager, @NonNull ContentResolver resolver, Account account) {
        super(accountManager, resolver, account);
    }

    @Override
    public void save(Serializer serializer, Payload payload) {
        Record record = new Record(payload.getClass(), serializer.serialize(payload));
        resolver.insert(BufferProvider.CONTENT_URI, record.toContentValues());
    }

    @Override
    public void save(Serializer serializer, Payload... payloads) {
        List<ContentValues> values = new ArrayList<>();
        for (Payload payload : payloads) {
            Record record = new Record(payload.getClass(), serializer.serialize(payload));
            values.add(record.toContentValues());
        }
        ContentValues[] array = values.toArray(new ContentValues[values.size()]);
        resolver.bulkInsert(BufferProvider.CONTENT_URI, array);
    }

    @Override
    public List<Record> fetch() {
        Cursor c = null;
        try {
            List<Record> payloads = new ArrayList<>();
            c = resolver.query(BufferProvider.CONTENT_URI, null, null, null, null);
            while (c.moveToNext()) {
                payloads.add(new Record(c));
            }
            return payloads;
        } finally {
            Utils.close(c);
        }
    }

    @Override
    public void remove(Record record) {
        Uri uri = ContentUris.withAppendedId(BufferProvider.CONTENT_URI, record.getId());
        resolver.delete(uri, null, null);
    }

    @Override
    public void clear() {
        List<Record> records = fetch();
        for (Record record : records) {
            remove(record);
        }
    }
}
