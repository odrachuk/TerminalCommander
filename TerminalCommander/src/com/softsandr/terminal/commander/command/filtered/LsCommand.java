package com.softsandr.terminal.commander.command.filtered;

import com.softsandr.terminal.commander.TerminalCommander;
import com.softsandr.terminal.commander.command.local.LocalCommand;

import static com.drk.terminal.util.utils.StringUtil.EMPTY;

/**
 * Date: 11/28/13
 *
 * @author Drachuk O.V.
 */
public class LsCommand implements LocalCommand {

    @Override
    public String isExecutable(TerminalCommander terminalProcess) {
        return EMPTY;
    }

    @Override
    public String onExecute(TerminalCommander terminalProcess) {
        return "LS RESPONSE";
    }
}
