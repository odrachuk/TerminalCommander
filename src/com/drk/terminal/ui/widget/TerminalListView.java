package com.drk.terminal.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import com.drk.terminal.R;
import com.drk.terminal.ui.DirectoryContentAdapter;
import com.drk.terminal.ui.DirectoryContentInfo;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/11/13
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalListView extends ListView {
    private View selectedView;

    public TerminalListView(Context context) {
        this(context, null);
    }

    public TerminalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initList();
    }

    private void initList() {
        setOnScrollListener(makeScrollListener());
    }

    private OnScrollListener makeScrollListener() {
        return new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case OnScrollListener.SCROLL_STATE_IDLE:
                        for (int i = 0; i < getChildCount(); i++) {
                            View child = getChildAt(i);
                            child.setBackgroundColor(getResources().getColor(R.color.COLOR_002EB8));
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // todo
            }
        };
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                int position = pointToPosition(x, y);
                if (position == ListView.INVALID_POSITION) {
                    break;
                }
                View downView = findChildAtPosition(x, y);
                if (downView != null && downView != selectedView) {
                    // select new item and restore color on previous
                    if (selectedView != null) {
                        selectedView.setBackgroundColor(getResources().getColor(R.color.COLOR_002EB8));
                    }
                    makeNewSelection(downView);
                    return true;
                } else {
                    // make directory change
                    DirectoryContentAdapter adapter = (DirectoryContentAdapter) getAdapter();
                    DirectoryContentInfo selectedItem = (DirectoryContentInfo) getAdapter().getItem(position);
                    if (selectedItem.isParentDots()) {
                        adapter.changeDirectory(selectedItem.getParentPath());
                    } else if (selectedItem.isDirectory() && selectedItem.canRead()) {
                        adapter.changeDirectory(selectedItem.getFileName());
                    } else {
                        // todo start opening
                        Toast.makeText(getContext(), "Selected item: " +
                                ((DirectoryContentInfo) getAdapter().getItem(position)).getFileName(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    private View findChildAtPosition(int x, int y) {
        View childView = null;
        Rect rect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.getHitRect(rect);
            if (rect.contains(x, y)) {
                childView = child;
                break;
            }
        }
        return childView;
    }

    private void makeNewSelection(View downView) {
        selectedView = downView;
        downView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child != downView) {
                child.setBackgroundColor(getResources().getColor(R.color.COLOR_002EB8));
            }
        }
    }
}
