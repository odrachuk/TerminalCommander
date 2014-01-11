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
package com.softsandr.terminal.commander;

import com.softsandr.utils.account.AccountUtil;

/**
 * The text to left of input on console - prompt message for user
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
