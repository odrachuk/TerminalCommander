package com.drk.terminal.model.shpref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.drk.terminal.utils.StringUtil;

/**
 * Date: 11/26/13
 *
 * @author Drachuk O.V.
 */
public class TerminalPreferences {
    private static final String LOG_NAME = TerminalPreferences.class.getSimpleName();
    private static final String LEFT_PANEL_LAST_LOCATION_PREF = LOG_NAME + ".LEFT_PANEL_LAST_LOCATION_PREF";
    private static final String RIGHT_PANEL_LAST_LOCATION_PREF = LOG_NAME + ".RIGHT_PANEL_LAST_LOCATION_PREF";

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
}
