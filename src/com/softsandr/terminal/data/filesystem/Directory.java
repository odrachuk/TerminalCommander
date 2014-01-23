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
package com.softsandr.terminal.data.filesystem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * This class contain public methods for processing directory
 */
public class Directory {

    public static File[] local(File dir, final String regex) {
        return dir.listFiles(new FilenameFilter() {
            private Pattern pattern = Pattern.compile(regex);

            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(new File(name).getName()).matches();
            }
        });
    }

    public static File[] local(String path, final String regex) {
        return local(new File(path), regex);
    }

    public static DirectoryContent walkDir(String start, String regex) {
        return readDirFiles(new File(start), regex);
    }

    public static DirectoryContent walkDir(String start) { // Overloaded
        return readDirFiles(new File(start), ".*");
    }

    static DirectoryContent readDirFiles(File startDir, String regex) {
        DirectoryContent result = new DirectoryContent();
        if (startDir != null) {
            File[] files = startDir.listFiles();
            if (files != null) {
                File[] fileArray = startDir.listFiles();
                if (fileArray != null) {
                    for (File item : fileArray) {
                        if (item.isDirectory()) {
                            result.dirs.add(item);
                        } else {
                            if (item.getName().matches(regex)) {
                                result.files.add(item);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
