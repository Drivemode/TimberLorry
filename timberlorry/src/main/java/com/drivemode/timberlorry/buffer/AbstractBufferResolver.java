package com.drivemode.timberlorry.buffer;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.drivemode.timberlorry.internal.utils.Utils;

/**
 * @author KeishinYokomaku
 */
public abstract class AbstractBufferResolver implements BufferResolver {
    protected final AccountManager accountManager;
    protected final ContentResolver resolver;
    protected final Account account;

    protected AbstractBufferResolver(@NonNull AccountManager accountManager, @NonNull ContentResolver resolver, Account account) {
        this.accountManager = accountManager;
        this.resolver = resolver;
        this.account = account;
    }

    @Override
    public void sync() {
        // TODO: consider the condition that master sync is disabled.
        ContentResolver.requestSync(account, BufferProvider.AUTHORITY, Bundle.EMPTY);
    }

    @Override
    public void scheduleSync(long period) {
        // TODO: consider the condition that master sync is disabled.
        if (!Utils.isAccountAdded(accountManager, account.type)) {
            accountManager.addAccountExplicitly(account, null, null);
        }
        ContentResolver.setSyncAutomatically(account, BufferProvider.AUTHORITY, true);
        ContentResolver.addPeriodicSync(account, BufferProvider.AUTHORITY, Bundle.EMPTY, period);
    }
}
