/*******************************************************************************
 * Created by o.drachuk on 10/01/2014.
 *
 * Copyright Oleksandr Drachuk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.softsandr.terminal.data.listview;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.softsandr.utils.sort.AlphanumComparator;
import com.softsandr.utils.string.StringUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class represent one record of List of files
 */
public class ListViewItem implements Comparable<ListViewItem>, Parcelable {
    private static final String LOG_TAG = ListViewItem.class.getSimpleName();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.US);

    public static final long DIRECTORY_DEF_SIZE = -1;
    public static final long UP_LINK_DEF_SIZE = -2;

    private ListViewSortingStrategy sortingStrategy;
    private String fileModifyTime;
    private String fileName;
    private String fileSize;
    private String absPath;
    private boolean isDirectory;
    private boolean canRead;
    private boolean canWrite;
    private boolean canExecute;
    private boolean isLink;

    public ListViewItem() {
    }

    public ListViewItem setFileModifyTime(long fileModifyTime) {
        this.fileModifyTime = sdf.format(fileModifyTime);
        return this;
    }

    public String getFileModifyTime() {
        return fileModifyTime;
    }

    public ListViewItem setFileSize(long fileSize) {
        this.fileSize = readableFileSize(fileSize);
        return this;
    }

    public String getFileSize() {
        return fileSize;
    }

    public ListViewItem setFileName(String fileName) {
        this.fileName = fileName;
        return this;
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

    public ListViewItem setCanRead(boolean canRead) {
        this.canRead = canRead;
        return this;
    }

    public boolean canRead() {
        return canRead;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public ListViewItem setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
        return this;
    }

    public boolean isCanExecute() {
        return canExecute;
    }

    public ListViewItem setCanExecute(boolean canExecute) {
        this.canExecute = canExecute;
        return this;
    }

    public ListViewSortingStrategy getSortingStrategy() {
        return sortingStrategy;
    }

    public ListViewItem setSortingStrategy(ListViewSortingStrategy sortingStrategy) {
        this.sortingStrategy = sortingStrategy;
        return this;
    }

    @Override
    public int compareTo(ListViewItem another) {
        switch (sortingStrategy) {
            case SORT_BY_DATE:
                return compareModifyTime(another);
            case SORT_BY_NAME:
                return compareFileNames(another);
            case SORT_BY_SIZE:
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

    /* Start Parcelable declarations */
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
        dest.writeBooleanArray(new boolean[]{canWrite});
        dest.writeBooleanArray(new boolean[]{canRead});
        dest.writeBooleanArray(new boolean[]{canExecute});
    }

    private ListViewItem(Parcel parcel) {
        fileName = parcel.readString();
        fileSize = parcel.readString();
        fileModifyTime = parcel.readString();
        absPath = parcel.readString();
        sortingStrategy = (ListViewSortingStrategy) parcel.readSerializable();

        boolean[] isDirectoryRsp = new boolean[1];
        parcel.readBooleanArray(isDirectoryRsp);
        isDirectory = isDirectoryRsp[0];

        boolean[] isLinkRsp = new boolean[1];
        parcel.readBooleanArray(isLinkRsp);
        isDirectory = isLinkRsp[0];

        boolean[] canWriteRsp = new boolean[1];
        parcel.readBooleanArray(canWriteRsp);
        canWrite = canWriteRsp[0];

        boolean[] canReadRsp = new boolean[1];
        parcel.readBooleanArray(canReadRsp);
        canRead = canReadRsp[0];

        boolean[] canExecuteRsp = new boolean[1];
        parcel.readBooleanArray(canExecuteRsp);
        canExecute = canExecuteRsp[0];
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
    /* End Parcelable declarations */

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListViewItem)) return false;
        ListViewItem item = (ListViewItem) o;
        return canExecute == item.canExecute
                && canRead == item.canRead
                && canWrite == item.canWrite
                && isDirectory == item.isDirectory
                && isLink == item.isLink
                && !(absPath != null ? !absPath.equals(item.absPath) : item.absPath != null)
                && !(fileModifyTime != null ? !fileModifyTime.equals(item.fileModifyTime) : item.fileModifyTime != null)
                && !(fileName != null ? !fileName.equals(item.fileName) : item.fileName != null)
                && !(fileSize != null ? !fileSize.equals(item.fileSize) : item.fileSize != null)
                && sortingStrategy == item.sortingStrategy;
    }

    @Override
    public int hashCode() {
        int result = sortingStrategy != null ? sortingStrategy.hashCode() : 0;
        result = 31 * result + (fileModifyTime != null ? fileModifyTime.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (fileSize != null ? fileSize.hashCode() : 0);
        result = 31 * result + (absPath != null ? absPath.hashCode() : 0);
        result = 31 * result + (isDirectory ? 1 : 0);
        result = 31 * result + (canRead ? 1 : 0);
        result = 31 * result + (canWrite ? 1 : 0);
        result = 31 * result + (canExecute ? 1 : 0);
        result = 31 * result + (isLink ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ListViewItem{" +
                "fileModifyTime='" + fileModifyTime + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", sortingStrategy=" + sortingStrategy +
                ", isDirectory=" + isDirectory +
                ", canRead=" + canRead +
                ", isLink=" + isLink +
                ", absPath='" + absPath + '\'' +
                '}';
    }
}
