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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.softsandr.terminal.R;
import com.softsandr.utils.file.FileOpeningUtil;
import com.softsandr.utils.string.StringUtil;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.terminal.activity.terminal.adapter.ListViewAdapter;
import com.softsandr.terminal.activity.terminal.selection.SelectionMonitor;

/**
 * This class represent {@link com.softsandr.terminal.activity.terminal.listener.ListViewItemClickListener}
 * for specified (left or right) panel
 */
public class ListViewItemClickListener implements AdapterView.OnItemClickListener {
    private final SelectionMonitor selectionStrategy;
    private final ListViewAdapter adapter;
    private final ListView listView;
    private final Activity activity;

    public ListViewItemClickListener(Activity activity, ListViewAdapter adapter, ListView listView) {
        this.activity = activity;
        this.adapter = adapter;
        this.listView = listView;
        this.selectionStrategy = adapter.getSelectionMonitor();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // If parent dots clicked go up
        ListViewItem selectedItem = adapter.getItem(position);
        if (selectedItem.isParentDots()) {
            String backPath = adapter.getBackLocation();
            if (backPath != null) {
                selectionStrategy.clear();
                adapter.changeDirectory(backPath);
                listView.smoothScrollToPosition(0);
            }
        } else {
            if (selectionStrategy.isCtrlToggle() ||
                    selectionStrategy.isShiftToggle()) {
                selectionStrategy.addSelection(position);
            } else {
                selectionStrategy.clear();
                if (selectedItem.isDirectory()) {
                    if (selectedItem.canRead()) {
                        if (selectedItem.isLink()) {
                            String[] splitPath = selectedItem.getAbsPath().
                                    substring(1).split(StringUtil.PATH_SEPARATOR);
                            adapter.clearLocationHistory(splitPath);
                            adapter.changeDirectory(splitPath[splitPath.length - 1]);
                        } else {
                            adapter.changeDirectory(selectedItem.getFileName());
                        }
                        listView.smoothScrollToPosition(0);
                    } else {
                        Toast.makeText(activity, activity.getString(R.string.toast_cannot_read_directory_exception) +
                                adapter.getItem(position).getFileName(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    FileOpeningUtil.openFile(activity, selectedItem.getAbsPath());
                }
            }
        }
    }
}
