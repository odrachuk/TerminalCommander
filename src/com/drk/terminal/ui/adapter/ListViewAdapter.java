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
import com.drk.terminal.ui.activity.terminal.CurrentPathLabel;
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
    private final CurrentPathLabel pathLabel;
    private final List<ListViewItem> filesInfo;
    private final LinkedList<String> pathStack;
    private Map<Integer, ViewHolder> cache;
    private final Activity activity;
    private boolean inFirst = true;
    private String labelPath;

    private final Handler notifyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            notifyDataSetChanged();
            pathLabel.setPath(labelPath);
        }
    };

    public ListViewAdapter(Activity activity,
                           List<ListViewItem> filesInfo,
                           CurrentPathLabel pathLabel) {
        super(activity, R.layout.terminal_list_row_layout, filesInfo);
        this.activity = activity;
        this.filesInfo = filesInfo;
        this.pathLabel = pathLabel;
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
        labelPath = path;
        ListViewFiller.fillingList(filesInfo, path, notifyHandler);
    }

    private void initCache(ViewGroup parent) {
        cache = new LinkedHashMap<Integer, ViewHolder>(getCount());
        for (int i = 0; i < getCount(); i++) {
            ViewHolder viewHolder = new ViewHolder();
            View rowView = activity.getLayoutInflater().inflate(R.layout.terminal_list_row_layout, parent, false);
            viewHolder.view = rowView;
            viewHolder.fileNameView = (TextView) rowView.findViewById(R.id.file_name);
            viewHolder.fileSizeView = (TextView) rowView.findViewById(R.id.file_size);
            viewHolder.fileModifyTimeView = (TextView) rowView.findViewById(R.id.file_modify_time);
            ListViewItem info = filesInfo.get(i);
            viewHolder.fileNameView.setText(info.getFileName());
            viewHolder.fileSizeView.setText(info.getFileSize());
            viewHolder.fileModifyTimeView.setText(info.getFileModifyTime());
            if (!info.isDirectory()) {
                viewHolder.fileNameView.setTextColor(activity.getResources().getColor(R.color.COLOR_b2b2b2));
                viewHolder.fileSizeView.setTextColor(activity.getResources().getColor(R.color.COLOR_b2b2b2));
                viewHolder.fileModifyTimeView.setTextColor(activity.getResources().getColor(R.color.COLOR_b2b2b2));
            }
            cache.put(i, viewHolder);
        }
        inFirst = false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inFirst) {
            initCache(parent);
        }
        ViewHolder viewHolder = cache.get(position);
        if (selectionStrategy.getSelectedItems().contains(position)) {
            viewHolder.fileNameView.setTextColor(
                    activity.getResources().getColor(R.color.COLOR_FFFF00));
            viewHolder.fileSizeView.setTextColor(
                    activity.getResources().getColor(R.color.COLOR_FFFF00));
            viewHolder.fileModifyTimeView.setTextColor(
                    activity.getResources().getColor(R.color.COLOR_FFFF00));
        }
        if (selectionStrategy.getUnselectedItems().contains(position)) {
            ListViewItem info = filesInfo.get(position);
            if (info.isDirectory()) {
                viewHolder.fileNameView.setTextColor(
                        activity.getResources().getColor(android.R.color.white));
                viewHolder.fileSizeView.setTextColor(
                        activity.getResources().getColor(android.R.color.white));
                viewHolder.fileModifyTimeView.setTextColor(
                        activity.getResources().getColor(android.R.color.white));
            } else {
                viewHolder.fileNameView.setTextColor(
                        activity.getResources().getColor(R.color.COLOR_b2b2b2));
                viewHolder.fileSizeView.setTextColor(
                        activity.getResources().getColor(R.color.COLOR_b2b2b2));
                viewHolder.fileModifyTimeView.setTextColor(
                        activity.getResources().getColor(R.color.COLOR_b2b2b2));
            }
        }
        return viewHolder.view;
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

    public CurrentPathLabel getPathLabel() {
        return pathLabel;
    }

    class ViewHolder {
        View view;
        TextView fileNameView;
        TextView fileSizeView;
        TextView fileModifyTimeView;
    }
}
