package com.drivemode.timberlorry.plug;

import com.drivemode.timberlorry.payload.Payload;
import com.drivemode.timberlorry.payload.Serializer;
import com.google.gson.Gson;

/**
 * @author KeishinYokomaku
 */
public class GsonSerializer implements Serializer {
    @Override
    public String serialize(Payload payload) {
        return new Gson().toJson(payload);
    }
}
