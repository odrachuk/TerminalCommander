package com.softsandr.terminal.model.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
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
