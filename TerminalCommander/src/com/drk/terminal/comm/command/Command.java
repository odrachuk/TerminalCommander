package com.drk.terminal.comm.command;

import com.drk.terminal.comm.TerminalCommander;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Command {
    String isExecutable(TerminalCommander terminalProcess);
    String onExecute(TerminalCommander terminalProcess);
}
