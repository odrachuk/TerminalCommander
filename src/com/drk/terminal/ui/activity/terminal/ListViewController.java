package com.drk.terminal.ui.activity.terminal;

import android.content.Context;
import android.widget.Toast;
import com.drk.terminal.model.listview.ListViewItem;
import com.drk.terminal.ui.adapter.ListViewAdapter;
import com.drk.terminal.ui.widget.actionbar.observer.ActionBarBtnObserver;
import com.drk.terminal.ui.widget.listview.TerminalListView;
import com.drk.terminal.ui.widget.listview.observer.ListViewObserver;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/17/13
 * Time: 9:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListViewController implements ListViewObserver, ActionBarBtnObserver {
    private Context context;
    private boolean shiftMode, ctrlMode;

    public ListViewController(Context context) {
        this.context = context;
    }

    @Override
    public void onItemSelected(TerminalListView listView, int selectedPosition) {
        ListViewAdapter adapter = (ListViewAdapter) listView.getAdapter();
        ListViewItem selectedItem = (ListViewItem) listView.getAdapter().getItem(selectedPosition);
        if (selectedItem.isParentDots()) {
            String backPath = adapter.getBackPath();
            if (backPath != null) {
                adapter.changeDirectory(backPath);
                listView.smoothScrollToPosition(0);
            }
        } else if (selectedItem.isDirectory()) {
            if (selectedItem.canRead()) {
                adapter.changeDirectory(selectedItem.getFileName());
                listView.smoothScrollToPosition(0);
            } else {
                Toast.makeText(context, "Selected directory: " +
                        ((ListViewItem) listView.getAdapter().getItem(selectedPosition)).getFileName(),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // todo start opening
            Toast.makeText(context, "Selected file: " +
                    ((ListViewItem) listView.getAdapter().getItem(selectedPosition)).getFileName(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShift() {
        shiftMode = true;
    }

    @Override
    public void offShift() {
        shiftMode = false;
    }

    @Override
    public void onCtrl() {
        ctrlMode = true;
    }

    @Override
    public void offCtrl() {
        ctrlMode = false;
    }
}
