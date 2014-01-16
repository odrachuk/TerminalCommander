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
package com.softsandr.terminal.model.filesystem;

import android.util.Log;
import com.softsandr.utils.string.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class represent processing directories strategy
 */
public class ProcessDirectory {
    private static final String LOG_TAG = ProcessDirectory.class.getSimpleName();
    private ProcessDirectoryStrategy strategy;
    private String fileExtension;

    public interface ProcessDirectoryStrategy {
        void initParentPath(String parentPath);
        void processDirectory(File file);
        void processFile(File file);
    }

    public ProcessDirectory(ProcessDirectoryStrategy strategy, String ext) {
        this.strategy = strategy;
        this.fileExtension = ext;
    }

    public void start(String path) {
        try {
            processDirectoryTree(new File(path));
        } catch (IOException e) {
            Log.e(LOG_TAG, "start", e);
        }
    }

    public void processDirectoryTree(File root) throws IOException {
        DirectoryContent treeInfo = Directory.walkDir(root.getAbsolutePath(), ".*" + fileExtension);
        // init parent dots
        if (!root.getPath().equals(StringUtil.PATH_SEPARATOR)) {
            strategy.initParentPath(root.getAbsolutePath());
        }
        // process all directories
        Iterator<File> dirsIterator = treeInfo.getDirsIterator();
        while (dirsIterator.hasNext()) {
            strategy.processDirectory(dirsIterator.next().getCanonicalFile());
        }
        // process all files
        Iterator<File> filesIterator = treeInfo.getFilesIterator();
        while (filesIterator.hasNext()) {
            strategy.processFile(filesIterator.next().getCanonicalFile());
        }
    }

    /**
     * Read all file objects in specific location
     * @param filesList The prepared list
     * @param path      The location path
     */
    public static void readUserLocation(final List<String> filesList, final String path) {
        new ProcessDirectory(new ProcessDirectory.ProcessDirectoryStrategy() {

            @Override
            public void initParentPath(String parentPath) {
                // ignored
            }

            @Override
            public void processDirectory(File file) {
                filesList.add(file.getName());
            }

            @Override
            public void processFile(File file) {
                filesList.add(file.getName());
            }
        }, "").start(path);
        Collections.sort(filesList);
    }
}
