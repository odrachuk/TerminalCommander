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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represent directory content
 */
public class DirectoryContent {
    public List<File> files = new ArrayList<File>();
    public List<File> dirs = new ArrayList<File>();

    public Iterator<File> getFilesIterator() {
        return files.iterator();
    }

    public Iterator<File> getDirsIterator() {
        return dirs.iterator();
    }

    void addAll(DirectoryContent other) {
        files.addAll(other.files);
        dirs.addAll(other.dirs);
    }

    public String toString() {
        return "dirs: " + dirs + "\n\nfiles:" + files;
    }
}
