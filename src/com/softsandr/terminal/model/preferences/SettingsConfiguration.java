/*******************************************************************************
 * Created by o.drachuk on 23/01/2014. 
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
package com.softsandr.terminal.model.preferences;

import java.io.Serializable;

/**
 * This class provide data as important configuration objects
 */
public class SettingsConfiguration implements Serializable {

    private static final long serialVersionUID = -1322464043202270224L;

    /**
     * Size of font on main ListView
     */
    private int listFontSize;
    /**
     * The background color of main activity
     */
    private int terminalBgColor;
    /**
     * The color of archives
     */
    private int archiveItemColor;
    /**
     * The color of shell-scripts
     */
    private int shellItemColor;
    /**
     * The color of images
     */
    private int imageItemColor;
    /**
     * The color of media files in ListView
     */
    private int mediaItemColor;
    /**
     * The color of textual items in ListView
     */
    private int docItemColor;

    public int getListFontSize() {
        return listFontSize;
    }

    public void setListFontSize(int listFontSize) {
        this.listFontSize = listFontSize;
    }

    public int getTerminalBgColor() {
        return terminalBgColor;
    }

    public void setTerminalBgColor(int terminalBgColor) {
        this.terminalBgColor = terminalBgColor;
    }

    public int getArchiveItemColor() {
        return archiveItemColor;
    }

    public void setArchiveItemColor(int archiveItemColor) {
        this.archiveItemColor = archiveItemColor;
    }

    public int getShellItemColor() {
        return shellItemColor;
    }

    public void setShellItemColor(int shellItemColor) {
        this.shellItemColor = shellItemColor;
    }

    public int getImageItemColor() {
        return imageItemColor;
    }

    public void setImageItemColor(int imageItemColor) {
        this.imageItemColor = imageItemColor;
    }

    public int getMediaItemColor() {
        return mediaItemColor;
    }

    public void setMediaItemColor(int mediaItemColor) {
        this.mediaItemColor = mediaItemColor;
    }

    public int getDocItemColor() {
        return docItemColor;
    }

    public void setDocItemColor(int docItemColor) {
        this.docItemColor = docItemColor;
    }
}
