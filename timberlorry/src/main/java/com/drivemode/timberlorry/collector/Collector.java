package com.drivemode.timberlorry.collector;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.drivemode.timberlorry.TimberLorry;
import com.drivemode.timberlorry.internal.utils.Utils;

/**
 * Asynchronous log collector.
 * @author KeithYokoma
 */
public class Collector extends AbstractThreadedSyncAdapter {
    public static final String TAG = Collector.class.getSimpleName();

    public Collector(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Utils.logV("start collecting...");
        TimberLorry timberLorry = TimberLorry.getInstance();
        timberLorry.collect();
    }
}
