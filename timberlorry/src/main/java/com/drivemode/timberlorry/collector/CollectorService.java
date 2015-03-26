package com.drivemode.timberlorry.collector;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author KeithYokoma
 */
public class CollectorService extends Service {
    private static Collector syncAdapter;
    private static final Object LOCK = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (LOCK) {
            if (syncAdapter == null) {
                syncAdapter = new Collector(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
