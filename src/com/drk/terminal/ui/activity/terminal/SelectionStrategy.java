package com.drk.terminal.ui.activity.terminal;

import android.widget.ArrayAdapter;
import com.drk.terminal.ui.adapter.ListViewAdapter;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/18/13
 * Time: 10:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectionStrategy {
    private final Set<Integer> selectedItems;
    private final ArrayAdapter adapter;
    private boolean isShiftToggle;
    private boolean isCtrlToggle;

    public SelectionStrategy(ArrayAdapter adapter) {
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
    }

    public synchronized void addSelection(int item) {
        if (item > 0) {
            if (isCtrlToggle) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
                    adapter.notifyDataSetChanged();
                } else {
                    selectedItems.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
        }
//        else if (isShiftToggle) {
//            if (selectedItems.isEmpty()) {
//                selectedItems.add(item);
//            } else {
//                Iterator<Integer> it = selectedItems.iterator();
//                while (it.hasNext()) {
//                    int nextItem = it.next();
//                    if (nextItem == item) {
//                        it.remove();
//                    }
//                }
//            }
//        }
    }

    public Set<Integer> getSelectedItems() {
        return selectedItems;
    }
}
