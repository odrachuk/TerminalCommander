package com.softsandr.terminal.ui.widget.actionbar;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import com.softsandr.terminal.R;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class ActionBarCtrlMenuItem extends TextView {
    private boolean isToggled;

    public ActionBarCtrlMenuItem(Context context) {
        super(context);
        initButton();
    }

    public ActionBarCtrlMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton();
    }

    public ActionBarCtrlMenuItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initButton();
    }

    private void initButton() {
        setText(getResources().getString(R.string.action_ctrl));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        setTextColor(getResources().getColor(R.color.COLOR_858585));
        setAllCaps(true);
        setTypeface(Typeface.DEFAULT_BOLD);
        setPadding(getResources().getInteger(R.integer.action_bar_middle_item_padding_left_right), 0,
                getResources().getInteger(R.integer.action_bar_middle_item_padding_left_right), 0);
    }

    public void onToggle() {
        if (isToggled) {
            setTextColor(getResources().getColor(R.color.COLOR_858585));
            isToggled = false;
        } else {
            isToggled = true;
            setTextColor(getResources().getColor(R.color.COLOR_FF4747));
        }
    }

    public boolean isToggled() {
        return isToggled;
    }
}
