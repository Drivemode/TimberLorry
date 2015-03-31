package com.drivemode.timberlorry.payload;

import android.content.ContentValues;
import android.database.Cursor;

import com.drivemode.timberlorry.buffer.BufferScheme;
import com.drivemode.timberlorry.internal.utils.Utils;

/**
 * @author KeithYokoma
 */
public class Record {
    private final long id;
    private final Class<?> clazz;
    private final String body;

    public Record(Class<?> clazz, String body) {
        this.id = 0;
        this.clazz = clazz;
        this.body = body;
    }

    public Record(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex(BufferScheme._ID));
        this.clazz = Utils.forName(cursor.getString(cursor.getColumnIndex(BufferScheme.COLUMN_CLASS)));
        this.body = cursor.getString(cursor.getColumnIndex(BufferScheme.COLUMN_BODY));
    }

    @Override
    public String toString() {
        return "Log record[" + String.valueOf(id) + "] described by " + clazz.getCanonicalName() + " [" + body + "]";
    }

    public long getId() {
        return id;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getBody() {
        return body;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(BufferScheme.COLUMN_CLASS, clazz.getCanonicalName());
        values.put(BufferScheme.COLUMN_BODY, body);
        return values;
    }
}
