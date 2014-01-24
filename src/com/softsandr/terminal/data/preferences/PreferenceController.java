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
package com.softsandr.terminal.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.JsonReader;
import android.util.Log;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.preference.custom.*;
import com.softsandr.terminal.data.listview.ListViewSortingStrategy;
import com.softsandr.utils.string.StringUtil;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class make entry point for all {@link java.util.prefs.Preferences} used in App
 */
public final class PreferenceController {
    private static final String LOG_NAME = PreferenceController.class.getSimpleName();
    private static final String LEFT_PANEL_LAST_LOCATION_PREF = LOG_NAME + ".LEFT_PANEL_LAST_LOCATION_PREF";
    private static final String RIGHT_PANEL_LAST_LOCATION_PREF = LOG_NAME + ".RIGHT_PANEL_LAST_LOCATION_PREF";
    private static final String LEFT_HISTORY_LOCATIONS = LOG_NAME + ".LEFT_HISTORY_LOCATIONS";
    private static final String RIGHT_HISTORY_LOCATIONS = LOG_NAME + ".RIGHT_HISTORY_LOCATIONS";
    private static final String SORTING_STRATEGY = LOG_NAME + ".SORTING_STRATEGY";

    /**
     * This class used as utility class and can't be instantiated
     */
    private PreferenceController() {
    }

    /**
     * Init application preferences when start application or when needs restore to default settings
     *
     * @param context     foreground {@link Context}
     * @param preferences the application default {@link android.content.SharedPreferences}
     */
    public static void initDefault(Context context, SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        // archive item color
        if (!preferences.contains(context.getString(R.string.pref_archive_item_color_key))) {
            setDefaultArchiveMediaItemColor(context, editor);
        }
        // document item color
        if (!preferences.contains(context.getString(R.string.pref_doc_item_color_key))) {
            setDefaultDocumentMediaItemColor(context, editor);
        }
        // image item color
        if (!preferences.contains(context.getString(R.string.pref_images_item_color_key))) {
            setDefaultImageMediaItemColor(context, editor);
        }
        // media item color
        if (!preferences.contains(context.getString(R.string.pref_media_item_color_key))) {
            setDefaultMediaItemColor(context, editor);
        }
        // shell-script item color
        if (!preferences.contains(context.getString(R.string.pref_shell_item_color_key))) {
            setDefaultShellItemColor(context, editor);
        }
        // terminal screen background color
        if (!preferences.contains(context.getString(R.string.pref_term_bg_color_key))) {
            setDefaultTerminalBgColor(context, editor);
        }
        // panel's list views font size
        if (!preferences.contains(context.getString(R.string.pref_font_picker_key))) {
            setDefaultListFontSize(context, editor);
        }
        editor.commit();
    }

    private static void setDefaultArchiveMediaItemColor(Context context, SharedPreferences.Editor editor) {
        editor.putString(context.getString(R.string.pref_archive_item_color_key),
                ArchiveColorPickerPreference.DEFAULT_VALUE);
    }

    private static void setDefaultDocumentMediaItemColor(Context context, SharedPreferences.Editor editor) {
        editor.putString(context.getString(R.string.pref_doc_item_color_key),
                DocumentsColorPickerPreference.DEFAULT_VALUE);
    }

    private static void setDefaultImageMediaItemColor(Context context, SharedPreferences.Editor editor) {
        editor.putString(context.getString(R.string.pref_images_item_color_key),
                ImagesColorPickerPreference.DEFAULT_VALUE);
    }

    private static void setDefaultMediaItemColor(Context context, SharedPreferences.Editor editor) {
        editor.putString(context.getString(R.string.pref_media_item_color_key),
                MediaColorPickerPreference.DEFAULT_VALUE);
    }

    private static void setDefaultShellItemColor(Context context, SharedPreferences.Editor editor) {
        editor.putString(context.getString(R.string.pref_shell_item_color_key),
                ShellColorPickerPreference.DEFAULT_VALUE);
    }

    private static void setDefaultTerminalBgColor(Context context, SharedPreferences.Editor editor) {
        editor.putString(context.getString(R.string.pref_term_bg_color_key),
                TerminalBgColorPickerPreference.DEFAULT_VALUE);
    }

    private static void setDefaultListFontSize(Context context, SharedPreferences.Editor editor) {
        editor.putInt(context.getString(R.string.pref_font_picker_key),
                (int) context.getResources().getDimension(R.dimen.terminal_list_item_text_size));
    }

