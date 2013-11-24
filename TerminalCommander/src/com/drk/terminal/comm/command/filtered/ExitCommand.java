package com.drk.terminal.comm.command.filtered;

import com.drk.terminal.comm.TerminalCommander;
import com.drk.terminal.comm.command.Command;
import com.drk.terminal.comm.command.FilteredCommand;

import static com.drk.terminal.utils.StringUtil.EMPTY;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class ExitCommand implements Command {
    @Override
    public String isExecutable(TerminalCommander terminalProcess) {
        return EMPTY;
    }

    @Override
    public String onExecute(TerminalCommander terminalProcess) {
        terminalProcess.onExit();
        return FilteredCommand.EXIT.getText();
    }
}
