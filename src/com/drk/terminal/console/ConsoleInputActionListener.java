package com.drk.terminal.console;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.*;
import com.drk.terminal.ConsoleActivity;
import com.drk.terminal.command.Command;
import com.drk.terminal.command.ConsoleCommand;

public class ConsoleInputActionListener implements OnEditorActionListener {
    private ConsoleActivity mActivity;

    public ConsoleInputActionListener(ConsoleActivity activity) {
        mActivity = activity;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            Command command = new ConsoleCommand(mActivity.getProcess());
            command.execute();
            handled = true;
        }
        return handled;
    }
}