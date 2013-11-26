package com.drk.terminal.model.shpref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.drk.terminal.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Date: 11/26/13
 *
 * @author Drachuk O.V.
 */
public class TerminalPreferences {
    private static final String LOG_NAME = TerminalPreferences.class.getSimpleName();
    private static final String LEFT_PANEL_LAST_LOCATION_PREF = LOG_NAME + ".LEFT_PANEL_LAST_LOCATION_PREF";
    private static final String RIGHT_PANEL_LAST_LOCATION_PREF = LOG_NAME + ".RIGHT_PANEL_LAST_LOCATION_PREF";
    private static final String LEFT_HISTORY_LOCATIONS = LOG_NAME + ".LEFT_HISTORY_LOCATIONS";
    private static final String RIGHT_HISTORY_LOCATIONS = LOG_NAME + ".RIGHT_HISTORY_LOCATIONS";

    private final SharedPreferences mPreferences;

    public TerminalPreferences(Context context) {
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String loadLastLeftLocations() {
        return mPreferences.getString(LEFT_PANEL_LAST_LOCATION_PREF, StringUtil.PATH_SEPARATOR);
    }

    public String loadLastRightLocations() {
        return mPreferences.getString(RIGHT_PANEL_LAST_LOCATION_PREF, StringUtil.PATH_SEPARATOR);
    }

    public void saveLastLocations(String leftLocation, String rightLocation) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(LEFT_PANEL_LAST_LOCATION_PREF, leftLocation);
        editor.putString(RIGHT_PANEL_LAST_LOCATION_PREF, rightLocation);
        editor.commit();
    }

    public void saveLeftHistoryLocations(String[] actualHistoryLocations) {
        if (actualHistoryLocations != null) {
            SharedPreferences.Editor editor = mPreferences.edit();
            Set<String> stringSet = new LinkedHashSet<String>(actualHistoryLocations.length);
            Collections.addAll(stringSet, actualHistoryLocations);
            editor.putStringSet(LEFT_HISTORY_LOCATIONS, stringSet);
            editor.commit();
        }
    }

    public String[] loadLeftHistoryLocation() {
        Set<String> stringSet = mPreferences.getStringSet(LEFT_HISTORY_LOCATIONS, new LinkedHashSet<String>());
        return stringSet.toArray(new String[stringSet.size()]);
    }

    public void saveRightHistoryLocations(String[] actualHistoryLocations) {
        if (actualHistoryLocations != null) {
            SharedPreferences.Editor editor = mPreferences.edit();
            Set<String> stringSet = new LinkedHashSet<String>(actualHistoryLocations.length);
            for (int i = 0; i < actualHistoryLocations.length; i++) {
                stringSet.add(actualHistoryLocations[i]);
            }
            editor.putStringSet(RIGHT_HISTORY_LOCATIONS, stringSet);
            editor.commit();
        }
    }

    public String[] loadRightHistoryLocation() {
        Set<String> stringSet = mPreferences.getStringSet(RIGHT_HISTORY_LOCATIONS, new LinkedHashSet<String>());
        return stringSet.toArray(new String[stringSet.size()]);
    }
}
