package com.drk.terminal.command.filtered;

import com.drk.terminal.command.FilteredCommand;
import com.drk.terminal.process.TerminalProcess;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 8:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClearCommand implements FilteredCommand {
    @Override
    public boolean isExecutable() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onExecute(TerminalProcess terminalProcess) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
