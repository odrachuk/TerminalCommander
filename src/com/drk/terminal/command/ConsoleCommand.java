package com.drk.terminal.command;

import android.util.Log;
import com.drk.terminal.process.TerminalProcess;

public class ConsoleCommand implements Command {
    public static final String LOG_TAG = ConsoleCommand.class.getSimpleName();
    private final TerminalProcess mProcess;

    public ConsoleCommand(TerminalProcess process) {
        mProcess = process;
    }

    @Override
    public void execute() {
        if (mProcess != null) {
            Log.d(LOG_TAG, "execute");
            mProcess.execCommand();
        }
    }
}