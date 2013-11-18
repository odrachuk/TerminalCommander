package com.drk.terminal.ui.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.drk.terminal.R;
import com.drk.terminal.model.listview.ListViewFiller;
import com.drk.terminal.model.listview.ListViewItem;
import com.drk.terminal.ui.activity.terminal.SelectionStrategy;
import com.drk.terminal.utils.StringUtil;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/16/13
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListViewAdapter extends ArrayAdapter<ListViewItem> {
    private static final String LOG_TAG = ListViewAdapter.class.getSimpleName();
    private final SelectionStrategy selectionStrategy;
    private final List<ListViewItem> filesInfo;
    private final Activity activity;
    private Map<Integer, View> cache;
    private boolean inFirst = true;
    private LinkedList<String> pathStack;

    private final Handler notifyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            notifyDataSetChanged();
        }
    };

    public ListViewAdapter(Activity activity,
                           List<ListViewItem> filesInfo) {
        super(activity, R.layout.terminal_list_row_layout, filesInfo);
        this.activity = activity;
        this.filesInfo = filesInfo;
        this.selectionStrategy = new SelectionStrategy(this);
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
                pathStack.addLast(prevPath.equals(StringUtil.PATH_SEPARATOR) ?
                        prevPath + path.substring(1) :
                        prevPath + StringUtil.PATH_SEPARATOR + path.substring(1)); // todo links not correct presentation
            }
        }
        // update filesystem
        inFirst = true;
        filesInfo.clear();
        ListViewFiller.fillingList(filesInfo, path, notifyHandler);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inFirst) {
            initCache(parent);
        }
        View rowView = cache.get(position);
        if (selectionStrategy.getSelectedItems().contains(position)) {
            ((TextView) rowView.findViewById(R.id.file_name)).setTextColor(
                    activity.getResources().getColor(R.color.COLOR_FFFF00));
            ((TextView) rowView.findViewById(R.id.file_size)).setTextColor(
                    activity.getResources().getColor(R.color.COLOR_FFFF00));
            ((TextView) rowView.findViewById(R.id.file_modify_time)).setTextColor(
                    activity.getResources().getColor(R.color.COLOR_FFFF00));
        }
        if (selectionStrategy.getUnselectedItems().contains(position)) {
            ListViewItem info = filesInfo.get(position);
            if (info.isDirectory()) {
                ((TextView) rowView.findViewById(R.id.file_name)).setTextColor(
                        activity.getResources().getColor(android.R.color.white));
                ((TextView) rowView.findViewById(R.id.file_size)).setTextColor(
                        activity.getResources().getColor(android.R.color.white));
                ((TextView) rowView.findViewById(R.id.file_modify_time)).setTextColor(
                        activity.getResources().getColor(android.R.color.white));
            } else {
                ((TextView) rowView.findViewById(R.id.file_name)).setTextColor(
                        activity.getResources().getColor(R.color.COLOR_b2b2b2));
                ((TextView) rowView.findViewById(R.id.file_size)).setTextColor(
                        activity.getResources().getColor(R.color.COLOR_b2b2b2));
                ((TextView) rowView.findViewById(R.id.file_modify_time)).setTextColor(
                        activity.getResources().getColor(R.color.COLOR_b2b2b2));
            }
        }
        return rowView;
    }

    private void initCache(ViewGroup parent) {
        cache = new LinkedHashMap<Integer, View>(getCount());
        for (int i = 0; i < getCount(); i++) {
            View rowView = activity.getLayoutInflater().inflate(R.layout.terminal_list_row_layout, parent, false);
            TextView fileNameView = (TextView) rowView.findViewById(R.id.file_name);
            TextView fileSizeView = (TextView) rowView.findViewById(R.id.file_size);
            TextView fileModifyTimeView = (TextView) rowView.findViewById(R.id.file_modify_time);
            ListViewItem info = filesInfo.get(i);
            fileNameView.setText(info.getFileName());
            fileSizeView.setText(info.getFileSize());
            fileModifyTimeView.setText(info.getFileModifyTime());
            if (!info.isDirectory()) {
                fileNameView.setTextColor(activity.getResources().getColor(R.color.COLOR_b2b2b2));
                fileSizeView.setTextColor(activity.getResources().getColor(R.color.COLOR_b2b2b2));
                fileModifyTimeView.setTextColor(activity.getResources().getColor(R.color.COLOR_b2b2b2));
            }
            cache.put(i, rowView);
        }
        inFirst = false;
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

    public SelectionStrategy getSelectionStrategy() {
        return selectionStrategy;
    }
}
