package com.drivemode.timberlorry;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.drivemode.timberlorry.buffer.BufferResolver;
import com.drivemode.timberlorry.buffer.DefaultBufferResolver;
import com.drivemode.timberlorry.output.Outlet;
import com.drivemode.timberlorry.payload.Serializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KeithYokoma
 */
public class Config {
    private Serializer serializer;
    private BufferResolver bufferResolver;
    private long collectPeriod;
    private List<Outlet> outlets;
    private Account account;

    /* package */ Config() {
        outlets = new ArrayList<>();
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public BufferResolver getBufferResolver() {
        return bufferResolver;
    }

    public List<Outlet> getOutlets() {
        return outlets;
    }

    public long getCollectPeriod() {
        return collectPeriod;
    }

    public Account getAccount() {
        return account;
    }

    public static class Builder {
        private final Config config;

        public Builder() {
            config = new Config();
        }

        public Builder serializeWith(Serializer serializer) {
            config.serializer = serializer;
            return this;
        }

        public Builder bufferedOn(BufferResolver buffer) {
            config.bufferResolver = buffer;
            return this;
        }

        public Builder addOutlet(Outlet plug) {
            config.outlets.add(plug);
            return this;
        }

        public Builder collectIn(long period) {
            config.collectPeriod = period;
            return this;
        }

        public Builder syncOn(Account account) {
            config.account = account;
            return this;
        }

        public Config build(Context context) {
            if (config.bufferResolver == null) {
                if (config.account == null) {
                    config.account = new Account("TimberLorry", "timberlorry.drivemode.com");
                }
                config.bufferResolver = new DefaultBufferResolver(
                        (AccountManager) context.getApplicationContext().getSystemService(Context.ACCOUNT_SERVICE),
                        context.getApplicationContext().getContentResolver(), config.account);
            }
            if (config.serializer == null) {
                throw new IllegalStateException("Serializer cannot be null.");
            }
            if (config.outlets.isEmpty()) {
                throw new IllegalStateException("You need to add at least 1 OutputPlug.");
            }
            if (config.collectPeriod == 0) {
                throw new IllegalStateException("You need to specify collect period in second.");
            }
            return config;
        }
    }
}
