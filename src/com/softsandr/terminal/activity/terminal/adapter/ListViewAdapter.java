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
package com.softsandr.terminal.activity.terminal.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.softsandr.terminal.activity.terminal.TerminalActivityImpl;
import com.softsandr.utils.string.StringUtil;
import com.softsandr.terminal.R;
import com.softsandr.terminal.model.listview.ListViewFiller;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.terminal.model.preferences.HistoryLocationsManager;
import com.softsandr.terminal.activity.terminal.LocationLabel;
import com.softsandr.terminal.activity.terminal.selection.SelectionMonitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The {@link android.widget.ArrayAdapter} for main lists
 */
public class ListViewAdapter extends ArrayAdapter<ListViewItem> {
    private final HistoryLocationsManager historyLocationsManager;
    private final LinkedList<String> locationHistoryStack;
    private final SelectionMonitor selectionMonitor;
    private final TerminalActivityImpl terminalActivity;
    private final LocationLabel locationLabel;

    public ListViewAdapter(TerminalActivityImpl activity,
                           List<ListViewItem> filesInfo,
                           LocationLabel pathLabel,
                           HistoryLocationsManager historyLocationsManager) {
        super(activity, R.layout.terminal_list_row_layout, filesInfo);
        this.terminalActivity = activity;
        this.locationLabel = pathLabel;
        this.selectionMonitor = new SelectionMonitor(this);
        this.historyLocationsManager = historyLocationsManager;
        locationHistoryStack = new LinkedList<String>();
        locationHistoryStack.add(StringUtil.PATH_SEPARATOR);
    }

    /**
     * Process change directory
     * @param path The new directory location path
     */
    public void changeDirectory(String path) {
        if (path.equals(StringUtil.PATH_SEPARATOR)) {
            locationHistoryStack.clear();
            locationHistoryStack.add(StringUtil.PATH_SEPARATOR);
        } else {
            String prevPath = locationHistoryStack.getLast();
            if (!prevPath.equals(path)) {
                if (path.contains(prevPath)) {
                    locationHistoryStack.addLast(path);
                } else {
                    String correctPath = path.startsWith(StringUtil.PATH_SEPARATOR) ?
                            path.substring(1) : path;
                    locationHistoryStack.addLast(prevPath.equals(StringUtil.PATH_SEPARATOR) ?
                            prevPath + correctPath :
                            prevPath + StringUtil.PATH_SEPARATOR + correctPath);
                }
            }
        }
        // update filesystem
        clear();
        locationLabel.setPath(locationHistoryStack.getLast());
        historyLocationsManager.addLocation(locationLabel.getPath());
        List<ListViewItem> list = new ArrayList<ListViewItem>();
        ListViewFiller.fillListContent(terminalActivity.getSortingStrategy(), list, locationHistoryStack.getLast());
        addAll(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = terminalActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.terminal_list_row_layout, parent, false);
            if (convertView != null) {
                // set up the ViewHolder
                viewHolder = new ViewHolder();
                viewHolder.fileNameView = (TextView) convertView.findViewById(R.id.file_name);
                viewHolder.fileSizeView = (TextView) convertView.findViewById(R.id.file_size);
                viewHolder.fileModifyTimeView = (TextView) convertView.findViewById(R.id.file_modify_time);
                // store the holder with the view
                convertView.setTag(viewHolder);
            }
        } else {
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ListViewItem info = getItem(position);
        // assign values if the object is not null
        if(info != null && viewHolder != null) {
            viewHolder.fileNameView.setText(info.getFileName());
            viewHolder.fileSizeView.setText(info.getFileSize());
            viewHolder.fileModifyTimeView.setText(info.getFileModifyTime());
            // determine files types and their text colors
            if (info.isDirectory()) {
                viewHolder.fileNameView.setTextColor(ListViewItemColor.DIRECTORY.getColor(terminalActivity.getResources()));
                viewHolder.fileSizeView.setTextColor(ListViewItemColor.DIRECTORY.getColor(terminalActivity.getResources()));
                viewHolder.fileModifyTimeView.setTextColor(ListViewItemColor.DIRECTORY.getColor(terminalActivity.getResources()));
                // determine directory that we can't read
                if (!info.isParentDots() && !info.canRead()) {
                    viewHolder.fileNameView.setTextColor(ListViewItemColor.ROOT_DIRECTORY.getColor(terminalActivity.getResources()));
                }
            } else {
                viewHolder.fileNameView.setTextColor(ListViewItemColor.FILE.getColor(terminalActivity.getResources()));
                viewHolder.fileSizeView.setTextColor(ListViewItemColor.FILE.getColor(terminalActivity.getResources()));
                viewHolder.fileModifyTimeView.setTextColor(ListViewItemColor.FILE.getColor(terminalActivity.getResources()));
            }
            checkSelection(viewHolder, info, position);
        }
        return convertView;
    }

