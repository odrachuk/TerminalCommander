package com.softsandr.terminal.ui.widget.actionbar;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;
import com.softsandr.terminal.R;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class ActionBarShiftMenuItem extends TextView {
    private boolean isToggled;

    public ActionBarShiftMenuItem(Context context) {
        super(context);
        initButton();
    }

    public ActionBarShiftMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton();
    }

    public ActionBarShiftMenuItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initButton();
    }

    private void initButton() {
        setText(getResources().getString(R.string.action_shift));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        setTextColor(getResources().getColor(R.color.COLOR_858585));
        setAllCaps(true);
        setTypeface(Typeface.DEFAULT_BOLD);
//        setBackground(getResources().getDrawable(R.drawable.list_dark_gray_cursor));
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