    /**
     * Used for saving in {@link android.content.SharedPreferences} new value of left and right locations
     *
     * @param preferences   the default {@link android.content.SharedPreferences}
     * @param leftLocation  the left location
     * @param rightLocation the right location
     */
    public static void saveLastLocations(SharedPreferences preferences, String leftLocation, String rightLocation) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LEFT_PANEL_LAST_LOCATION_PREF, leftLocation);
        editor.putString(RIGHT_PANEL_LAST_LOCATION_PREF, rightLocation);
        editor.commit();
    }

    /**
     * Get saved in {@link android.content.SharedPreferences} last right location
     *
     * @param preferences a {@link android.content.SharedPreferences}
     * @return string path location
     */
    public static String loadLastRightLocation(SharedPreferences preferences) {
        return preferences.getString(RIGHT_PANEL_LAST_LOCATION_PREF, StringUtil.PATH_SEPARATOR);
    }

    /**
     * Get saved in {@link android.content.SharedPreferences} last left location
     *
     * @param preferences a {@link android.content.SharedPreferences}
     * @return string path location
     */
    public static String loadLastLeftLocation(SharedPreferences preferences) {
        return preferences.getString(LEFT_PANEL_LAST_LOCATION_PREF, StringUtil.PATH_SEPARATOR);
    }

    /**
     * Used for saving in {@link android.content.SharedPreferences} new values of history from left panel
     *
     * @param preferences            the default {@link android.content.SharedPreferences}
     * @param actualHistoryLocations array of locations
     */
    public static void saveLeftHistoryLocations(SharedPreferences preferences, String[] actualHistoryLocations) {
        if (actualHistoryLocations != null) {
            SharedPreferences.Editor editor = preferences.edit();
            Set<String> stringSet = new LinkedHashSet<String>(actualHistoryLocations.length);
            Collections.addAll(stringSet, actualHistoryLocations);
            editor.putStringSet(LEFT_HISTORY_LOCATIONS, stringSet);
            editor.commit();
        }
    }

    /**
     * Get saved in {@link android.content.SharedPreferences} array of locations from left panel
     *
     * @param preferences default {@link android.content.SharedPreferences}
     * @return array of strings
     */
    public static String[] loadLeftHistoryLocations(SharedPreferences preferences) {
        Set<String> stringSet = preferences.getStringSet(LEFT_HISTORY_LOCATIONS, new LinkedHashSet<String>());
        return stringSet.toArray(new String[stringSet.size()]);
    }

    /**
     * Used for saving in {@link android.content.SharedPreferences} new values of history locations from right panel
     *
     * @param preferences            the default {@link android.content.SharedPreferences}
     * @param actualHistoryLocations array of locations
     */
    public static void saveRightHistoryLocations(SharedPreferences preferences, String[] actualHistoryLocations) {
        if (actualHistoryLocations != null) {
            SharedPreferences.Editor editor = preferences.edit();
            Set<String> stringSet = new LinkedHashSet<String>(actualHistoryLocations.length);
            Collections.addAll(stringSet, actualHistoryLocations);
            editor.putStringSet(RIGHT_HISTORY_LOCATIONS, stringSet);
            editor.commit();
        }
    }

    /**
     * Get saved in {@link android.content.SharedPreferences} array of locations from right panel
     *
     * @param preferences default {@link android.content.SharedPreferences}
     * @return array of strings
     */
    public static String[] loadRightHistoryLocations(SharedPreferences preferences) {
        Set<String> stringSet = preferences.getStringSet(RIGHT_HISTORY_LOCATIONS, new LinkedHashSet<String>());
        return stringSet.toArray(new String[stringSet.size()]);
    }

    /**
     * Used for saving in {@link android.content.SharedPreferences} new values of sorting mode
     *
     * @param preferences     the default {@link android.content.SharedPreferences}
     * @param sortingStrategy the constant from {@link com.softsandr.terminal.data.listview.ListViewSortingStrategy}
     */
    public static void saveSortingStrategy(SharedPreferences preferences, ListViewSortingStrategy sortingStrategy) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SORTING_STRATEGY, sortingStrategy.toString());
        editor.commit();
    }

    /**
     * Get saved in {@link android.content.SharedPreferences} constant of {@link com.softsandr.terminal.data.listview.ListViewSortingStrategy}
     *
     * @param preferences default application {@link android.content.SharedPreferences}
     * @return {@link com.softsandr.terminal.data.listview.ListViewSortingStrategy}
     */
    public static ListViewSortingStrategy loadSortingStrategy(SharedPreferences preferences) {
        String sortingStrategy = preferences.getString(SORTING_STRATEGY,
                ListViewSortingStrategy.SORT_BY_NAME.toString());
        return ListViewSortingStrategy.valueOf(sortingStrategy);
    }

    /**
     * Get saved in {@link android.content.SharedPreferences} value of background color for terminal
     * @param context       the foreground {@link android.content.Context}
     * @param preferences   the default context {@link android.content.SharedPreferences}
     * @return  integer as value of Color
     */
    public static int loadTerminalBgColor(Context context, SharedPreferences preferences) {
        String colorJson = preferences.getString(context.getString(R.string.pref_term_bg_color_key),
                TerminalBgColorPickerPreference.DEFAULT_VALUE);
        return parseColorFromJson(colorJson);
    }

    /**
     * Get saved in {@link android.content.SharedPreferences} value of color for archives
     * @param context       the foreground {@link android.content.Context}
     * @param preferences   the default {@link android.content.SharedPreferences}
     * @return  integer as color
     */
    public static int loadArchiveItemColor(Context context, SharedPreferences preferences) {
        String colorJson = preferences.getString(context.getString(R.string.pref_archive_item_color_key),
                ArchiveColorPickerPreference.DEFAULT_VALUE);
        return parseColorFromJson(colorJson);
    }

    /**
     * Get saved in {@link android.content.SharedPreferences} value of color for archives
     * @param context       the foreground {@link android.content.Context}
     * @param preferences   the default {@link android.content.SharedPreferences}
     * @return  integer as color
     */
    public static int loadDocumentItemColor(Context context, SharedPreferences preferences) {
        String colorJson = preferences.getString(context.getString(R.string.pref_doc_item_color_key),
                DocumentsColorPickerPreference.DEFAULT_VALUE);
        return parseColorFromJson(colorJson);
    }

    /**
     * Get saved in {@link android.content.SharedPreferences} value of color for archives
     * @param context       the foreground {@link android.content.Context}
     * @param preferences   the default {@link android.content.SharedPreferences}
     * @return  integer as color
     */
    public static int loadImageItemColor(Context context, SharedPreferences preferences) {
        String colorJson = preferences.getString(context.getString(R.string.pref_images_item_color_key),
                ImagesColorPickerPreference.DEFAULT_VALUE);
        return parseColorFromJson(colorJson);
    }

    /**
     * Get saved in {@link android.content.SharedPreferences} value of color for archives
     * @param context       the foreground {@link android.content.Context}
     * @param preferences   the default {@link android.content.SharedPreferences}
     * @return  integer as color
     */
    public static int loadMediaItemColor(Context context, SharedPreferences preferences) {
        String colorJson = preferences.getString(context.getString(R.string.pref_media_item_color_key),
                MediaColorPickerPreference.DEFAULT_VALUE);
        return parseColorFromJson(colorJson);
    }

    /**
     * Get saved in {@link android.content.SharedPreferences} value of color for archives
     * @param context       the foreground {@link android.content.Context}
     * @param preferences   the default {@link android.content.SharedPreferences}
     * @return  integer as color
     */
    public static int loadShellItemColor(Context context, SharedPreferences preferences) {
        String colorJson = preferences.getString(context.getString(R.string.pref_shell_item_color_key),
                ShellColorPickerPreference.DEFAULT_VALUE);
        return parseColorFromJson(colorJson);
    }

    /**
     * Get saved in {@link android.content.SharedPreferences} value of color for archives
     * @param context       the foreground {@link android.content.Context}
     * @param preferences   the default {@link android.content.SharedPreferences}
     * @return  integer as color
     */
    public static int loadListFontSize(Context context, SharedPreferences preferences) {
        return preferences.getInt(context.getString(R.string.pref_font_picker_key),
                (int) context.getResources().getDimension(R.dimen.terminal_list_item_text_size));
    }

    /**
     * Used when needs parse color values {red, green, blue, alpha} from json string
     *
     * @param jsonValue a json string
     * @return array of color components
     */
    public static int[] parseColorComponentsFromJson(String jsonValue) {
        Reader reader = new StringReader(jsonValue);
        JsonReader jsonReader = new JsonReader(reader);
        int[] colors = new int[]{0, 0, 0};
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (name != null) {
                    if (name.equals("red")) {
                        colors[0] = jsonReader.nextInt();
                    } else if (name.equals("green")) {
                        colors[1] = jsonReader.nextInt();
                    } else if (name.equals("blue")) {
                        colors[2] = jsonReader.nextInt();
                    } else {
                        jsonReader.skipValue();
                    }
                }
            }
            jsonReader.endObject();
        } catch (IOException ex) {
            Log.d(PreferenceController.class.getSimpleName(), "readColorJson: " + ex.getMessage());
        }
        return colors;
    }

    /**
     * Use for parsing Color from json string
     *
     * @param json a json string
     * @return {@link android.graphics.Color} as integer
     */
    public static int parseColorFromJson(String json) {
        int[] colorArray = parseColorComponentsFromJson(json);
        return Color.rgb(colorArray[0], colorArray[1], colorArray[2]);
    }
}
