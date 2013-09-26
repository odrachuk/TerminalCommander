package com.drk.terminal.command;

import com.drk.terminal.process.TerminalProcess;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Command {
    boolean isExecutable();
    void onExecute(TerminalProcess terminalProcess);
}
