package com.drivemode.timberlorry.payload;

/**
 * Payload will be serialized with this class.
 *
 * @author KeithYokoma
 */
public interface Serializer {
    /**
     * Serialize the payload.
     * @param payload to be serialized.
     * @return serialized payload.
     */
    String serialize(Payload payload);
}
