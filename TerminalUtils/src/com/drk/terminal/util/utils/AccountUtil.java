package com.drk.terminal.util.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import static com.drk.terminal.util.utils.StringUtil.EMPTY;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class AccountUtil {

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    public static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    public static String getUserName(Context context) {
        AccountManager manager = AccountManager.get(context);
        Account account = getAccount(manager);
        if (account == null) {
            return EMPTY;
        } else {
            String email = account.name;
            String[] parts = email.split("@");
            if (parts.length > 0 && parts[0] != null) {
                return parts[0];
            } else {
                return EMPTY;
            }
        }
    }
}
