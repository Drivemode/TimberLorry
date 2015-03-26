package com.drivemode.timberlorry.internal.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author KeithYokoma
 */
public class Utils {
    private Utils() {
        throw new AssertionError("no instance!");
    }

    public static void close(Cursor cursor) {
        if (cursor == null)
            return;
        cursor.close();
    }

    public static void close(SQLiteDatabase db) {
        if (db == null)
            return;
        db.close();
    }

    public static void endTransaction(SQLiteDatabase db) {
        if (db == null)
            return;
        db.endTransaction();
    }

    public static void logV(String message) {
        Log.v("TimberLorry", message);
    }

    public static void logE(String message, Throwable e) {
        Log.e("TimberLorry", message, e);
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            logE("Unknown class for the current class loader: ", e);
            return null;
        }
    }

    public static boolean isAccountAdded(AccountManager manager, String type) {
        Account[] accounts = manager.getAccountsByType(type);
        return !(accounts == null || accounts.length == 0);
    }
}
