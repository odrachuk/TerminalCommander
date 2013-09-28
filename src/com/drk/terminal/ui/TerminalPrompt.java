package com.drk.terminal.ui;

import android.os.Handler;
import com.drk.terminal.controller.UiController;
import com.drk.terminal.ui.TerminalActivity;
import com.drk.terminal.utils.AccountUtils;

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
    private UiController mUiController;

    public TerminalPrompt(UiController uiController) {
        mUserName = AccountUtils.getUserName(uiController.getActivity());
        mUiController = uiController;
    }

    public String getFullText() {
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
        mUiController.getActivity().getTerminalPromptView().setText(getFullText());
    }

    public String getUserName() {
        return mUserName;
    }
}
