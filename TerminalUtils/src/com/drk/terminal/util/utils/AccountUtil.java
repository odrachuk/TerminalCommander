/*******************************************************************************
 * Created by o.drachuk on 10/01/2014.
 *
 * Copyright Oleksandr Drachuk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.drk.terminal.util.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import static com.drk.terminal.util.utils.StringUtil.EMPTY;

/**
 * The utility class used for getting information about user
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
