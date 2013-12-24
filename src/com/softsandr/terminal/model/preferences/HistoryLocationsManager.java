package com.softsandr.terminal.model.preferences;

import com.softsandr.terminal.ui.activity.terminal.TerminalActivePage;

import java.util.LinkedList;

/**
 * Date: 11/26/13
 *
 * @author Drachuk O.V.
 */
public class HistoryLocationsManager {
    private static final int MAX_HISTORY_SIZE = 20;
    private final LinkedList<String> historyLocationsQueue;

    public HistoryLocationsManager(TerminalPreferences terminalPreferences, TerminalActivePage activePage) {
        this.historyLocationsQueue = new LinkedList<String>();
        String[] prefLocations;
        if (activePage.equals(TerminalActivePage.LEFT)) {
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
        } else {
            historyLocationsQueue.remove(location);
            historyLocationsQueue.addFirst(location);
        }
    }

    public String[] getActualHistoryLocations() {
        return historyLocationsQueue.toArray(new String[historyLocationsQueue.size()]);
    }
}
