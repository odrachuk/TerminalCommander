/*******************************************************************************
 * Created by o.drachuk on 07/05/2014. 
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
package com.softsandr.commander.commands;

import com.softsandr.terminal.R;

/**
 * This class used for...
 */
public enum FCommand {
    FALSE("false", R.raw.false_man),
    FG("fg", R.raw.bash_builtins_man),
    FOR("fg", R.raw.for_man);

    private final String command;
    private final int fileId;

    FCommand(String command, int fileId) {
        this.command = command;
        this.fileId = fileId;
    }
}
