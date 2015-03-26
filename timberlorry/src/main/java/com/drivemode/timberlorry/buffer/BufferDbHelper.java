package com.drivemode.timberlorry.buffer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author KeithYokoma
 */
public class BufferDbHelper extends SQLiteOpenHelper {
    private static final String NAME = "timberlorry_buffer.db";
    private static final int VERSION = 1;

    public BufferDbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        delete(db);
        create(db);
    }

    private void create(SQLiteDatabase db) {
        db.execSQL(BufferScheme.getCreateStatement());
    }

    private void delete(SQLiteDatabase db) {
        db.execSQL(BufferScheme.getDropStatement());
    }
}
