package com.softsandr.terminal.ui.widget.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;
import com.softsandr.terminal.R;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class ActionBarCtrlToggleButton extends ToggleButton {

    public ActionBarCtrlToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initButton();
    }

    public ActionBarCtrlToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton();
    }

    public ActionBarCtrlToggleButton(Context context) {
        super(context);
        initButton();
    }

    private void initButton() {
        setText(getResources().getString(R.string.ctrl_off));
        setTextOn(getResources().getString(R.string.ctrl_on));
        setTextOff(getResources().getString(R.string.ctrl_off));
        setWidth(getResources().getInteger(R.integer.ab_toggle_btn_width));
    }
}
