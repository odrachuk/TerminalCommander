package com.softsandr.terminal.ui.widget.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;
import com.softsandr.terminal.R;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
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