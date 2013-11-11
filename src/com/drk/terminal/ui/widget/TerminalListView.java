package com.drk.terminal.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import com.drk.terminal.R;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/11/13
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalListView extends ListView {
    private int mItemPosition = ListView.INVALID_POSITION;
    private int mTopScrollBounds, mBottomScrollBounds;
    private boolean isSelection;

    public TerminalListView(Context context) {
        this(context, null);
    }

    public TerminalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initList();
    }

    private void initList() {
        // todo
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                mItemPosition = pointToPosition(x, y);
                if (mItemPosition == ListView.INVALID_POSITION) {
                    break;
                }
                View selectedView = getChildAtPosition(x, y);
                if (selectedView != null) {
                    findScrollBounds();
                    isSelection = true;
                    return true;
                }
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isSelection) {
            return super.onTouchEvent(ev);
        } else {
            final int action = ev.getAction();
            final int x = (int) ev.getX();
            final int y = (int) ev.getY();
            mItemPosition = pointToPosition(x, y);
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    makeNewSelection();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (y >= 0) { // y can be negative if fast moving cursor
                        makeNewSelection();
                        makeScrolling(y);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                default:
                    isSelection = false;
                    makeNewSelection();
                    break;
            }
            return true;
        }
    }

    private void makeNewSelection() {
        View downView = getChildAt(mItemPosition);
        if (downView != null) {
            downView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
            for (int i = 0; i < getChildCount(); i++) {
                if (mItemPosition != i) {
                    View child = getChildAt(i);
                    child.setBackgroundColor(getResources().getColor(R.color.COLOR_002EB8));
                }
            }
        }
    }

    private View getChildAtPosition(int x, int y) {
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

    private void findScrollBounds() {
        mTopScrollBounds = getTop() + getHeight() / 3;
        mBottomScrollBounds = getBottom() - getHeight() / 3;
    }

    private void makeScrolling(int y) {
        int speed = getScrollSpeedForPosition(y);
        if (speed != 0) {
            if (!(getLastVisiblePosition() != getCount() - 1 && speed > 0)
                    || !(getFirstVisiblePosition() == 0 && speed < 0)) {
                smoothScrollBy(speed, 30);
            }
        }
        // for making scrolling to last element in list
        if (y > mBottomScrollBounds && getLastVisiblePosition() == getCount() - 1) {
            smoothScrollToPosition(getCount());
        }
    }

    private int getScrollSpeedForPosition(int y) {
        int speed = 0;
        if (y > mBottomScrollBounds) {
            if (getLastVisiblePosition() < getCount()) {
                speed = y > (getBottom() + mBottomScrollBounds) / 2? 16 : 8;
            } else {
                speed = 1;
            }
        } else if (y < mTopScrollBounds) {
            speed = y < mTopScrollBounds / 2? -16: -8;
            if (getFirstVisiblePosition() == 0) {
                speed = 0;
            }
        }
        return speed;
    }
}
