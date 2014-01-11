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

import android.view.MenuItem;
import com.softsandr.terminal.model.listview.ListViewSortingStrategy;
import com.softsandr.terminal.activity.terminal.TerminalActivity;

/**
 * This class used for configuring sorting mode of list by user options from main menu
 */
public class SortingMenuItemsMonitor {
    private final MenuItem sortByName;
    private final MenuItem sortBySize;
    private final MenuItem sortByModify;
    private final TerminalActivity terminal;

    public SortingMenuItemsMonitor(TerminalActivity terminal, MenuItem sortByName,
                                   MenuItem sortBySize, MenuItem sortByModify) {
        this.terminal = terminal;
        this.sortByName = sortByName;
        this.sortBySize = sortBySize;
        this.sortByModify = sortByModify;
    }

    public void onMenuSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        if (!menuItem.equals(sortByName)) {
            sortByName.setChecked(false);
        }
        if (!menuItem.equals(sortBySize)) {
            sortBySize.setChecked(false);
        }
        if (!menuItem.equals(sortByModify)) {
            sortByModify.setChecked(false);
        }
        // sorting type
        if (menuItem.equals(sortByName)) {
            terminal.setSortingStrategy(ListViewSortingStrategy.SORT_BY_NAME);
        } else if (menuItem.equals(sortBySize)) {
            terminal.setSortingStrategy(ListViewSortingStrategy.SORT_BY_SIZE);
        } else if (menuItem.equals(sortByModify)) {
            terminal.setSortingStrategy(ListViewSortingStrategy.SORT_BY_DATE);
        }
        terminal.getLeftListAdapter().changeDirectory(terminal.getLeftListAdapter().getLocationLabel().getPath());
        terminal.getRightListAdapter().changeDirectory(terminal.getRightListAdapter().getLocationLabel().getPath());
        terminal.getLeftListAdapter().getSelectionMonitor().clear();
        terminal.getRightListAdapter().getSelectionMonitor().clear();
    }
}
