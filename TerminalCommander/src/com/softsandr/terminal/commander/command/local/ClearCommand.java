package com.softsandr.terminal.commander.command.local;

import com.softsandr.terminal.commander.TerminalCommander;

import static com.drk.terminal.util.utils.StringUtil.EMPTY;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class ClearCommand implements LocalCommand {
    @Override
    public String isExecutable(TerminalCommander terminalProcess) {
        return EMPTY;
    }

    @Override
    public String onExecute(TerminalCommander terminalProcess) {
        terminalProcess.onClear();
        return EMPTY;
    }
}
