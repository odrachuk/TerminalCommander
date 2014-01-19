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
package com.softsandr.terminal.activity.terminal.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.commander.CommanderActivityImpl;
import com.softsandr.terminal.activity.terminal.ActivePage;
import com.softsandr.terminal.activity.terminal.TerminalActivityImpl;
import com.softsandr.terminal.activity.terminal.TerminalActivity;
import com.softsandr.terminal.dialog.TerminalDialogUtil;

/**
 * This class used for localization logic of processing all possible click events on
 * {@link com.softsandr.terminal.activity.terminal.TerminalActivityImpl}
 */
public class TerminalClickListener implements View.OnClickListener {
    private final TerminalActivity terminal;

    public TerminalClickListener(TerminalActivity terminal) {
        this.terminal = terminal;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        String destinationLocation = !terminal.getActivePage().equals(ActivePage.LEFT) ?
                terminal.getLeftListAdapter().getLocationLabel().getPath() :
                terminal.getRightListAdapter().getLocationLabel().getPath();
        String currentLocation = terminal.getActivePage().equals(ActivePage.LEFT) ?
                terminal.getLeftListAdapter().getLocationLabel().getPath() :
                terminal.getRightListAdapter().getLocationLabel().getPath();
            /* Menu items */
        if (v.getId() == R.id.action_bar_comm_btn) {
            Intent startIntent = new Intent((Activity) terminal, CommanderActivityImpl.class);
            startIntent.putExtra(CommanderActivityImpl.WORK_PATH_EXTRA, currentLocation);
            startIntent.putExtra(CommanderActivityImpl.OTHER_PATH_EXTRA, destinationLocation);
            startIntent.putExtra(CommanderActivityImpl.ACTIVE_PAGE_EXTRA, terminal.getActivePage().equals(ActivePage.LEFT));
            ((Activity) terminal).startActivityForResult(startIntent, TerminalActivityImpl.REQUEST_CODE);
        } else if (v.getId() == R.id.action_bar_shift_btn) {
            terminal.getActionBarToggleMonitor().onClickShift();
        } else if (v.getId() == R.id.action_bar_ctrl_btn) {
            terminal.getActionBarToggleMonitor().onClickCtrl();
        }
        /* Control buttons */
        else if (viewId == R.id.copy_btn) {
            if (!terminal.getOperationItems().isEmpty()) {
                TerminalDialogUtil.showCopyDialog((Activity) terminal,
                        terminal.getOperationItems(),
                        destinationLocation);
            } else {
                showNoObjectSelectedToast();
            }
        } else if (viewId == R.id.move_btn) {
            if (!terminal.getOperationItems().isEmpty()) {
                TerminalDialogUtil.showMoveDialog((Activity) terminal,
                        terminal.getOperationItems(),
                        currentLocation,
                        destinationLocation);
            } else {
                showNoObjectSelectedToast();
            }
        } else if (viewId == R.id.rename_btn) {
            if (terminal.getOperationItems().isEmpty()) {
                showNoObjectSelectedToast();
            } else if (terminal.getOperationItems().size() == 1) {
                TerminalDialogUtil.showRenameDialog((Activity) terminal,
                        terminal.getOperationItems().get(0),
                        currentLocation,
                        destinationLocation);
            } else {
                showMultipleObjectsToast();
            }
        } else if (viewId == R.id.mkdir_btn) {
            TerminalDialogUtil.showMkDirDialog((Activity) terminal, currentLocation, destinationLocation);
        } else if (viewId == R.id.delete_btn) {
            if (!terminal.getOperationItems().isEmpty()) {
                TerminalDialogUtil.showDeleteDialog((Activity) terminal,
                        terminal.getOperationItems(),
                        currentLocation,
                        destinationLocation);
            } else {
                showNoObjectSelectedToast();
            }
        }
        /* History buttons */
        else if (viewId == R.id.history_btn_in_left) {
            String[] locations = terminal.getLeftHistoryLocationManager().getActualHistoryLocations();
            if (locations.length > 0) {
                TerminalDialogUtil.showHistoryDialog((Activity) terminal,
                        locations,
                        ActivePage.LEFT);
            } else {
                showHistoryIsEmptyToast();
            }
        } else if (viewId == R.id.history_btn_in_right) {
            String[] locations = terminal.getRightHistoryLocationManager().getActualHistoryLocations();
            if (locations.length > 0) {
                TerminalDialogUtil.showHistoryDialog((Activity) terminal,
                        locations,
                        ActivePage.RIGHT);
            } else {
                showHistoryIsEmptyToast();
            }
        }
    }

    private void showNoObjectSelectedToast() {
        Toast.makeText((Activity) terminal, ((Activity) terminal).getString(R.string.toast_no_objects_selected),
                Toast.LENGTH_SHORT).show();
    }

    private void showHistoryIsEmptyToast() {
        Toast.makeText((Activity) terminal, ((Activity) terminal).getString(R.string.toast_history_is_empty),
                Toast.LENGTH_SHORT).show();
    }

    private void showMultipleObjectsToast() {
        Toast.makeText((Activity) terminal, ((Activity) terminal).getString(R.string.toast_cannot_rename_multiple_objects),
                Toast.LENGTH_SHORT).show();
    }
}
