package com.drk.terminal.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.drk.terminal.model.listview.ListViewItem;
import com.drk.terminal.ui.adapter.ListViewAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/11/13
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalListView extends ListView {

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
                ListViewAdapter adapter = (ListViewAdapter) getAdapter();
                ListViewItem selectedItem = (ListViewItem) getAdapter().getItem(position);
                if (selectedItem.isParentDots()) {
                    adapter.changeDirectory(selectedItem.getParentPath());
                    smoothScrollToPosition(0);
                } else if (selectedItem.isDirectory() && selectedItem.canRead()) {
                    adapter.changeDirectory(selectedItem.getFileName());
                    smoothScrollToPosition(0);
                } else {
                    // todo start opening
                    Toast.makeText(getContext(), "Selected item: " +
                            ((ListViewItem) getAdapter().getItem(position)).getFileName(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}
