package com.drivemode.timberlorry.app;

import com.drivemode.timberlorry.payload.Payload;

/**
 * @author KeishinYokomaku
 */
public class ButtonClick implements Payload {
    private final long timeInMillis;

    public ButtonClick(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }
}
