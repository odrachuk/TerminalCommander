package com.drk.terminal.model.listview;

import android.os.Handler;
import android.util.Log;
import com.drk.terminal.model.filesystem.ProcessDirectory;
import com.drk.terminal.utils.DirectoryUtil;
import com.drk.terminal.utils.StringUtil;

import java.io.File;
import java.io.IOException;
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

    public static void fillingList(final List<ListViewItem> filesList, final String path, Handler notifyHandler) {
        ListViewItem firstItem = new ListViewItem(StringUtil.PARENT_DOTS, -1, 0l).setIsDirectory(true);
        filesList.add(firstItem);
        new ProcessDirectory(new ProcessDirectory.ProcessDirectoryStrategy() {
            @Override
            public void processDirectory(File file) {
                try {
                    ListViewItem item = new ListViewItem(
                            DirectoryUtil.isSymlink(path, file) ?
                                    StringUtil.DIRECTORY_LINK_PREFIX +
                                            file.getName():
                                    StringUtil.PATH_SEPARATOR +
                                            file.getName(),
                            file.getUsableSpace(),
                            file.lastModified()).
                            setAbsPath(file.getAbsolutePath()).
                            setCanRead(file.canRead()).
                            setIsDirectory(file.isDirectory()).
                            setIsLink(DirectoryUtil.isSymlink(path, file));
                    filesList.add(item);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepareLeftList", e);
                }
            }

            @Override
            public void processFile(File file) {
                try {
                    ListViewItem item = new ListViewItem(
                            DirectoryUtil.isSymlink(path, file) ?
                                    StringUtil.FILE_LINK_PREFIX +
                                            file.getName():
                                    file.getName(),
                            file.getUsableSpace(),
                            file.lastModified()).
                            setAbsPath(file.getAbsolutePath()).
                            setCanRead(file.canRead()).
                            setIsDirectory(file.isDirectory()).
                            setIsLink(DirectoryUtil.isSymlink(path, file));
                    filesList.add(item);
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
