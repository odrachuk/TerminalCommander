package com.drk.terminal.comm.command.filtered;

import com.drk.terminal.comm.TerminalCommander;
import com.drk.terminal.comm.command.Command;

import static com.drk.terminal.utils.StringUtil.EMPTY;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 8:07 PM
 * To change this template use File | Settings | File Templates.
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
