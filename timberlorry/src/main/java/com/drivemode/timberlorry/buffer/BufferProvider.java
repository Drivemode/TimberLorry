package com.drivemode.timberlorry.buffer;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.drivemode.timberlorry.internal.utils.Utils;

/**
 * @author KeithYokoma
 */
public class BufferProvider extends ContentProvider {
    public static final String AUTHORITY = "com.drivemode.timberlorry.buffer.BufferProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BufferScheme.TABLE_NAME);
    public static final int TYPE_LOG_BUFFERS = 1;
    public static final int TYPE_LOG_BUFFER = 2;
    public static final String CONTENT_TYPE_LOG_BUFFERS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.drivemode.log_buffer";
    public static final String CONTENT_TYPE_LOG_BUFFER = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.drivemode.log_buffer";
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private BufferDbHelper helper;

    static {
        MATCHER.addURI(AUTHORITY, BufferScheme.TABLE_NAME, TYPE_LOG_BUFFERS);
        MATCHER.addURI(AUTHORITY, BufferScheme.TABLE_NAME + "/#", TYPE_LOG_BUFFER);
    }

    @Override
    public boolean onCreate() {
        helper = new BufferDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            String newSelection = buildSelection(uri, selection);
            builder.setTables(BufferScheme.TABLE_NAME);
            db = helper.getReadableDatabase();
            c = builder.query(db, projection, newSelection, selectionArgs, null, null, sortOrder);
            if (c != null && getContext() != null) {
                c.setNotificationUri(getContext().getContentResolver(), uri);
            }
            return c;
        } catch (RuntimeException e) {
            Utils.close(c);
            Utils.close(db);
            throw e;
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)) {
            case TYPE_LOG_BUFFER:
                return CONTENT_TYPE_LOG_BUFFER;
            case TYPE_LOG_BUFFERS:
                return CONTENT_TYPE_LOG_BUFFERS;
            default:
                throw new IllegalArgumentException("unknown uri: " + uri);
        }
    }

    @Override
    public int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        int count = 0;
        try {
            SQLiteStatement insertStmt = db.compileStatement(
                    "INSERT INTO " + BufferScheme.TABLE_NAME + " (" +
                            BufferScheme.COLUMN_CLASS + ", " +
                            BufferScheme.COLUMN_BODY + ") VALUES (?, ?);");
            for (ContentValues value : values) {
                insertStmt.bindString(1, value.getAsString("name"));
                long id = insertStmt.executeInsert();
                if (id != -1) {
                    count++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            Utils.endTransaction(db);
        }
        return count;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (MATCHER.match(uri) != TYPE_LOG_BUFFERS) {
            throw new IllegalArgumentException("unknown uri: " + uri);
        }
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            long id = db.insert(BufferScheme.TABLE_NAME, null, values);
            Uri inserted = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(inserted, null, false);
            db.setTransactionSuccessful();
            return inserted;
        } finally {
            Utils.endTransaction(db);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (MATCHER.match(uri) != TYPE_LOG_BUFFER) {
            throw new IllegalArgumentException("unknown uri: " + uri);
        }
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            String newSelection = buildSelection(uri, selection);
            int count = db.delete(BufferScheme.TABLE_NAME, newSelection, selectionArgs);
            getContext().getContentResolver().notifyChange(CONTENT_URI, null, false);
            db.setTransactionSuccessful();
            return count;
        } finally {
            Utils.endTransaction(db);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            String newSelection = buildSelection(uri, selection);
            int count = db.update(BufferScheme.TABLE_NAME, values, newSelection, selectionArgs);
            getContext().getContentResolver().notifyChange(CONTENT_URI, null, false);
            db.setTransactionSuccessful();
            return count;
        } finally {
            Utils.endTransaction(db);
        }
    }

    private String buildSelection(Uri uri, String selection) {
        long id;
        String additionalSelection = null;

        switch (MATCHER.match(uri)) {
            case TYPE_LOG_BUFFER:
                id = ContentUris.parseId(uri);
                additionalSelection = BufferScheme._ID + " = " + id;
                break;
            case TYPE_LOG_BUFFERS:
                // do nothing
                break;
            default:
                throw new IllegalArgumentException("unknown uri: " + uri);
        }

        if (additionalSelection == null) {
            return selection;
        }
        if (selection == null) {
            return additionalSelection;
        }
        return additionalSelection + " AND " + selection;
    }
}
