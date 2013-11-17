package com.drk.terminal.ui.widget.actionbar;

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
