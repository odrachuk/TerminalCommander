package com.drk.terminal.process;

import android.util.Log;
import android.widget.TextView;
import com.drk.terminal.process.exceptions.NotReadyExecutorException;
import com.drk.terminal.ui.TerminalPrompt;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TerminalProcess {
    private static final String SYSTEM_EXECUTOR = "sh";
    private static final String LOG_TAG = TerminalProcess.class.getSimpleName();
    private ExecutorService mExecutorService;
    private TerminalPrompt mTerminalPrompt;
    private TextView mTerminalOutView;
    private String mProcessDirectory;
    private Process mExecutionProcess;
    private String mCommand;
    private Future mFuture;

    /**
     * Create new input runtime process
     *
     * @param terminalOutView TextView component for display command results
     */
    public TerminalProcess(TextView terminalOutView, TerminalPrompt terminalPrompt) {
        mTerminalOutView = terminalOutView;
        mTerminalPrompt = terminalPrompt;
    }

    public void startExecutionProcess(ExecutorService processExecutor, String processDirectory) throws IOException {
        Log.d(LOG_TAG, "startProcess");
        mExecutorService = processExecutor;
        File pathDirectory = new File(processDirectory);
        if (pathDirectory.isDirectory()) {
            ProcessBuilder builder = new ProcessBuilder(SYSTEM_EXECUTOR);
            builder.redirectErrorStream(true);
            builder.directory(pathDirectory);
            try {
                mExecutionProcess = builder.start();
                mFuture = processExecutor.submit(new TerminalListener(TerminalProcess.this));
            } catch (IOException ex) {
                Log.e(LOG_TAG, "Exception when create console main process: " + ex.getMessage());
                throw ex;
            }
        } else {
            Log.d(LOG_TAG, "Wrong process init directory ");
            mTerminalOutView.setText("FAIL!");
        }
        mProcessDirectory = processDirectory;
        mTerminalPrompt.setCurrentPath(processDirectory);
    }

    public void onChangeDirectory(String targetPath) throws Exception {
        stopExecutionProcess(); // stop current process
        if (mExecutorService == null
                || mExecutorService.isShutdown()
                || mExecutorService.isTerminated()) {
            throw new NotReadyExecutorException();
        }
        startExecutionProcess(mExecutorService, targetPath);
    }

    public void execCommand(String command) {
        mCommand = command;
    }

    public void clearCommand() {
        mCommand = null;
    }

    public void stopExecutionProcess() {
        Log.d(LOG_TAG, "stopExecutionProcess");
        mFuture.cancel(true);
        mExecutionProcess.destroy();
    }

    public TextView getTerminalOutView() {
        return mTerminalOutView;
    }

    public Process getProcess() {
        return mExecutionProcess;
    }

    public String getCommand() {
        return mCommand;
    }

    public String getProcessDirectory() {
        return mProcessDirectory;
    }
}