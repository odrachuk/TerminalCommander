package com.drk.terminal.model.filesystem;

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

    public static DirectoryTree walkDir(String start, String regex) {
        return readDirFiles(new File(start), regex);
    }

    public static DirectoryTree walkDir(String start) { // Overloaded
        return readDirFiles(new File(start), ".*");
    }

    static DirectoryTree readDirFiles(File startDir, String regex) {
        DirectoryTree result = new DirectoryTree();
        if (startDir != null) {
            File[] files = startDir.listFiles();
            if (files != null) {
                for (File item : startDir.listFiles()) {
                    if (item.isDirectory()) {
                        result.dirs.add(item);
                    } else {
                        if (item.getName().matches(regex)) {
                            result.files.add(item);
                        }
                    }
                }
            } else {
                // todo empty filesystem or link
            }
        }
        return result;
    }
}
