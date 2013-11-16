package com.drk.terminal.data;

import java.util.regex.*;
import java.io.*;
import java.util.*;

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

    public static TreeInfo walk(String start) {
        return recursiveDirs(new File(start), ".*");
    }

    static TreeInfo recursiveDirs(File startDir, String regex) {
        TreeInfo result = new TreeInfo();
        for (File item : startDir.listFiles()) {
            if (item.isDirectory()) {
                result.dirs.add(item);
                result.addAll(recursiveDirs(item, regex));
            } else { // Regular file
                if (item.getName().matches(regex)) {
                    result.files.add(item);
                }
            }
        }
        return result;
    }

    public static TreeInfo walkDir(String start, String regex) {
        return recurseDir(new File(start), regex);
    }

    public static TreeInfo walkDir(String start) { // Overloaded
        return recurseDir(new File(start), ".*");
    }

    static TreeInfo recurseDir(File startDir, String regex) {
        TreeInfo result = new TreeInfo();
        for (File item : startDir.listFiles()) {
            if (item.isDirectory()) {
                result.dirs.add(item);
            } else { // Regular file
                if (item.getName().matches(regex)) {
                    result.files.add(item);
                }
            }
        }
        return result;
    }

    public static class TreeInfo {
        public List<File> files = new ArrayList<File>();
        public List<File> dirs = new ArrayList<File>();

        public Iterator<File> getFilesIterator() {
            return files.iterator();
        }

        public Iterator<File> getDirsIterator() {
            return dirs.iterator();
        }

        void addAll(TreeInfo other) {
            files.addAll(other.files);
            dirs.addAll(other.dirs);
        }

        public String toString() {
            return "dirs: " + dirs + "\n\nfiles:" + files;
        }
    }
}
