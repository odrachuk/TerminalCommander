/*******************************************************************************
 * Created by o.drachuk on 13/01/2014. 
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
package com.softsandr.terminal.activity.commander;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.softsandr.commander.Commander;
import com.softsandr.terminal.data.filesystem.ProcessDirectory;
import com.softsandr.utils.string.StringUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class used for processing click on tabulate button from console activity
 * {@link com.softsandr.terminal.activity.commander.CommanderActivityImpl}
 */
public final class TabClickListener implements View.OnClickListener {
    private final Commander commander;
    private final EditText inputView;
    private final TextView outView;

    public TabClickListener(Commander commander, EditText inputView, TextView outView) {
        this.commander = commander;
        this.inputView = inputView;
        this.outView = outView;
    }

    @Override
    public void onClick(View v) {
        if (inputView.getText() != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    // determining start phrase for searching
                    String searchText = inputView.getText().toString().trim();
                    int lastIndexOfWhitespace = searchText.lastIndexOf(StringUtil.WHITESPACE) + 1;
                    searchText = searchText.substring(lastIndexOfWhitespace, searchText.length());
                    // determining current location of user
                    String currentLocation = commander.getPrompt().getUserLocation();
                    // reading user location
                    List<String> list = new ArrayList<String>();
                    ProcessDirectory.readUserLocation(list, currentLocation);
                    LinkedList<String> resultList = new LinkedList<String>();
                    for (String s : list) {
                        if (s.startsWith(searchText) || s.equals(searchText)) {
                            resultList.addLast(s);
                        }
                    }
                    // prepare result string
                    if (!resultList.isEmpty()) {
                        if (resultList.size() == 1) {
                            String inputText = inputView.getText().toString();
                            inputText = inputText.substring(0, lastIndexOfWhitespace) + resultList.get(0);
                            inputView.setText(inputText);
                            inputView.setSelection(inputText.length());
                        } else {
                            String outText = outView.getText() != null ?
                                    outView.getText().toString() + StringUtil.LINE_SEPARATOR : StringUtil.EMPTY;
                            for (String s : resultList) {
                                outText += "\'" + s + "\'" + StringUtil.WHITESPACE;
                            }
                            outView.setVisibility(View.VISIBLE);
                            outView.setText(outText);
                        }
                    }
                }
            });
        }
    }
}
