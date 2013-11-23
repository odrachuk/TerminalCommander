package com.drk.terminal.model.listview;

import android.os.Parcel;
import android.os.Parcelable;
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
public class ListViewItem implements Comparable<ListViewItem>, Parcelable {
    public static final String DATE_FORMAT = "MMM dd yyyy";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
    private final String fileName;
    private final String fileSize;
    private final String fileModifyTime;
    private String absPath;
    private boolean isDirectory;
    private boolean isLink;

    private boolean canRead;

    public ListViewItem(String fileName,
                        long fileSize,
                        long fileModifyTime) {
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
        if (this.isDirectory()) {
            if (another.isDirectory()) {
                return fileName.substring(1).compareTo(another.getFileName().substring(1));
            } else {
                return -1;
            }
        } else {
            if (another.isDirectory()) {
                return 1;
            } else if (this.getFileName().startsWith(StringUtil.FILE_LINK_PREFIX)) {
                if (another.getFileName().startsWith(StringUtil.FILE_LINK_PREFIX)) {
                    return fileName.substring(1).compareTo(another.getFileName().substring(1));
                } else {
                    return fileName.substring(1).compareTo(another.getFileName());
                }
            } else {
                return fileName.compareTo(another.getFileName());
            }
        }
    }

    public boolean isEmpty() {
        return absPath.isEmpty();
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

    public Creator<ListViewItem> CREATOR = new Creator<ListViewItem>() {
        @Override
        public ListViewItem createFromParcel(Parcel source) {
            return new ListViewItem(source);
        }

        @Override
        public ListViewItem[] newArray(int size) {
            return new ListViewItem[0];
        }
    };
}
