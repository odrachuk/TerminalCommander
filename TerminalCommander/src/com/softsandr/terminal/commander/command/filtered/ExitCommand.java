package com.softsandr.terminal.commander.command.filtered;

import com.softsandr.terminal.commander.TerminalCommander;
import com.softsandr.terminal.commander.command.Command;
import com.softsandr.terminal.commander.command.FilteredCommand;

import static com.drk.terminal.util.utils.StringUtil.EMPTY;

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
