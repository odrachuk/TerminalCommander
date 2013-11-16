package com.drk.terminal.ui;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/16/13
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryContentInfo {
    private final boolean isDirectory;
    private final String fileName;
    private final String fileSize;
    private final String fileModifyTime;

    public DirectoryContentInfo(boolean isDirectory, String fileName, String fileSize, String fileModifyTime) {
        this.isDirectory = isDirectory;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileModifyTime = fileModifyTime;
    }

    public String getFileModifyTime() {
        return fileModifyTime;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }
}
