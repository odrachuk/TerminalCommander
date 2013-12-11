package com.softsandr.terminal.commander.controller;

import android.util.Log;
import android.widget.Toast;
import com.softsandr.terminal.commander.TerminalCommander;

import java.io.IOException;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class ProcessController {
    private static final String LOG_TAG = ProcessController.class.getSimpleName();

    private UiController mUiController;
    private TerminalCommander mProcess;

    public ProcessController(UiController uiController, String path) {
        Log.d(LOG_TAG, "constructor");
        mUiController = uiController;
        createTerminalProcess(path);
    }

    /**
     * Only when comm not started already
     * @param path The comm execution filesystem name
     */
    private void createTerminalProcess(String path) {
        Log.d(LOG_TAG, "createTerminalProcess");
        mProcess = new TerminalCommander(mUiController);
        try {
            mProcess.startExecutionProcess(path);
        } catch (IOException e) {
            Toast.makeText(mUiController.getActivity(), "Can't start main execution process", Toast.LENGTH_LONG).show();
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
