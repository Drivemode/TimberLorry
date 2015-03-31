package com.drivemode.timberlorry;

import android.util.Log;

import com.drivemode.timberlorry.buffer.BufferResolver;
import com.drivemode.timberlorry.internal.utils.Utils;
import com.drivemode.timberlorry.output.Outlet;
import com.drivemode.timberlorry.output.Result;
import com.drivemode.timberlorry.payload.Payload;
import com.drivemode.timberlorry.payload.Record;
import com.drivemode.timberlorry.payload.Serializer;

import java.util.List;

/**
 * TimberLorry is a periodical log collector.
 *
 * You can plug in your logging backend(e.g. Mixpanel, Google Analytics, etc...) with {@link Outlet} and log customize serialization form with {@link Serializer}.
 *
 * TimberLorry will take care about battery and bandwidth consumption on collecting logs.
 * If the log cannot be sent for some reason, it will be remained in the buffer and will be sent at the next period.
 *
 * @author KeithYokoma
 */
public abstract class TimberLorry {
    public static final String TAG = TimberLorry.class.getSimpleName();
    private static volatile TimberLorry instance;
    protected final Serializer serializer;
    protected final BufferResolver bufferResolver;
    protected final long period;
    protected final List<Outlet> outlets;

    /**
     * Construct the instance with the specified configuration.
     * @param config configuration.
     */
    /* package */ TimberLorry(Config config) {
        serializer = config.getSerializer();
        bufferResolver = config.getBufferResolver();
        period = config.getCollectPeriod();
        outlets = config.getOutlets();
    }

    /**
     * Construct the instance with the specified configuration.
     * The instance is singleton, so you may not initialize more than once.
     * @param config configuration.
     */
    public static void initialize(Config config) {
        if (instance != null) {
            Log.v(TAG, "already initialized");
            return;
        }
        synchronized (TimberLorry.class) {
            if (instance == null) {
                instance = new TimberLorryImpl(config);
            }
        }
    }

    /**
     * @return a singleton instance.
     */
    public static synchronized TimberLorry getInstance() {
        return instance;
    }

    /**
     * Clear the instance.
     */
    public static void destroy() {
        if (instance == null) {
            return;
        }
        instance = null;
    }

    /**
     * Track payload in the buffer. Log will be sent in a certain time.
     * @param payload to put in the buffer.
     */
    public abstract void load(Payload payload);

    /**
     * Track all payloads in the buffer. They will be sent in a certain time.
     * @param payloads to put in the buffer.
     */
    public abstract void load(Payload... payloads);

    /**
     * Fetch all logs in the buffer and force sending them.
     */
    public abstract void collect();

    /**
     * Start collecting periodically.
     */
    public abstract void schedule();

    /**
     * Start collecting now.
     */
    public abstract void dispatch();

    /**
     * Clear all payload in the buffer.
     */
    public abstract void unload();

    /* package */ static class TimberLorryImpl extends TimberLorry {
        /**
         * {@inheritDoc}
         */
        /* package */ TimberLorryImpl(Config config) {
            super(config);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void load(Payload payload) {
            bufferResolver.save(serializer, payload);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void load(Payload... payloads) {
            bufferResolver.save(serializer, payloads);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void collect() {
            List<Record> records = bufferResolver.fetch();
            for (Record record : records) {
                for (Outlet plug : outlets) {
                    if (!plug.accept(record.getClazz())) {
                        continue;
                    }
                    Result result = plug.dispatch(record.getBody());
                    if (result.isSuccess()) {
                        bufferResolver.remove(record);
                    } else {
                        Utils.logE("Cannot dispatch your log[" +
                                record.getClazz().getCanonicalName() +
                                "] to " + plug.name() +
                                " for the reason: ", result.getException());
                    }
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void schedule() {
            bufferResolver.scheduleSync(period);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void dispatch() {
            bufferResolver.sync();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unload() {
            bufferResolver.clear();
        }
    }
}
