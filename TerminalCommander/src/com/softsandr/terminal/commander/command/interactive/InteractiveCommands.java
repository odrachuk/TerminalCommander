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
package com.softsandr.terminal.commander.command.interactive;

import android.content.res.Resources;
import android.widget.TextView;
import com.softsandr.terminal.R;
import com.softsandr.terminal.commander.command.filtered.LsRowRecord;
import com.softsandr.terminal.commander.command.filtered.LsRowsList;

/**
 * The console commands that use console before not canceled
 */
public enum InteractiveCommands {
    TOP("top") {
        @Override
        public String processResponse(TextView outTextView, int screenWidth,
                                      Resources resources, String[] commandResponse) {
            return null;
        }
    };

    String text;

    private InteractiveCommands(String text) {
        this.text = text;
    }

    public static boolean isInteractiveCommand(String command) {
        boolean isInteractive = false;
        for (InteractiveCommands c : values()) {
            if (c.text.equals(command.trim())) {
                isInteractive = true;
                break;
            }
        }
        return isInteractive;
    }

    public static InteractiveCommands parseCommandTypeFromString(String command) {
        InteractiveCommands interactiveCommand = null;
        for (InteractiveCommands fc : values()) {
            if (fc.text.equals(command.trim())) {
                interactiveCommand = fc;
                break;
            }
        }
        return interactiveCommand;
    }

    public String getText() {
        return text;
    }

    public abstract String processResponse(TextView outTextView, int screenWidth, Resources resources, String[] commandResponse);
}
