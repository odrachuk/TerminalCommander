package com.drk.terminal.ui;

import com.drk.terminal.utils.StringUtil;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/16/13
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryContentInfo implements Comparable<DirectoryContentInfo> {
    private final String fileName;
    private final String fileSize;
    private final String fileModifyTime;
    private final String parentPath;
    private final boolean canRead;
    private final boolean isDirectory;

    public DirectoryContentInfo(boolean isDirectory,
                                boolean canRead,
                                String parentPath,
                                String fileName,
                                String fileSize,
                                String fileModifyTime) {
        this.isDirectory = isDirectory;
        this.canRead = canRead;
        this.parentPath = parentPath;
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

    public boolean canRead() {
        return canRead;
    }

    public boolean isParentDots() {
        return fileName.equals(StringUtil.PARENT_DOTS);
    }

    public String getParentPath() {
        return parentPath;
    }

    @Override
    public int compareTo(DirectoryContentInfo another) {
        int result = -1;
        String anotherFileName = another.getFileName();
        if (fileName.startsWith(StringUtil.PATH_SEPARATOR) ||
                fileName.startsWith(StringUtil.DIRECTORY_LINK_PREFIX)) {
            if (anotherFileName.startsWith(StringUtil.PATH_SEPARATOR) ||
                    anotherFileName.startsWith(StringUtil.DIRECTORY_LINK_PREFIX)) { // ---- /sd <> /ds or /sd <> @ds
                result = fileName.substring(1).compareTo(anotherFileName.substring(1));
            } else if (anotherFileName.startsWith(StringUtil.FILE_LINK_PREFIX)) {
                result = 1;
            }
        } else if (fileName.startsWith(StringUtil.FILE_LINK_PREFIX)) {
            if (anotherFileName.startsWith(StringUtil.FILE_LINK_PREFIX)) {
                result = fileName.substring(1).compareTo(anotherFileName.substring(1));
            } else if (anotherFileName.startsWith(StringUtil.PATH_SEPARATOR) ||
                    anotherFileName.startsWith(StringUtil.DIRECTORY_LINK_PREFIX)) {
                result = -1;
            }
        } else {
            result = fileName.compareTo(anotherFileName);
        }
        return result;
    }
}
