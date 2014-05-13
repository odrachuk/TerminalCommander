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
package com.softsandr.commander;

import android.widget.TextView;
import com.softsandr.utils.account.AccountUtil;
import com.softsandr.utils.file.FileUtil;

/**
 * The text to left of input on console - prompt message for user
 */
public class PromptCompoundString {
    private String userName;
    private String userSymbol = "$"; // todo Add determining of real privileges
    private String userLocation;
    private TextView promptTextView;

    public PromptCompoundString(Commander commander) {
        userName = AccountUtil.getUserName(commander.getActivity());
        promptTextView = commander.getPromptTextView();
    }

    public String getCompoundString() {
        StringBuilder promptText = new StringBuilder();
        promptText.append("[");
        promptText.append(userName + " ");
        promptText.append(userLocation);
        promptText.append("]");
        promptText.append(userSymbol + " ");
        return promptText.toString();
    }

    public void setUserLocation(String newLocation) {
        userLocation = newLocation;
        promptTextView.setText(getCompoundString());
    }

    public String getUserLocation() {
        return userLocation;
    }
}
