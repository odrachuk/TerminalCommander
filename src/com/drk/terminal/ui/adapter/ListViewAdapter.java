package com.drk.terminal.ui.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.drk.terminal.R;
import com.drk.terminal.model.filesystem.ProcessDirectory;
import com.drk.terminal.model.listview.ListViewItem;
import com.drk.terminal.utils.DirectoryUtil;
import com.drk.terminal.utils.StringUtil;

import java.io.File;
import java.io.IOException;
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
    private final List<ListViewItem> filesInfo;
    private final Activity activity;
    private Map<Integer, View> cache;
    private boolean inFirst = true;
    private LinkedList<String> pathStack;

    public ListViewAdapter(Activity activity, List<ListViewItem> filesInfo) {
        super(activity, R.layout.terminal_list_row_layout, filesInfo);
        this.activity = activity;
        this.filesInfo = filesInfo;
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
        // show progress
        ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Data downloading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        // update filesystem
        inFirst = true;
        cache.clear();
        filesInfo.clear();
        filesInfo.add(new ListViewItem(true, true, StringUtil.PARENT_DOTS,
                -1, 0l));
        new ProcessDirectory(new ProcessDirectory.ProcessDirectoryStrategy() {
            @Override
            public void processDirectory(File file) {
                try {
                    filesInfo.add(new ListViewItem(
                            true,
                            file.canRead(),
                            DirectoryUtil.isSymlink(file) ?
                                    StringUtil.DIRECTORY_LINK_PREFIX +
                                            file.getName() :
                                    StringUtil.PATH_SEPARATOR +
                                            file.getName(),
                            file.getUsableSpace(),
                            file.lastModified()));
                } catch (IOException e) {
                    Log.d(LOG_TAG, "changeDirectory", e);
                }
            }

            @Override
            public void processFile(File file) {
                try {
                    filesInfo.add(new ListViewItem(
                            false,
                            file.canRead(),
                            DirectoryUtil.isSymlink(file) ?
                                    StringUtil.FILE_LINK_PREFIX +
                                            file.getName() :
                                    file.getName(),
                            file.getUsableSpace(),
                            file.lastModified()));
                } catch (IOException e) {
                    Log.d(LOG_TAG, "changeDirectory", e);
                }
            }
        }, "").start(path);
        Collections.sort(filesInfo);
        notifyDataSetChanged();
        progressBar.dismiss();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inFirst) {
            initCache(parent);
        }
        return cache.get(position);
    }

    private void initCache(ViewGroup parent) {
        cache = new HashMap<Integer, View>(getCount());
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
                fileNameView.setTextColor(getContext().getResources().getColor(R.color.COLOR_b2b2b2));
                fileSizeView.setTextColor(getContext().getResources().getColor(R.color.COLOR_b2b2b2));
                fileModifyTimeView.setTextColor(getContext().getResources().getColor(R.color.COLOR_b2b2b2));
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
}
