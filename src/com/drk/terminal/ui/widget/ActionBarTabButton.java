package com.drk.terminal.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;
import com.drk.terminal.R;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/17/13
 * Time: 9:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class ActionBarTabButton extends Button {

    public ActionBarTabButton(Context context) {
        super(context);
        initButton();
    }

    public ActionBarTabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton();
    }

    public ActionBarTabButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initButton();
    }

    private void initButton() {
        setText(getResources().getString(R.string.action_tab));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        setWidth(getResources().getInteger(R.integer.ab_toggle_btn_width));
    }
}
