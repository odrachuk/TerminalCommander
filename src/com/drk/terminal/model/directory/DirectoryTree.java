package com.drk.terminal.model.directory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/16/13
 * Time: 2:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryTree {
    public List<File> files = new ArrayList<File>();
    public List<File> dirs = new ArrayList<File>();

    public Iterator<File> getFilesIterator() {
        return files.iterator();
    }

    public Iterator<File> getDirsIterator() {
        return dirs.iterator();
    }

    void addAll(DirectoryTree other) {
        files.addAll(other.files);
        dirs.addAll(other.dirs);
    }

    public String toString() {
        return "dirs: " + dirs + "\n\nfiles:" + files;
    }
}