    /**
     * Processing selection and make selected items in yellow and unselected in default color
     * @param viewHolder    The row view holder
     * @param info          The row instance of {@link com.softsandr.terminal.model.listview.ListViewItem}
     * @param position      the row number (index of position) in ListView
     */
    private void checkSelection(ViewHolder viewHolder, ListViewItem info, int position) {
        if (selectionMonitor.getSelectedItems().contains(position)) {
            viewHolder.fileNameView.setTextColor(ListViewItemColor.SELECTED.getColor(terminalActivity.getResources()));
            viewHolder.fileSizeView.setTextColor(ListViewItemColor.SELECTED.getColor(terminalActivity.getResources()));
            viewHolder.fileModifyTimeView.setTextColor(ListViewItemColor.SELECTED.getColor(terminalActivity.getResources()));
        }
        if (selectionMonitor.getUnselectedItems().contains(position)) {
            if (info.isDirectory()) {
                viewHolder.fileNameView.setTextColor(ListViewItemColor.DIRECTORY.getColor(terminalActivity.getResources()));
                viewHolder.fileSizeView.setTextColor(ListViewItemColor.DIRECTORY.getColor(terminalActivity.getResources()));
                viewHolder.fileModifyTimeView.setTextColor(ListViewItemColor.DIRECTORY.getColor(terminalActivity.getResources()));
            } else {
                viewHolder.fileNameView.setTextColor(ListViewItemColor.FILE.getColor(terminalActivity.getResources()));
                viewHolder.fileSizeView.setTextColor(ListViewItemColor.FILE.getColor(terminalActivity.getResources()));
                viewHolder.fileModifyTimeView.setTextColor(ListViewItemColor.FILE.getColor(terminalActivity.getResources()));
            }
        }
    }

    /**
     * Make clearing and start initialization of history location stack
     * @param newBackPathInArray    The initial history stack
     */
    public void clearLocationHistory(String[] newBackPathInArray) {
        locationHistoryStack.clear();
        locationHistoryStack.add(StringUtil.PATH_SEPARATOR);
        for (int i = 0; i < newBackPathInArray.length - 1; i++) {
            if (locationHistoryStack.getLast().equals(StringUtil.PATH_SEPARATOR)) {
                locationHistoryStack.add(locationHistoryStack.getLast() + newBackPathInArray[i]);
            } else {
                locationHistoryStack.add(locationHistoryStack.getLast() + StringUtil.PATH_SEPARATOR + newBackPathInArray[i]);
            }
        }
    }

    /**
     * Return actual previous location from history location
     * @return  String of previous location
     */
    public String getBackLocation() {
        locationHistoryStack.removeLast();
        String last = StringUtil.PATH_SEPARATOR;
        try {
            last = locationHistoryStack.getLast();
        } catch (NoSuchElementException ignored) {
        }
        return last;
    }

    /**
     * Delete last path from history location if argument path not equal the path
     * @param backPath  New last location in history location
     */
    public void makeBackHistory(String backPath) {
        while (!locationHistoryStack.getLast().equals(backPath)) {
            locationHistoryStack.removeLast();
        }
    }

    /**
     * Recreate history locations from SavedInstance string
     * @param listSavedLocation The saved location string
     */
    public void restoreHistoryLocation(String listSavedLocation) {
        locationHistoryStack.clear();
        locationHistoryStack.add(StringUtil.PATH_SEPARATOR);
        if (listSavedLocation.length() > 1) {
            String correctSavedListLocation = listSavedLocation.endsWith(StringUtil.PATH_SEPARATOR) ?
                    listSavedLocation.substring(0, listSavedLocation.length() - 1) : listSavedLocation;
            String[] pathArray = correctSavedListLocation.split(StringUtil.PATH_SEPARATOR);
            locationHistoryStack.addLast(locationHistoryStack.getLast() + pathArray[1]);
            for (int i = 2; i < pathArray.length; i++) {
                locationHistoryStack.addLast(locationHistoryStack.getLast() + StringUtil.PATH_SEPARATOR + pathArray[i]);
            }
            locationLabel.setPath(correctSavedListLocation);
        }
    }

    /**
     * The panel selection monitor {@link com.softsandr.terminal.activity.terminal.selection.SelectionMonitor}
     * @return The specific selection state monitor
     */
    public SelectionMonitor getSelectionMonitor() {
        return selectionMonitor;
    }

    /**
     * Transform selection set to ArrayList
     * @return {@link java.util.ArrayList} of selected items
     */
    public ArrayList<ListViewItem> getSelectedList() {
        ArrayList<ListViewItem> selectedItems = new ArrayList<ListViewItem>();
        for (Integer selectedPosition : selectionMonitor.getSelectedItems()) {
            selectedItems.add(getItem(selectedPosition));
        }
        return selectedItems;
    }

    public LocationLabel getLocationLabel() {
        return locationLabel;
    }

    /**
     * Clear selection monitor anf notifyDataSetChanged
     */
    public void clearSelection() {
        selectionMonitor.makeClearSelected();
        notifyDataSetChanged();
    }

    /**
     * Container of ui elements from one row of List
     */
    private final class ViewHolder {
        TextView fileNameView;
        TextView fileSizeView;
        TextView fileModifyTimeView;
    }
}
