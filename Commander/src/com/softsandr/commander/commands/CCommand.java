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
public enum CCommand {
    CASE("case", R.raw.case_man),
    CAT("cat", R.raw.cat_man),
    CD("cd", R.raw.cd_man),
    CHMOD("chmod", R.raw.chmod_man),
    CHOWN("chown", R.raw.chown_man),
    CLEAR("clear", R.raw.clear_man),
    CMP("cmp", R.raw.cmp_man),
    CP("cp", R.raw.cp_man);

    private final String command;
    private final int fileId;

    CCommand(String command, int fileId) {
        this.command = command;
        this.fileId = fileId;
    }
}
