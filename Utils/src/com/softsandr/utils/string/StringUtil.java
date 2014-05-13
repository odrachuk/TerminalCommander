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
package com.softsandr.utils.string;

/**
 * The String unified operations
 */
public class StringUtil {
    public final static String EMPTY = "";
    public final static String WHITESPACE = " ";
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String PATH_SEPARATOR = "/"; // System.getProperty("path.separator");
    public final static char PATH_SEPARATE_CHAR = '/';
    public final static String DIRECTORY_LINK_PREFIX = "~";
    public final static String FILE_LINK_PREFIX = "@";
    public final static String PARENT_DOTS = "/..";
    public final static String UP_DIR = "UP--DIR ";

    /**
     * Utility class
     */
    private StringUtil() {
    }

    public static int countOccurrences(String haystack, String needle) {
        if (haystack.contains(needle)) {
            String[] tmp = haystack.split(needle);
            return tmp.length - 1;
        }
        return 0;
    }

    public static boolean isStringDigital(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isCorrectPath(String path) {
        return !path.contains("//") && path.startsWith(PATH_SEPARATOR);
    }
}
