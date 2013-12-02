package com.softsandr.terminal.commander.command.local;

import com.softsandr.terminal.commander.TerminalCommander;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public interface LocalCommand {
    String isExecutable(TerminalCommander terminalProcess);
    String onExecute(TerminalCommander terminalProcess);
}
