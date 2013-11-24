package com.drk.terminal.comm.command;

import com.drk.terminal.comm.TerminalCommander;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public interface Command {
    String isExecutable(TerminalCommander terminalProcess);
    String onExecute(TerminalCommander terminalProcess);
}
