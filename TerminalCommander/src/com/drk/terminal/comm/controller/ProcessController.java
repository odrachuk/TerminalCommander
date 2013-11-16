package com.drk.terminal.comm.controller;

import android.util.Log;
import android.widget.Toast;
import com.drk.terminal.comm.TerminalCommander;

import java.io.IOException;

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
    private TerminalCommander mProcess;

    public ProcessController(UiController uiController) {
        Log.d(LOG_TAG, "constructor");
        mUiController = uiController;
        createTerminalProcess("/");
    }

    /**
     * Only when comm not started already
     * @param path The comm execution directory name
     */
    private void createTerminalProcess(String path) {
        Log.d(LOG_TAG, "createTerminalProcess");
        mProcess = new TerminalCommander(mUiController);
        try {
            mProcess.startExecutionProcess(path);
        } catch (IOException e) {
            Toast.makeText(mUiController.getActivity(), "Can't start main comm!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * When activity destroyed or when changed configuration should be recreated comm instance
     */
    public void onDestroyExecutionProcess() {
        mProcess.stopExecutionProcess();
    }

    public TerminalCommander getProcess() {
        return mProcess;
    }
}
