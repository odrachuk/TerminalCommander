package com.softsandr.terminal.model.listview;

import android.util.Log;
import com.softsandr.terminal.model.filesystem.ProcessDirectory;
import com.drk.terminal.util.utils.FileUtil;
import com.drk.terminal.util.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class ListViewFiller {
    private static final String LOG_TAG = ListViewFiller.class.getSimpleName();

    public static void fillingList(final List<ListViewItem> filesList, final String path) {
        new ProcessDirectory(new ProcessDirectory.ProcessDirectoryStrategy() {

            @Override
            public void initParentPath(String parentPath) {
                ListViewItem firstItem = new ListViewItem(StringUtil.PARENT_DOTS,
                        ListViewItem.UP_LINK_DEF_SIZE, 0l).setIsDirectory(true).setAbsPath(parentPath);
                filesList.add(firstItem);
            }

            @Override
            public void processDirectory(File file) {
                try {
                    ListViewItem item = new ListViewItem(
                            FileUtil.isSymlink(path, file) ?
                                    StringUtil.DIRECTORY_LINK_PREFIX +
                                            file.getName() :
                                    StringUtil.PATH_SEPARATOR +
                                            file.getName(),
                            ListViewItem.DIRECTORY_DEF_SIZE,
                            file.lastModified()).
                            setAbsPath(file.getAbsolutePath()).
                            setCanRead(file.canRead()).
                            setIsDirectory(file.isDirectory()).
                            setIsLink(FileUtil.isSymlink(path, file));
                    filesList.add(item);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepareLeftList", e);
                }
            }

            @Override
            public void processFile(File file) {
                try {
                    ListViewItem item = new ListViewItem(
                            FileUtil.isSymlink(path, file) ?
                                    StringUtil.FILE_LINK_PREFIX +
                                            file.getName() :
                                    file.getName(),
                            file.length(),
                            file.lastModified()).
                            setAbsPath(file.getAbsolutePath()).
                            setCanRead(file.canRead()).
                            setIsDirectory(file.isDirectory()).
                            setIsLink(FileUtil.isSymlink(path, file));
                    filesList.add(item);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepareLeftList", e);
                }
            }
        }, "").start(path);
        Collections.sort(filesList);
    }
}
