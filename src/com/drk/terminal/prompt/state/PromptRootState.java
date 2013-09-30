package com.drk.terminal.prompt.state;

import com.drk.terminal.prompt.TerminalPrompt;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/29/13
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class PromptRootState implements PromptState {
    private TerminalPrompt mTerminalPrompt;

    public PromptRootState(TerminalPrompt terminalPrompt) {
        mTerminalPrompt = terminalPrompt;
    }

    @Override
    public void addUserLocation(String newLocation) {

    }
}
