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
package com.softsandr.terminal.commander.commands.filtered;

/**
 * An record after executing ls command
 */
public class LsRowRecord {
    private String permissionsToken = "";
    private String ownerToken = "";
    private String groupToken = "";
    private String sizeToken = "";
    private String dateToken = "";
    private String nameToken = "";

    public String getPermissionsToken() {
        return permissionsToken;
    }

    public void setPermissionsToken(String permissionsToken) {
        this.permissionsToken = permissionsToken;
    }

    public String getOwnerToken() {
        return ownerToken;
    }

    public void setOwnerToken(String ownerToken) {
        this.ownerToken = ownerToken;
    }

    public String getGroupToken() {
        return groupToken;
    }

    public void setGroupToken(String groupToken) {
        this.groupToken = groupToken;
    }

    public String getSizeToken() {
        return sizeToken;
    }

    public void setSizeToken(String sizeToken) {
        this.sizeToken = sizeToken;
    }

    public String getDateToken() {
        return dateToken;
    }

    public void setDateToken(String dateToken) {
        this.dateToken = dateToken;
    }

    public String getNameToken() {
        return nameToken;
    }

    public void setNameToken(String nameToken) {
        this.nameToken = nameToken;
    }
}
