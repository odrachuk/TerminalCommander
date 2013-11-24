package com.drk.terminal.model.filesystem;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class ProcessDirectory {
    private static final String LOG_TAG = ProcessDirectory.class.getSimpleName();
    private ProcessDirectoryStrategy strategy;
    private String ext;

    public interface ProcessDirectoryStrategy {
        void initParentPath(String parentPath);
        void processDirectory(File file);
        void processFile(File file);
    }

    public ProcessDirectory(ProcessDirectoryStrategy strategy, String ext) {
        this.strategy = strategy;
        this.ext = ext;
    }

    public void start(String path) {
        try {
            processDirectoryTree(new File(path));
        } catch (IOException e) {
            Log.e(LOG_TAG, "start", e);
        }
    }

    public void processDirectoryTree(File root) throws IOException {
        DirectoryTree treeInfo = Directory.walkDir(
                root.getAbsolutePath(), ".*" + ext);
        // init parent dots
        strategy.initParentPath(root.getAbsolutePath());
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
}
