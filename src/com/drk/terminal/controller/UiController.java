package com.drk.terminal.controller;

import com.drk.terminal.ui.TerminalActivity;
import com.drk.terminal.prompt.TerminalPrompt;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/28/13
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class UiController {
    private TerminalActivity mTerminalActivity;
    private TerminalPrompt mPrompt;
    private boolean hideOutView;

    public UiController(TerminalActivity terminalActivity) {
        mTerminalActivity = terminalActivity;
        mPrompt = new TerminalPrompt(this);
    }

    public TerminalActivity getActivity() {
        return mTerminalActivity;
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
}
