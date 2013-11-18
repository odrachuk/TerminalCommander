package com.drk.terminal.ui.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.drk.terminal.model.listview.ListViewItem;
import com.drk.terminal.ui.activity.terminal.SelectionStrategy;
import com.drk.terminal.ui.adapter.ListViewAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/11/13
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalListView extends ListView {
    private SelectionStrategy selectionStrategy;

    public TerminalListView(Context context) {
        this(context, null);
    }

    public TerminalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initList();
    }

    private void initList() {
        setOnItemClickListener(makeItemClickListener());
    }

    private OnItemClickListener makeItemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectionStrategy = ((ListViewAdapter) getAdapter()).getSelectionStrategy();
                if (selectionStrategy.isCtrlToggle() ||
                        selectionStrategy.isShiftToggle()) {
                    selectionStrategy.addSelection(position);
                } else {
                    selectionStrategy.clear();
                    ListViewAdapter adapter = (ListViewAdapter) getAdapter();
                    ListViewItem selectedItem = (ListViewItem) getAdapter().getItem(position);
                    if (selectedItem.isParentDots()) {
                        String backPath = adapter.getBackPath();
                        if (backPath != null) {
                            adapter.changeDirectory(backPath);
                            smoothScrollToPosition(0);
                        }
                    } else if (selectedItem.isDirectory()) {
                        if (selectedItem.canRead()) {
                            adapter.changeDirectory(selectedItem.getFileName());
                            smoothScrollToPosition(0);
                        } else {
                            Toast.makeText(getContext(), "Selected directory: " +
                                    ((ListViewItem) getAdapter().getItem(position)).getFileName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // todo start opening
                        Toast.makeText(getContext(), "Selected file: " +
                                ((ListViewItem) getAdapter().getItem(position)).getFileName(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }
}
