package com.softsandr.terminal.model.listview;

import android.os.Parcel;
import android.os.Parcelable;
import com.drk.terminal.util.utils.StringUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class ListViewItem implements Comparable<ListViewItem>, Parcelable {
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

    public ListViewItem(String fileName,
                        long fileSize,
                        long fileModifyTime) {
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
                if (StringUtil.isStringNumber(fileName.substring(1)) &&
                        StringUtil.isStringNumber(another.getFileName().substring(1))) {
                    return ((Integer) Integer.parseInt(fileName.substring(1))).
                            compareTo(Integer.parseInt(another.getFileName().substring(1)));
                } else {
                    return fileName.substring(1).compareTo(another.getFileName().substring(1));
                }
            } else {
                return -1;
            }
        } else {
            if (another.isDirectory()) {
                return 1;
            } else if (this.getFileName().startsWith(StringUtil.FILE_LINK_PREFIX)) {
                if (another.getFileName().startsWith(StringUtil.FILE_LINK_PREFIX)) {
                    if (StringUtil.isStringNumber(fileName.substring(1)) &&
                            StringUtil.isStringNumber(another.getFileName().substring(1))) {
                        return ((Integer) Integer.parseInt(fileName.substring(1))).
                                compareTo(Integer.parseInt(another.getFileName().substring(1)));
                    } else {
                        return fileName.substring(1).compareTo(another.getFileName().substring(1));
                    }
                } else {
                    if (StringUtil.isStringNumber(fileName.substring(1)) &&
                            StringUtil.isStringNumber(another.getFileName())) {
                        return ((Integer) Integer.parseInt(fileName.substring(1))).
                                compareTo(Integer.parseInt(another.getFileName()));
                    } else {
                        return fileName.substring(1).compareTo(another.getFileName());
                    }
                }
            } else {
                if (StringUtil.isStringNumber(fileName) &&
                        StringUtil.isStringNumber(another.getFileName())) {
                    return ((Integer) Integer.parseInt(fileName)).
                            compareTo(Integer.parseInt(another.getFileName()));
                } else {
                    return fileName.compareTo(another.getFileName());
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        ListViewItem other = (ListViewItem) o;
        if (this.fileName.equals(other.fileName) &&
                this.fileSize.equals(other.fileSize) &&
                this.fileModifyTime.equals(other.fileModifyTime) &&
                this.absPath.equals(other.absPath)  &&
                this.isDirectory == other.isDirectory &&
                this.isLink == other.isLink) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + fileName.hashCode();
        result = 37 * result + fileSize.hashCode();
        result = 37 * result + fileModifyTime.hashCode();
        result = 37 * result + (absPath != null? absPath.hashCode() : 0);
        result = 37 * result + (isDirectory? 0: 1);
        result = 37 * result + (isLink? 0: 1);
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
}
