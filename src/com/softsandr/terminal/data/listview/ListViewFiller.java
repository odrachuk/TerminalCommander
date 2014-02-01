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
package com.softsandr.terminal.data.listview;

import android.util.Log;
import com.softsandr.utils.file.FileUtil;
import com.softsandr.utils.string.StringUtil;
import com.softsandr.terminal.data.filesystem.ProcessDirectory;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * This class contain public method that unify processing directory and filling List of directory's items
 */
public class ListViewFiller {
    private static final String LOG_TAG = ListViewFiller.class.getSimpleName();

    /**
     * Processing target path and collect all found elements to list.
     * Used predefined sorting strategy from {@link com.softsandr.terminal.data.listview.ListViewSortingStrategy}.
     * After processing data is not sorting in resulting list.
     * @param sortingStrategy   the enum type that determine variant of sorting field
     * @param filesList         the result list of data
     * @param path              the target location path
     */
    public static void fillListContent(final ListViewSortingStrategy sortingStrategy, final List<ListViewItem> filesList, final String path) {
        new ProcessDirectory(new ProcessDirectory.ProcessDirectoryStrategy() {

            @Override
            public void initParentPath(File root) {
                ListViewItem item = new ListViewItem()
                        .setFileName(StringUtil.PARENT_DOTS)
                        .setAbsPath(root.getAbsolutePath())
                        .setCanRead(true)
                        .setCanWrite(root.canWrite())
                        .setCanExecute(true)
                        .setFileSize(ListViewItem.UP_LINK_DEF_SIZE)
                        .setFileModifyTime(root.lastModified())
                        .setIsDirectory(true)
                        .setSortingStrategy(sortingStrategy)
                        .setIsLink(false);
                filesList.add(item);
            }

            @Override
            public void processDirectory(File file) {
                try {
                    boolean isSymLink = FileUtil.isSymlink(path, file);
                    ListViewItem item = new ListViewItem()
                            .setFileName(isSymLink ?
                                    StringUtil.DIRECTORY_LINK_PREFIX +
                                            file.getName() :
                                    StringUtil.PATH_SEPARATOR +
                                            file.getName())
                            .setAbsPath(file.getAbsolutePath())
                            .setCanRead(file.canRead())
                            .setCanWrite(file.canWrite())
                            .setCanExecute(file.canExecute())
                            .setFileSize(ListViewItem.DIRECTORY_DEF_SIZE)
                            .setFileModifyTime(file.lastModified())
                            .setIsDirectory(file.isDirectory())
                            .setSortingStrategy(sortingStrategy)
                            .setIsLink(isSymLink);
                    filesList.add(item);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepareLeftList", e);
                }
            }

            @Override
            public void processFile(File file) {
                try {
                    boolean isSymLink = FileUtil.isSymlink(path, file);

                    ListViewItem item = new ListViewItem()
                            .setFileName(isSymLink?
                                    StringUtil.FILE_LINK_PREFIX +
                                            file.getName() :
                                    file.getName())
                            .setAbsPath(file.getAbsolutePath())
                            .setCanRead(file.canRead())
                            .setCanWrite(file.canWrite())
                            .setCanExecute(file.canExecute())
                            .setFileSize(file.length())
                            .setFileModifyTime(file.lastModified())
                            .setIsDirectory(false)
                            .setSortingStrategy(sortingStrategy)
                            .setIsLink(isSymLink);
                    filesList.add(item);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepareLeftList", e);
                }
            }
        }, "").start(path);
        Collections.sort(filesList);
    }
}
