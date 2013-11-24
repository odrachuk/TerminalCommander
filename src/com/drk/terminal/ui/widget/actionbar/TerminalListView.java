package com.drk.terminal.ui.widget.actionbar;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/24/13
 * Time: 8:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalListView extends ListView {
    public TerminalListView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
