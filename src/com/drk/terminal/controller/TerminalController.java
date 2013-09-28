package com.drk.terminal.controller;

import android.util.Log;
import android.widget.Toast;
import com.drk.terminal.process.TerminalProcess;
import com.drk.terminal.ui.KeyboardListener;
import com.drk.terminal.ui.TerminalActivity;
import com.drk.terminal.utils.TerminalPrompt;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 8:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalController {
    private static final String LOG_TAG = TerminalController.class.getSimpleName();

    private TerminalPrompt mPrompt;
    private TerminalProcess mProcess;
    private TerminalActivity mActivity;
    private KeyboardListener mKeyboardListener;
    private ExecutorService mProcessExecutor;

    public TerminalController(TerminalActivity activity) {
        mActivity = activity;
        mKeyboardListener = new KeyboardListener(this);
        mProcessExecutor = Executors.newCachedThreadPool();
        mPrompt = new TerminalPrompt(activity);
        Log.d(LOG_TAG, "startConsole");
        createTerminalProcess("/");
    }

    /**
     * Only when process not started already
     * @param path The process execution directory name
     */
    private void createTerminalProcess(String path) {
        mProcess = new TerminalProcess(mActivity.getTerminalOutView(), mPrompt);
        try {
            mProcess.startExecutionProcess(mProcessExecutor, path);
        } catch (IOException e) {
            Toast.makeText(mActivity, "Can't start main process!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * When activity destroyed or when changed configuration should be recreated process instance
     */
    public void onDestroyController() {
        mProcess.stopExecutionProcess();
        mProcessExecutor.shutdownNow();
    }

    public TerminalPrompt getTerminalPrompt() {
        return mPrompt;
    }

    public TerminalProcess getProcess() {
        return mProcess;
    }

    public TerminalActivity getActivity() {
        return mActivity;
    }

    public KeyboardListener getKeyboardListener() {
        return mKeyboardListener;
    }
}
