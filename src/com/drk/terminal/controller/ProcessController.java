package com.drk.terminal.controller;

import android.util.Log;
import android.widget.Toast;
import com.drk.terminal.process.TerminalProcess;

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
public class ProcessController {
    private static final String LOG_TAG = ProcessController.class.getSimpleName();

    private UiController mUiController;
    private TerminalProcess mProcess;

    public ProcessController(UiController uiController) {
        Log.d(LOG_TAG, "constructor");
        mUiController = uiController;
        createTerminalProcess("/");
    }

    /**
     * Only when process not started already
     * @param path The process execution directory name
     */
    private void createTerminalProcess(String path) {
        Log.d(LOG_TAG, "createTerminalProcess");
        mProcess = new TerminalProcess(mUiController.getActivity().getTerminalOutView(), mUiController.getPrompt());
        try {
            mProcess.startExecutionProcess(path);
        } catch (IOException e) {
            Toast.makeText(mUiController.getActivity(), "Can't start main process!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * When activity destroyed or when changed configuration should be recreated process instance
     */
    public void onDestroyExecutionProcess() {
        mProcess.stopExecutionProcess();
    }

    public TerminalProcess getProcess() {
        return mProcess;
    }
}
