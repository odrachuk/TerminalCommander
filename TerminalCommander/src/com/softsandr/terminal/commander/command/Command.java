package com.softsandr.terminal.commander.command;

import com.softsandr.terminal.commander.TerminalCommander;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public interface Command {
    String isExecutable(TerminalCommander terminalProcess);
    String onExecute(TerminalCommander terminalProcess);
}
