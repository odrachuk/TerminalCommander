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
package com.softsandr.terminal.ui.activity.terminal;

import android.content.res.Resources;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.terminal.model.listview.ListViewSortingStrategy;
import com.softsandr.terminal.model.preferences.HistoryLocationsManager;
import com.softsandr.terminal.ui.activity.terminal.adapter.ListViewAdapter;
import com.softsandr.terminal.ui.activity.terminal.monitor.ActionBarToggleMonitor;
import com.softsandr.terminal.ui.activity.terminal.selection.SelectionUiComponents;

import java.util.ArrayList;

/**
 * This class used for specify interface of Terminal activity
 */
public interface Terminal {
    /**
     * Return prepared {@link ListViewAdapter} of left panel
     * @return instance of {@link ListViewAdapter}
     */
    ListViewAdapter getLeftListAdapter();

    /**
     * Used for init left panel adapter
     * @param adapter an {@link ListViewAdapter}
     */
    void setLeftListAdapter(ListViewAdapter adapter);

    /**
     * Return prepared {@link ListViewAdapter} of right panel
     * @return instance of {@link ListViewAdapter}
     */
    ListViewAdapter getRightListAdapter();

    /**
     * Used for init right panel adapter
     * @param adapter an {@link ListViewAdapter}
     */
    void setRightListAdapter(ListViewAdapter adapter);

    /**
     * Return current activity sorting mode from possible in {@link ListViewSortingStrategy}
     * @return enum constant
     */
    ListViewSortingStrategy getSortingStrategy();

    /**
     * Set current activity sorting mode as enum constant from {@link ListViewSortingStrategy}
     * @param sortingStrategy an constant {@link ListViewSortingStrategy}
     */
    void setSortingStrategy(ListViewSortingStrategy sortingStrategy);

    /**
     * Return {@link android.content.res.Resources} from Activity context
     * @return {@link android.content.res.Resources}
     */
    Resources getContextResources();

    /**
     * Return left instance of {@link HistoryLocationsManager}
     * @return {@link HistoryLocationsManager} for left panel
     */
    HistoryLocationsManager getLeftHistoryLocationManager();

    /**
     * Return right instance of {@link HistoryLocationsManager}
     * @return {@link HistoryLocationsManager} for right panel
     */
    HistoryLocationsManager getRightHistoryLocationManager();

    /**
     * Return String location for right panel
     * @return String path
     */
    String getRightListSavedLocation();


    /**
     * Return String location for left panel
     * @return String path
     */
    String getLeftListSavedLocation();

    /**
     * Return List of all current files from selected panel
     * @return {@link java.util.ArrayList} of {@link ListViewItem}
     */
    ArrayList<ListViewItem> getOperationItems();

    /**
     * Return {@link com.softsandr.terminal.ui.activity.terminal.selection.SelectionUiComponents}
     * from terminal activity instance
     * @return {@link com.softsandr.terminal.ui.activity.terminal.selection.SelectionUiComponents}
     */
    SelectionUiComponents getSelectionVisualItems();

    /**
     * Determine current active page as {@link com.softsandr.terminal.ui.activity.terminal.TerminalActivePage}
     * @param activePage an constant from {@link com.softsandr.terminal.ui.activity.terminal.TerminalActivePage}
     */
    void setActivePage(TerminalActivePage activePage);

    /**
     * Return active page as instance of {@link com.softsandr.terminal.ui.activity.terminal.TerminalActivePage}
     * @return current active page
     */
    TerminalActivePage getActivePage();

    /**
     * Return instance of {@link com.softsandr.terminal.ui.activity.terminal.monitor.ActionBarToggleMonitor}
     * @return {@link com.softsandr.terminal.ui.activity.terminal.monitor.ActionBarToggleMonitor}
     * from terminal activity.
     */
    ActionBarToggleMonitor getActionBarToggleMonitor();
}
