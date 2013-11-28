package com.softsandr.terminal.commander.prompt;

import com.softsandr.terminal.commander.controller.UiController;
import com.drk.terminal.util.utils.AccountUtil;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class TerminalPrompt {
    private String mUserName;
    private String mUserSymbol = "$"; // todo Determine privileges
    private String mUserLocation;
    private UiController mUiController;

    public TerminalPrompt(UiController uiController) {
        mUserName = AccountUtil.getUserName(uiController.getActivity());
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
