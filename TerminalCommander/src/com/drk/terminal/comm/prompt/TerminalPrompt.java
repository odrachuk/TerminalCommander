package com.drk.terminal.comm.prompt;

import com.drk.terminal.comm.controller.UiController;
import com.drk.terminal.utils.AccountUtils;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 9:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalPrompt {
    private String mUserName;
    private String mUserSymbol = "$"; // todo Determine privileges
    private String mUserLocation;
    private UiController mUiController;

    public TerminalPrompt(UiController uiController) {
        mUserName = AccountUtils.getUserName(uiController.getActivity());
        mUiController = uiController;
    }

    public String getPromptText() {
        StringBuilder promptText = new StringBuilder();
        promptText.append("[");
        promptText.append(mUserName + " ");
        promptText.append(mUserLocation);
        promptText.append("]");
        promptText.append(mUserSymbol + " ");
        return promptText.toString();
    }

    public void setUserLocation(String newLocation) {
        mUserLocation = newLocation;
        mUiController.getTerminalPromptView().setText(getPromptText());
    }

    public String getUserLocation() {
        return mUserLocation;
    }
}
