package com.drivemode.timberlorry.app;

import android.accounts.Account;
import android.app.Application;

import com.drivemode.timberlorry.Config;
import com.drivemode.timberlorry.TimberLorry;
import com.drivemode.timberlorry.output.LogcatOutlet;
import com.drivemode.timberlorry.plug.GsonSerializer;
import com.drivemode.timberlorry.plug.RealmBufferResolver;

/**
 * @author KeishinYokomaku
 */
public class TimberLorryApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TimberLorry.initialize(new Config.Builder()
                .collectIn(10)
                .serializeWith(new GsonSerializer())
                .bufferedOn(new RealmBufferResolver(this, new Account("TimberLorry", "timberlorry.drivemode.com"), "sample_realm"))
                .addOutlet(new LogcatOutlet()).build(this));
        TimberLorry.getInstance().schedule();
    }
}