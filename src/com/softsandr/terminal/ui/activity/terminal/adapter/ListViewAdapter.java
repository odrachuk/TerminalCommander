package com.softsandr.terminal.ui.activity.terminal.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.drk.terminal.util.utils.StringUtil;
import com.softsandr.terminal.R;
import com.softsandr.terminal.model.listview.ListViewFiller;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.terminal.model.preferences.HistoryLocationsManager;
import com.softsandr.terminal.ui.activity.terminal.CurrentPathLabel;
import com.softsandr.terminal.ui.activity.terminal.TerminalActivity;
import com.softsandr.terminal.ui.activity.terminal.selection.SelectionStrategy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class ListViewAdapter extends ArrayAdapter<ListViewItem> {
    private final SelectionStrategy selectionStrategy;
    private final CurrentPathLabel pathLabel;
    private final LinkedList<String> pathStack;
    private final HistoryLocationsManager historyLocationsManager;
    private final TerminalActivity terminalActivity;

    public ListViewAdapter(TerminalActivity activity,
                           List<ListViewItem> filesInfo,
                           CurrentPathLabel pathLabel,
                           HistoryLocationsManager historyLocationsManager) {
        super(activity, R.layout.terminal_list_row_layout, filesInfo);
        this.terminalActivity = activity;
        this.pathLabel = pathLabel;
        this.selectionStrategy = new SelectionStrategy(this);
        this.historyLocationsManager = historyLocationsManager;
        pathStack = new LinkedList<String>();
        pathStack.add(StringUtil.PATH_SEPARATOR);
    }

    public void changeDirectory(String path) {
        if (path.equals(StringUtil.PATH_SEPARATOR)) {
            pathStack.clear();
            pathStack.add(StringUtil.PATH_SEPARATOR);
        } else {
            String prevPath = pathStack.getLast();
            if (!prevPath.equals(path)) {
                if (isContinueForPrev(prevPath, path)) {
                    pathStack.addLast(path);
                } else {
                    String correctPath = path.startsWith(StringUtil.PATH_SEPARATOR) ?
                            path.substring(1) : path;
                    pathStack.addLast(prevPath.equals(StringUtil.PATH_SEPARATOR) ?
                            prevPath + correctPath :
                            prevPath + StringUtil.PATH_SEPARATOR + correctPath);
                }
            }
        }
        // update filesystem
        clear();
        pathLabel.setPath(pathStack.getLast());
        historyLocationsManager.addLocation(pathLabel.getFullPath());
        List<ListViewItem> list = new ArrayList<ListViewItem>();
        ListViewFiller.fillListContent(terminalActivity.getSortingStrategy(), list, pathStack.getLast());
        addAll(list);
    }

    private boolean isContinueForPrev(String prevPath, String newPath) {
        return newPath.contains(prevPath);
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
                viewHolder.fileNameView.setTextColor(terminalActivity.getResources().getColor(android.R.color.white));
                viewHolder.fileSizeView.setTextColor(terminalActivity.getResources().getColor(android.R.color.white));
                viewHolder.fileModifyTimeView.setTextColor(terminalActivity.getResources().getColor(android.R.color.white));
                // determine directory that we can't read
                if (!info.isParentDots() && !info.canRead()) {
                    viewHolder.fileNameView.setTextColor(terminalActivity.getResources().getColor(R.color.COLOR_TRANSLUCENT_WHITE));
                }
            } else {
                viewHolder.fileNameView.setTextColor(terminalActivity.getResources().getColor(R.color.COLOR_B2B2B2));
                viewHolder.fileSizeView.setTextColor(terminalActivity.getResources().getColor(R.color.COLOR_B2B2B2));
                viewHolder.fileModifyTimeView.setTextColor(terminalActivity.getResources().getColor(R.color.COLOR_B2B2B2));
                viewHolder.fileNameView.setPaintFlags(viewHolder.fileNameView.getPaintFlags() |
                        Paint.LINEAR_TEXT_FLAG);
            }
            checkSelection(viewHolder, info, position);
        }
        return convertView;
    }

    private void checkSelection(ViewHolder viewHolder, ListViewItem info, int position) {
        if (selectionStrategy.getSelectedItems().contains(position)) {
            viewHolder.fileNameView.setTextColor(
                    terminalActivity.getResources().getColor(R.color.COLOR_FECE0A));
            viewHolder.fileSizeView.setTextColor(
                    terminalActivity.getResources().getColor(R.color.COLOR_FECE0A));
            viewHolder.fileModifyTimeView.setTextColor(
                    terminalActivity.getResources().getColor(R.color.COLOR_FECE0A));
        }
        if (selectionStrategy.getUnselectedItems().contains(position)) {
            if (info.isDirectory()) {
                viewHolder.fileNameView.setTextColor(
                        terminalActivity.getResources().getColor(android.R.color.white));
                viewHolder.fileSizeView.setTextColor(
                        terminalActivity.getResources().getColor(android.R.color.white));
                viewHolder.fileModifyTimeView.setTextColor(
                        terminalActivity.getResources().getColor(android.R.color.white));
            } else {
                viewHolder.fileNameView.setTextColor(
                        terminalActivity.getResources().getColor(R.color.COLOR_B2B2B2));
                viewHolder.fileSizeView.setTextColor(
                        terminalActivity.getResources().getColor(R.color.COLOR_B2B2B2));
                viewHolder.fileModifyTimeView.setTextColor(
                        terminalActivity.getResources().getColor(R.color.COLOR_B2B2B2));
            }
        }
    }

    public void clearBackPath(String[] newBackPathInArray) {
        pathStack.clear();
        pathStack.add(StringUtil.PATH_SEPARATOR);
        for (int i = 0; i < newBackPathInArray.length - 1; i++) {
            if (pathStack.getLast().equals(StringUtil.PATH_SEPARATOR)) {
                pathStack.add(pathStack.getLast() + newBackPathInArray[i]);
            } else {
                pathStack.add(pathStack.getLast() + StringUtil.PATH_SEPARATOR + newBackPathInArray[i]);
            }
        }
    }

    public String getBackPath() {
        pathStack.removeLast();
        String last = StringUtil.PATH_SEPARATOR;
        try {
            last = pathStack.getLast();
        } catch (NoSuchElementException ignored) {
        }
        return last;
    }

    public void goBackToPath(String backPath) {
        while (!pathStack.getLast().equals(backPath)) {
            pathStack.removeLast();
        }
    }

    public void restoreBackPath(String listSavedLocation) {
        pathStack.clear();
        pathStack.add(StringUtil.PATH_SEPARATOR);
        if (listSavedLocation.length() > 1) {
            String correctSavedListLocation = listSavedLocation.endsWith(StringUtil.PATH_SEPARATOR) ?
                    listSavedLocation.substring(0, listSavedLocation.length() - 1) : listSavedLocation;
            String[] pathArray = correctSavedListLocation.split(StringUtil.PATH_SEPARATOR);
            pathStack.addLast(pathStack.getLast() + pathArray[1]);
            for (int i = 2; i < pathArray.length; i++) {
                pathStack.addLast(pathStack.getLast() + StringUtil.PATH_SEPARATOR + pathArray[i]);
            }
            pathLabel.setPath(correctSavedListLocation);
        }
    }

    public SelectionStrategy getSelectionStrategy() {
        return selectionStrategy;
    }

    public ArrayList<ListViewItem> getSelectedItems() {
        ArrayList<ListViewItem> selectedItems = new ArrayList<ListViewItem>();
        for (Integer selectedPosition : selectionStrategy.getSelectedItems()) {
            selectedItems.add(getItem(selectedPosition));
        }
        return selectedItems;
    }

    public CurrentPathLabel getPathLabel() {
        return pathLabel;
    }

    public void clearSelection() {
        selectionStrategy.makeClearSelected();
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView fileNameView;
        TextView fileSizeView;
        TextView fileModifyTimeView;
    }
}
