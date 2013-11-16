package com.drk.terminal.process.command.filtered;

import com.drk.terminal.process.TerminalProcess;
import com.drk.terminal.process.command.Command;

import static com.drk.terminal.utils.StringUtils.EMPTY;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 8:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClearCommand implements Command {
    @Override
    public String isExecutable(TerminalProcess terminalProcess) {
        return EMPTY;
    }

    @Override
    public String onExecute(TerminalProcess terminalProcess) {
        terminalProcess.onClear();
        return EMPTY;
    }
}
