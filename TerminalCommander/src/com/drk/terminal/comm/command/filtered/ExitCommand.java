package com.drk.terminal.comm.command.filtered;

import com.drk.terminal.comm.TerminalCommander;
import com.drk.terminal.comm.command.Command;
import com.drk.terminal.comm.command.FilteredCommand;

import static com.drk.terminal.utils.StringUtil.EMPTY;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 8:08 PM
 * To change this template use File | Settings | File Templates.
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
