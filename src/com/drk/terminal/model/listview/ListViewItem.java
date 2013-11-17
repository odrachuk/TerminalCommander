package com.drk.terminal.model.listview;

import com.drk.terminal.utils.StringUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/16/13
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListViewItem implements Comparable<ListViewItem> {
    public static final String DATE_FORMAT = "MMM dd yyyy";
    private final String fileName;
    private final String fileSize;
    private final String fileModifyTime;
    private final String parentPath;
    private final boolean canRead;
    private final boolean isDirectory;
    private SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);

    public ListViewItem(boolean isDirectory,
                        boolean canRead,
                        String parentPath,
                        String fileName,
                        long fileSize,
                        long fileModifyTime) {
        this.isDirectory = isDirectory;
        this.canRead = canRead;
        this.parentPath = parentPath;
        this.fileName = fileName;
        this.fileSize = readableFileSize(fileSize);
        this.fileModifyTime = sdf.format(fileModifyTime);
    }

    public static String readableFileSize(long size) {
        if (size < 0) {
            return StringUtil.UP_DIR;
        } else if (size > 0) {
            final String[] units = new String[]{"b", "Kb", "Mb", "Gb", "Tb"};
            int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
            return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        } else {
            return "0 b";
        }
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
    public int compareTo(ListViewItem another) {
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
