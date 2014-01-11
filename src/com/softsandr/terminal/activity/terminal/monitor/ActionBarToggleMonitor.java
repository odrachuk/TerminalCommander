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
package com.softsandr.terminal.activity.terminal.monitor;

import android.content.res.Resources;
import android.view.View;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.terminal.TerminalActivity;

/**
 * This class used for monitoring toggle mode of group of buttons on ActionBar of
 * {@link com.softsandr.terminal.activity.terminal.TerminalActivityImpl}
 */
public class ActionBarToggleMonitor {
    private final View shiftBtnContainer, ctrlBtnContainer;
    private final Resources resources;
    private final TerminalActivity terminal;

    private boolean isShiftToggled;
    private boolean isCtrlToggled;

    public ActionBarToggleMonitor(TerminalActivity terminal, View shiftBtnContainer, View ctrlBtnContainer) {
        this.terminal = terminal;
        resources = terminal.getContextResources();
        this.shiftBtnContainer = shiftBtnContainer;
        this.ctrlBtnContainer = ctrlBtnContainer;
    }

    public void onClickShift() {
        if (isCtrlToggled) {
            isCtrlToggled = false;
            ctrlBtnContainer.setBackgroundColor(resources.getColor(android.R.color.transparent));
            terminal.getLeftListAdapter().getSelectionMonitor().setCtrlToggle(false);
            terminal.getRightListAdapter().getSelectionMonitor().setCtrlToggle(false);
        }
        if (isShiftToggled) {
            isShiftToggled = false;
            shiftBtnContainer.setBackgroundColor(resources.getColor(android.R.color.transparent));
        } else {
            isShiftToggled = true;
            shiftBtnContainer.setBackgroundColor(resources.getColor(R.color.COLOR_FEA50A));
        }
        terminal.getLeftListAdapter().getSelectionMonitor().setShiftToggle(isShiftToggled);
        terminal.getRightListAdapter().getSelectionMonitor().setShiftToggle(isShiftToggled);
    }

    public void onClickCtrl() {
        if (isShiftToggled) {
            isShiftToggled = false;
            shiftBtnContainer.setBackgroundColor(resources.getColor(android.R.color.transparent));
            terminal.getLeftListAdapter().getSelectionMonitor().setShiftToggle(false);
            terminal.getRightListAdapter().getSelectionMonitor().setShiftToggle(false);
        }
        if (isCtrlToggled) {
            isCtrlToggled = false;
            ctrlBtnContainer.setBackgroundColor(resources.getColor(android.R.color.transparent));
        } else {
            isCtrlToggled = true;
            ctrlBtnContainer.setBackgroundColor(resources.getColor(R.color.COLOR_FEA50A));
        }
        terminal.getLeftListAdapter().getSelectionMonitor().setCtrlToggle(isCtrlToggled);
        terminal.getRightListAdapter().getSelectionMonitor().setCtrlToggle(isCtrlToggled);
    }
}
