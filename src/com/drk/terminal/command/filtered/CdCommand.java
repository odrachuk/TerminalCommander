package com.drk.terminal.command.filtered;

import com.drk.terminal.command.Command;
import com.drk.terminal.process.TerminalProcess;
import com.drk.terminal.process.exceptions.NotReadyExecutorException;
import com.drk.terminal.utils.DirectoryUtils;
import com.drk.terminal.utils.StringUtils;

import java.io.IOException;

import static com.drk.terminal.utils.StringUtils.EMPTY;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 8:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class CdCommand implements Command {
    @Override
    public String isExecutable(TerminalProcess terminalProcess) {
        String callbackString = EMPTY;
        String allCommand = terminalProcess.getCommand().trim();
        if (allCommand.indexOf(' ') > 0) {
            String targetDirectory = allCommand.substring(allCommand.indexOf(' ') + 1, allCommand.length());
            if (DirectoryUtils.isDirectoryExist(terminalProcess.getProcessDirectory(), targetDirectory)) {
                if (!DirectoryUtils.canChangeDirectory(terminalProcess.getProcessDirectory(), targetDirectory)) {
                    callbackString += "Cannot read directory";
                }
            } else {
                callbackString += "Target is not directory";
            }
        } else {
            callbackString += "Not arguments";
        }
        return callbackString;
    }

    @Override
    public String onExecute(TerminalProcess terminalProcess) {
        String callbackString = EMPTY;
        try {
            String allCommand = terminalProcess.getCommand().trim();
            if (allCommand.indexOf(' ') > 0) {
                String targetDirectory = allCommand.substring(allCommand.indexOf(' ') + 1, allCommand.length());
                terminalProcess.onChangeDirectory(terminalProcess.getProcessDirectory()
                        + StringUtils.PATH_SEPARATOR + targetDirectory);
            } else {
                callbackString += "Not arguments";
            }
        } catch (Exception e) {
            if (e instanceof NotReadyExecutorException) {
                callbackString += "System Executor not ready.";
            } else if (e instanceof IOException) {
                callbackString += "System Executor exception";
            }
        }
        return callbackString;
    }
}
