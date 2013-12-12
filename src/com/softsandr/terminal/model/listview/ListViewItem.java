package com.softsandr.terminal.model.listview;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.drk.terminal.util.utils.AlphanumComparator;
import com.drk.terminal.util.utils.StringUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class ListViewItem implements Comparable<ListViewItem>, Parcelable {
    private static final String LOG_TAG = ListViewItem.class.getSimpleName();
    public static final String DATE_FORMAT = "MMM dd yyyy";
    public static final long DIRECTORY_DEF_SIZE = -1;
    public static final long UP_LINK_DEF_SIZE = -2;
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
    private final String fileName;
    private final String fileSize;
    private final String fileModifyTime;
    private String absPath;
    private boolean isDirectory;
    private boolean isLink;
    private boolean canRead;
    private SortingStrategy sortingStrategy;

    public ListViewItem(SortingStrategy sortingStrategy,
                        String fileName,
                        long fileSize,
                        long fileModifyTime) {
        this.sortingStrategy = sortingStrategy;
        this.fileName = fileName;
        this.fileSize = readableFileSize(fileSize);
        this.fileModifyTime = sdf.format(fileModifyTime);
    }

    public static String readableFileSize(long size) {
        if (size == UP_LINK_DEF_SIZE) {
            return StringUtil.UP_DIR;
        } else if (size == DIRECTORY_DEF_SIZE) {
            return "dir";
        } else if (size > 0) {
            final String[] units = new String[]{"b", "Kb", "Mb", "Gb", "Tb"};
            int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
            return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        } else {
            return "0 b";
        }
    }

    public static long fileSizeFromReadableString(String size) {
        long longSize = 0l;
        if (size.contains("Tb")) {
            longSize = Math.round(Float.parseFloat(size.substring(0, size.indexOf("T") - 1)) * 1024 * 1024 * 1024 * 1024);
        } else if (size.contains("Gb")) {
            longSize = Math.round(Float.parseFloat(size.substring(0, size.indexOf("G") - 1)) * 1024 * 1024 * 1024);
        } else if (size.contains("Mb")) {
            longSize = Math.round(Float.parseFloat(size.substring(0, size.indexOf("M") - 1)) * 1024 * 1024);
        } else if (size.contains("Kb")) {
            longSize = Math.round(Float.parseFloat(size.substring(0, size.indexOf("K") - 1)) * 1024);
        } else if (size.contains("b")) {
            longSize = Math.round(Float.parseFloat(size.substring(0, size.indexOf("b") - 1)));
        }
        return longSize;
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

    public ListViewItem setIsDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
        return this;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public ListViewItem setCanRead(boolean canRead) {
        this.canRead = canRead;
        return this;
    }

    public boolean canRead() {
        return canRead;
    }

    public ListViewItem setAbsPath(String absPath) {
        this.absPath = absPath;
        return this;
    }

    public String getAbsPath() {
        return absPath;
    }

    public ListViewItem setIsLink(boolean isLink) {
        this.isLink = isLink;
        return this;
    }

    public boolean isLink() {
        return isLink;
    }

    public boolean isParentDots() {
        return fileName.equals(StringUtil.PARENT_DOTS);
    }

    @Override
    public int compareTo(ListViewItem another) {
        switch (sortingStrategy) {
            case DATE:
                return compareModifyTime(another);
            case NAME:
                return compareFileNames(another);
            case SIZE:
                return compareSize(another);
            default:
                return compareFileNames(another);
        }
    }

    private int compareFileNames(ListViewItem another) {
        if (this.isDirectory()) {
            if (another.isDirectory()) {
                return new AlphanumComparator().compare(fileName.substring(1),
                        another.getFileName().substring(1));
            } else {
                return -1;
            }
        } else {
            if (another.isDirectory()) {
                return 1;
            } else if (this.getFileName().startsWith(StringUtil.FILE_LINK_PREFIX)) {
                if (another.getFileName().startsWith(StringUtil.FILE_LINK_PREFIX)) {
                    return new AlphanumComparator().compare(fileName.substring(1),
                            another.getFileName().substring(1));
                } else {
                    return new AlphanumComparator().compare(fileName.substring(1),
                            StringUtil.isStringDigital(another.getFileName()));
                }
            } else {
                return new AlphanumComparator().compare(fileName, another.getFileName());
            }
        }
    }

    private int compareSize(ListViewItem another) {
        return Long.valueOf(fileSizeFromReadableString(fileSize)).
                compareTo(fileSizeFromReadableString(another.fileSize));
    }

    private int compareModifyTime(ListViewItem another) {
        try {
            Date thisDate = sdf.parse(fileModifyTime);
            Date anotherDate = sdf.parse(another.fileModifyTime);
            return thisDate.compareTo(anotherDate);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "compareModifyTime", e);
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!o.getClass().isInstance(ListViewItem.class)) {
            return false;
        }
        ListViewItem other = (ListViewItem) o;
        return !(!fileName.equals(other.fileName) ||
                !fileSize.equals(other.fileSize) ||
                !fileModifyTime.equals(other.fileModifyTime) ||
                !absPath.equals(other.absPath) ||
                isDirectory != other.isDirectory ||
                isLink != other.isLink);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + fileName.hashCode();
        result = 37 * result + fileSize.hashCode();
        result = 37 * result + fileModifyTime.hashCode();
        result = 37 * result + (absPath != null ? absPath.hashCode() : 0);
        result = 37 * result + (isDirectory ? 0 : 1);
        result = 37 * result + (isLink ? 0 : 1);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeString(fileSize);
        dest.writeString(fileModifyTime);
        dest.writeString(absPath);
        dest.writeSerializable(sortingStrategy);
        dest.writeBooleanArray(new boolean[]{isDirectory});
        dest.writeBooleanArray(new boolean[]{isLink});
    }

    private ListViewItem(Parcel parcel) {
        fileName = parcel.readString();
        fileSize = parcel.readString();
        fileModifyTime = parcel.readString();
        absPath = parcel.readString();
        boolean[] isDirectoryRsp = new boolean[1];
        parcel.readBooleanArray(isDirectoryRsp);
        isDirectory = isDirectoryRsp[0];
        boolean[] isLinkRsp = new boolean[1];
        parcel.readBooleanArray(isLinkRsp);
        isDirectory = isLinkRsp[0];
    }

    public static final Creator<ListViewItem> CREATOR = new Creator<ListViewItem>() {
        @Override
        public ListViewItem createFromParcel(Parcel source) {
            return new ListViewItem(source);
        }

        @Override
        public ListViewItem[] newArray(int size) {
            return new ListViewItem[0];
        }
    };

    public enum SortingStrategy {
        NAME,
        SIZE,
        DATE
    }
}
