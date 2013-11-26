package com.drk.terminal.model.shpref;

import android.app.Activity;
import com.drk.terminal.ui.activity.terminal.TerminalActivity;

import java.util.LinkedList;

/**
 * Date: 11/26/13
 *
 * @author Drachuk O.V.
 */
public class HistoryLocationsManager {
    private static final int MAX_HISTORY_SIZE = 20;
    private final LinkedList<String> historyLocationsQueue;

    public HistoryLocationsManager(TerminalPreferences terminalPreferences, TerminalActivity.ActivePage activePage) {
        this.historyLocationsQueue = new LinkedList<String>();
        String[] prefLocations = null;
        if (activePage.equals(TerminalActivity.ActivePage.LEFT)) {
            prefLocations = terminalPreferences.loadLeftHistoryLocation();
        } else {
            prefLocations = terminalPreferences.loadRightHistoryLocation();
        }
        if (prefLocations!= null && prefLocations.length > 0) {
            for (String s : prefLocations) {
                addLocation(s);
            }
        }
    }

    public void addLocation(String location) {
        if (!historyLocationsQueue.contains(location)) {
            if (historyLocationsQueue.size() == MAX_HISTORY_SIZE) {
                historyLocationsQueue.removeLast();
            }
            historyLocationsQueue.addFirst(location);
        }
    }

    public String[] getActualHistoryLocations() {
        return historyLocationsQueue.toArray(new String[historyLocationsQueue.size()]);
    }
}
