package com.drk.terminal.data;

import java.util.regex.*;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/12/13
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
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
        for (File item : startDir.listFiles()) {
            if (item.isDirectory()) {
                // Directory
                result.dirs.add(item);
            } else {
                // Regular file
                if (item.getName().matches(regex)) {
                    result.files.add(item);
                }
            }
        }
        return result;
    }
}
