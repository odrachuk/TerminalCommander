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
public class ActionBarCommanderButton extends Button {

    public ActionBarCommanderButton(Context context) {
        super(context);
        initButton();
    }

    public ActionBarCommanderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton();
    }

    public ActionBarCommanderButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initButton();
    }

    private void initButton() {
        setText(getResources().getString(R.string.action_commander));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        setWidth(getResources().getInteger(R.integer.ab_toggle_btn_width));
    }
}
