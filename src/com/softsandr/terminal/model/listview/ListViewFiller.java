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
package com.softsandr.terminal.model.listview;

import android.util.Log;
import com.drk.terminal.util.utils.FileUtil;
import com.drk.terminal.util.utils.StringUtil;
import com.softsandr.terminal.model.filesystem.ProcessDirectory;

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
     * Used predefined sorting strategy from {@link com.softsandr.terminal.model.listview.ListViewSortingStrategy}.
     * After processing data is not sorting in resulting list.
     * @param sortingStrategy   the enum type that determine variant of sorting field
     * @param filesList         the result list of data
     * @param path              the target location path
     */
    public static void fillListContent(final ListViewSortingStrategy sortingStrategy, final List<ListViewItem> filesList, final String path) {
        new ProcessDirectory(new ProcessDirectory.ProcessDirectoryStrategy() {

            @Override
            public void initParentPath(String parentPath) {
                ListViewItem firstItem = new ListViewItem(sortingStrategy, StringUtil.PARENT_DOTS,
                        ListViewItem.UP_LINK_DEF_SIZE, 0l).setIsDirectory(true).setAbsPath(parentPath);
                filesList.add(firstItem);
            }

            @Override
            public void processDirectory(File file) {
                try {
                    boolean isSymLink = FileUtil.isSymlink(path, file);
                    ListViewItem item = new ListViewItem(
                            sortingStrategy,
                            isSymLink ?
                                    StringUtil.DIRECTORY_LINK_PREFIX +
                                            file.getName() :
                                    StringUtil.PATH_SEPARATOR +
                                            file.getName(),
                            ListViewItem.DIRECTORY_DEF_SIZE,
                            file.lastModified()).
                            setAbsPath(file.getAbsolutePath()).
                            setCanRead(file.canRead()).
                            setIsDirectory(file.isDirectory()).
                            setIsLink(isSymLink);
                    filesList.add(item);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepareLeftList", e);
                }
            }

            @Override
            public void processFile(File file) {
                try {
                    boolean isSymLink = FileUtil.isSymlink(path, file);
                    ListViewItem item = new ListViewItem(
                            sortingStrategy,
                             isSymLink?
                                    StringUtil.FILE_LINK_PREFIX +
                                            file.getName() :
                                    file.getName(),
                            file.length(),
                            file.lastModified()).
                            setAbsPath(file.getAbsolutePath()).
                            setCanRead(file.canRead()).
                            setIsDirectory(file.isDirectory()).
                            setIsLink(isSymLink);
                    filesList.add(item);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepareLeftList", e);
                }
            }
        }, "").start(path);
        Collections.sort(filesList);
    }
}
