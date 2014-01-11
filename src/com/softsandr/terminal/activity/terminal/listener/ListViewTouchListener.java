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

import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.terminal.ActivePage;
import com.softsandr.terminal.activity.terminal.TerminalActivity;

/**
 * This class used for management visual selection of panels
 */
public class ListViewTouchListener implements AbsListView.OnTouchListener {
    private final TerminalActivity terminal;

    public ListViewTouchListener(TerminalActivity terminal) {
        this.terminal = terminal;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Detecting active list
        if (v.getId() == R.id.left_directory_list) {
            terminal.setActivePage(ActivePage.LEFT);
            terminal.getSelectionVisualItems().selectLeft();
        } else if (v.getId() == R.id.right_directory_list) {
            terminal.setActivePage(ActivePage.RIGHT);
            terminal.getSelectionVisualItems().selectRight();
        }
        return false;
    }
}
