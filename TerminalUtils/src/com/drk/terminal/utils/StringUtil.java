package com.drk.terminal.utils;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/23/13
 * Time: 6:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {
    public final static String EMPTY = "";
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String PATH_SEPARATOR = "/"; // System.getProperty("path.separator");
    public final static char PATH_SEPARATE_CHAR = '/';
    public final static String DIRECTORY_LINK_PREFIX = "~";
    public final static String FILE_LINK_PREFIX = "@";
    public final static String PARENT_DOTS = "/..";
    public final static String UP_DIR = "UP--DIR ";

    public static int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }

}
