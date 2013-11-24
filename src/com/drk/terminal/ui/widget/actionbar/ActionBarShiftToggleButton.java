package com.drk.terminal.ui.widget.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;
import com.drk.terminal.R;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
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
