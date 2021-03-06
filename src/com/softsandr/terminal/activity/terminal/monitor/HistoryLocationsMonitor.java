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
package com.softsandr.terminal.activity.terminal.monitor;

import java.util.LinkedList;

/**
 * This class manage writing and reading from {@link java.util.prefs.Preferences} history of locations by user
 */
public class HistoryLocationsMonitor {
    private static final int MAX_HISTORY_SIZE = 20;
    private final LinkedList<String> historyLocationsQueue;

    public HistoryLocationsMonitor(String[] historyLocations) {
        this.historyLocationsQueue = new LinkedList<String>();
        if (historyLocations != null && historyLocations.length > 0) {
            for (String s : historyLocations) {
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

    /**
     * Used for erasing all data history
     */
    public void clearHistory() {
        historyLocationsQueue.clear();
    }
}
