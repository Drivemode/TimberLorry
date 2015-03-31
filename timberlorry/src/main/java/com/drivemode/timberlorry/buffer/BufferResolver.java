package com.drivemode.timberlorry.buffer;

import com.drivemode.timberlorry.payload.Payload;
import com.drivemode.timberlorry.payload.Record;
import com.drivemode.timberlorry.payload.Serializer;

import java.util.List;

/**
 * BufferResolver is a bridge class to a buffer storage like {@link android.content.ContentResolver}.
 * If you would like to use buffer based on content provider, you can use {@link DefaultBufferResolver}.
 *
 * @author KeithYokoma
 */
public interface BufferResolver {
    /**
     * Save the payload in the buffer.
     * @param serializer serializer of the payload.
     * @param payload to be saved.
     */
    void save(Serializer serializer, Payload payload);

    /**
     * Save all payloads in the buffer.
     * If payloads has 0 length, this method does nothing.
     * @param serializer serializer of the payload.
     * @param payloads to be saved.
     */
    void save(Serializer serializer, Payload... payloads);

    /**
     * Start sync now.
     */
    void sync();

    /**
     * Deploy sync with the specified period.
     * @param period the sync period.
     */
    void scheduleSync(long period);

    /**
     * Read all record from the buffer.
     */
    List<Record> fetch();

    /**
     * Remove the record from the buffer.
     * @param record to be removed.
     */
    void remove(Record record);

    /**
     * Remove all records from the buffer.
     */
    void clear();
}
