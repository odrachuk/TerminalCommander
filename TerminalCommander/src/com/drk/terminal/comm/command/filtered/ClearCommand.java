package com.drk.terminal.comm.command.filtered;

import com.drk.terminal.comm.TerminalCommander;
import com.drk.terminal.comm.command.Command;

import static com.drk.terminal.utils.StringUtil.EMPTY;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class ClearCommand implements Command {
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
