package com.drivemode.timberlorry.buffer;

import android.provider.BaseColumns;

/**
 * @author KeithYokoma
 */
public class BufferScheme implements BaseColumns {
    public static final String TABLE_NAME = "log_buffer";
    public static final String COLUMN_CLASS = "log_class";
    public static final String COLUMN_BODY  = "log_body";

    public static String getCreateStatement() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER NOT NULL PRIMARY KEY," +
                COLUMN_CLASS + " TEXT NOT NULL," +
                COLUMN_BODY + " TEXT NOT NULL)";
    }

    public static String getDropStatement() {
        return "DROP TABLE " + TABLE_NAME;
    }
}
