package com.drk.terminal.console;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.*;
import com.drk.terminal.TerminalActivity;
import com.drk.terminal.command.Command;
import com.drk.terminal.command.ConsoleCommand;

public class TerminalInputActionListener implements OnEditorActionListener {
    private TerminalActivity mActivity;

    public TerminalInputActionListener(TerminalActivity activity) {
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