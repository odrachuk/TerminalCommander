package com.drk.terminal;

import android.app.Activity;
import android.content.Context;
import com.drk.terminal.utils.AccountUtils;
import com.drk.terminal.utils.DirectoryUtils;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 9:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalPrompt {
    private String mCurrentPath;
    private String mUserName;
    private String mPromptSymbol = "$"; // todo Determine privileges

    public TerminalPrompt(Context context) {
        mUserName = AccountUtils.getUserName(context);
        mCurrentPath = "/";
    }

    public String getPrompt() {
        StringBuilder promptText = new StringBuilder();
        promptText.append("[");
        promptText.append(mUserName + " ");
        promptText.append(mCurrentPath);
        promptText.append("]");
        promptText.append(mPromptSymbol + " ");
        return promptText.toString();
    }

    public String getCurrentPath() {
        return mCurrentPath;
    }

    public void setCurrentPath(String currentPath) {
        mCurrentPath = currentPath;
    }

    public String getUserName() {
        return mUserName;
    }
}
