package com.drk.terminal.ui.widget.listview.observer;

import com.drk.terminal.ui.widget.listview.TerminalListView;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/17/13
 * Time: 9:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ListViewObserver {
    void onItemSelected(TerminalListView listView, int selectedPosition);
}
