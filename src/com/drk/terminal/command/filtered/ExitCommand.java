package com.drk.terminal.command.filtered;

import com.drk.terminal.command.Command;
import com.drk.terminal.process.TerminalProcess;
import com.drk.terminal.utils.StringUtils;

import static com.drk.terminal.utils.StringUtils.EMPTY;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 8:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExitCommand implements Command {
    @Override
    public String isExecutable(TerminalProcess terminalProcess) {
        return EMPTY;
    }

    @Override
    public String onExecute(TerminalProcess terminalProcess) {
        return EMPTY;
    }
}
