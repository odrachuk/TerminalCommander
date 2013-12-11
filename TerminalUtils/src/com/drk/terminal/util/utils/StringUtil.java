package com.drk.terminal.util.utils;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
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

    public static boolean isStringDigital(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
