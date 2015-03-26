package com.drivemode.timberlorry.output;

import com.drivemode.timberlorry.internal.utils.Utils;

/**
 * {@inheritDoc}
 * @author KeithYokoma
 */
public class LogcatOutlet implements Outlet {
    public static final String TAG = LogcatOutlet.class.getSimpleName();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(Class<?> payload) {
        return true; // every payload is logged with this plug
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result publish(String payload) {
        Utils.logV(payload);
        return Result.success();
    }
}
