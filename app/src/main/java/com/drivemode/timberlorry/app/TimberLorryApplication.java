package com.drivemode.timberlorry.app;

import android.app.Application;

import com.drivemode.timberlorry.Config;
import com.drivemode.timberlorry.TimberLorry;
import com.drivemode.timberlorry.output.LogcatOutlet;
import com.drivemode.timberlorry.plug.GsonSerializer;

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
                .addOutlet(new LogcatOutlet()).build(this));
        TimberLorry.getInstance().schedule();
    }
}