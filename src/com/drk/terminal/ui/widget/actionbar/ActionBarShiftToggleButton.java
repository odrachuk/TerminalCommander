package com.drk.terminal.ui.widget.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;
import com.drk.terminal.R;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/16/13
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionBarShiftToggleButton extends ToggleButton {

    public ActionBarShiftToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initButton();
    }

    public ActionBarShiftToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton();
    }

    public ActionBarShiftToggleButton(Context context) {
        super(context);
        initButton();
    }

    private void initButton() {
        setText(getResources().getString(R.string.shift_off));
        setTextOn(getResources().getString(R.string.shift_on));
        setTextOff(getResources().getString(R.string.shift_off));
        setWidth(getResources().getInteger(R.integer.ab_toggle_btn_width));
    }
}
