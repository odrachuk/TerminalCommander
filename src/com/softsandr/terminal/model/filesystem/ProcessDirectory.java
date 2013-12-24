package com.softsandr.terminal.model.filesystem;

import android.util.Log;
import com.drk.terminal.util.utils.StringUtil;

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
}
