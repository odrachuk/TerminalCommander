package com.drk.terminal.process;

import android.util.Log;
import android.widget.TextView;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TerminalProcess {
    public static final String SYSTEM_EXECUTOR = "sh";
    private static final String LOG_TAG = TerminalProcess.class.getSimpleName();
    private TextView mTerminalOutView;
    private Process mProcess;
    private String mCommand;
    private Future mFuture;

    /**
     * Create new input runtime process
     *
     * @param terminalOutView TextView component for display command results
     */
    public TerminalProcess(TextView terminalOutView) {
        mTerminalOutView = terminalOutView;
    }

    public void startProcess(ExecutorService processExecutor, String processDirectory) throws IOException {
        Log.d(LOG_TAG, "startProcess");
        File pathDirectory = new File(processDirectory);
        if (pathDirectory.isDirectory()) {
            ProcessBuilder builder = new ProcessBuilder(SYSTEM_EXECUTOR);
            builder.redirectErrorStream(true);
            builder.directory(pathDirectory);
            try {
                mProcess = builder.start();
                mFuture = processExecutor.submit(new TerminalListener(TerminalProcess.this));
            } catch (IOException ex) {
                Log.d(LOG_TAG, "Exception when create console main process: " + ex.getMessage());
                throw ex;
            }
        } else {
            Log.d(LOG_TAG, "Wrong process init directory ");
        }
    }

    public void execCommand(String command) {
        mCommand = command;
    }

    public void clearCommand() {
        mCommand = null;
    }

    public void stopProcess() {
        Log.d(LOG_TAG, "stopProcess");
        if (!mFuture.isDone()) {
            mFuture.cancel(true);
        }
        mProcess.destroy();
    }

    public TextView getTerminalOutView() {
        return mTerminalOutView;
    }

    public String getCommand() {
        return mCommand;
    }

    public Process getProcess() {
        return mProcess;
    }
}