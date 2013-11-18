package com.drk.terminal.model.listview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import com.drk.terminal.model.filesystem.ProcessDirectory;
import com.drk.terminal.ui.adapter.ListViewAdapter;
import com.drk.terminal.utils.DirectoryUtil;
import com.drk.terminal.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/18/13
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListViewFiller {
    private static final String LOG_TAG = ListViewFiller.class.getSimpleName();

    public static void fillingList(final List<ListViewItem> filesList, String path, Handler notifyHandler) {
        filesList.add(new ListViewItem(true, true, StringUtil.PARENT_DOTS,
                -1, 0l));
        new ProcessDirectory(new ProcessDirectory.ProcessDirectoryStrategy() {
            @Override
            public void processDirectory(File file) {
                try {
                    filesList.add(new ListViewItem(
                            true,
                            file.canRead(),
                            DirectoryUtil.isSymlink(file) ?
                                    StringUtil.DIRECTORY_LINK_PREFIX +
                                            file.getName():
                                    StringUtil.PATH_SEPARATOR +
                                            file.getName(),
                            file.getUsableSpace(),
                            file.lastModified()));
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepareLeftList", e);
                }
            }

            @Override
            public void processFile(File file) {
                try {
                    filesList.add(new ListViewItem(
                            false,
                            file.canRead(),
                            DirectoryUtil.isSymlink(file) ?
                                    StringUtil.FILE_LINK_PREFIX +
                                            file.getName():
                                    file.getName(),
                            file.getUsableSpace(),
                            file.lastModified()));
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepareLeftList", e);
                }
            }
        }, "").start(path);
        Collections.sort(filesList);
        if (notifyHandler != null) {
            notifyHandler.sendEmptyMessage(0);
        }
    }
}
