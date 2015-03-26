package com.drivemode.timberlorry.internal.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author KeithYokoma
 */
public class StubAuthenticatorService extends Service {
    private StubAuthenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        authenticator = new StubAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
