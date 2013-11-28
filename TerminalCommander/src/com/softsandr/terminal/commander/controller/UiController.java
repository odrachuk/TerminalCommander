package com.softsandr.terminal.commander.controller;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;
import com.softsandr.terminal.commander.prompt.TerminalPrompt;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class UiController {
    private Activity mProcessActivity;
    private TerminalPrompt mPrompt;
    private boolean hideOutView;
    private EditText mTerminalInView;
    private TextView mTerminalOutView;
    private TextView mTerminalPromptView;

    private UiController() {}

    public UiController(Activity terminalActivity,
                        EditText terminalInView,
                        TextView terminalOutView,
                        TextView terminalPromptView) {
        mProcessActivity = terminalActivity;
        mPrompt = new TerminalPrompt(this);
        mTerminalInView = terminalInView;
        mTerminalOutView = terminalOutView;
        mTerminalPromptView = terminalPromptView;
    }

    public Activity getActivity() {
        return mProcessActivity;
    }

    public TerminalPrompt getPrompt() {
        return mPrompt;
    }

    public boolean isHideOutView() {
        return hideOutView;
    }

    public void setHideOutView(boolean hideOutView) {
        this.hideOutView = hideOutView;
    }

    public EditText getTerminalInView() {
        return mTerminalInView;
    }

    public TextView getTerminalPromptView() {
        return mTerminalPromptView;
    }

    public TextView getTerminalOutView() {
        return mTerminalOutView;
    }
}
