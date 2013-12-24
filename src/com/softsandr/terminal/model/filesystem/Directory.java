package com.softsandr.terminal.model.filesystem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
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
