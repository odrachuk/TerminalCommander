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
package com.softsandr.terminal.ui.activity.terminal.selection;

import android.widget.ArrayAdapter;

import java.util.Set;
import java.util.TreeSet;

/**
 * This class represent strategy that proceed selection items operations and contain
 * separate list of selected and unselected items.
 */
public class SelectionStrategy {
    private final Set<Integer> unselectedItems;
    private final Set<Integer> selectedItems;
    private final ArrayAdapter adapter;
    private boolean isShiftToggle;
    private boolean isCtrlToggle;
    private int lastShiftPosition;

    public SelectionStrategy(ArrayAdapter adapter) {
        this.unselectedItems = new TreeSet<Integer>();
        this.selectedItems = new TreeSet<Integer>();
        this.adapter = adapter;
    }

    public boolean isCtrlToggle() {
        return isCtrlToggle;
    }

    public synchronized void setCtrlToggle(boolean ctrlToggle) {
        isCtrlToggle = ctrlToggle;
    }

    public boolean isShiftToggle() {
        return isShiftToggle;
    }

    public synchronized void setShiftToggle(boolean shiftToggle) {
        isShiftToggle = shiftToggle;
        selectedItems.clear();
    }

    public synchronized void addSelection(int item) {
        if (item >= 0) {
            if (isCtrlToggle) {
                makeCtrlSelection(item);
            } else if (isShiftToggle) {
                makeShiftSelection(item);
            }
        }
    }

    private void makeCtrlSelection(int item) {
        if (selectedItems.contains(item)) {
            removeElement(item);
            adapter.notifyDataSetChanged();
        } else {
            addElement(item);
            adapter.notifyDataSetChanged();
        }
    }

    private void makeShiftSelection(int item) {
        if (selectedItems.isEmpty()) {
            addElement(item);
            lastShiftPosition = item;
            adapter.notifyDataSetChanged();
        } else {
            if (selectedItems.size() == 1) {
                if (item > lastShiftPosition) {
                    for (int i = lastShiftPosition; i <= item; i++) {
                        addElement(i);
                    }
                } else {
                    for (int i = item; i <= lastShiftPosition; i++) {
                        addElement(i);
                    }
                }
                lastShiftPosition = item;
                adapter.notifyDataSetChanged();
            } else {
                if (item > lastShiftPosition) {
                    for (int i = lastShiftPosition; i <= item; i++) {
                        addElement(i);
                    }
                } else {
                    for (int i = item; i < lastShiftPosition; i++) {
                        if (selectedItems.contains(i)) {
                            removeElement(i);
                        } else {
                            addElement(i);
                        }
                    }
                    removeElement(lastShiftPosition);
                }
                lastShiftPosition = item;
                adapter.notifyDataSetChanged();
            }
        }
    }

    public synchronized void makeClearSelected() {
        Integer[] selected = selectedItems.toArray(new Integer[selectedItems.size()]);
        for (Integer item : selected) {
            removeElement(item);
        }
    }

    private synchronized void addElement(int item) {
        if (unselectedItems.contains(item)) {
            unselectedItems.remove(item);
        }
        selectedItems.add(item);
    }

    private synchronized void removeElement(int item) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        }
        unselectedItems.add(item);
    }

    public Set<Integer> getSelectedItems() {
        return selectedItems;
    }

    public Set<Integer> getUnselectedItems() {
        return unselectedItems;
    }

    public void clear() {
        selectedItems.clear();
        unselectedItems.clear();
    }
}
