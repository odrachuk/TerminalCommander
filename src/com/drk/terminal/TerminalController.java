package com.drk.terminal;

import android.util.Log;
import android.widget.Toast;
import com.drk.terminal.process.TerminalProcess;
import com.drk.terminal.process.TerminalProcessImpl;
import com.drk.terminal.utils.AccountUtils;

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
    private TerminalInputListener mInputListener;
    private ExecutorService mProcessExecutor;

    public TerminalController(TerminalActivity activity) {
        mActivity = activity;
        mInputListener = new TerminalInputListener(this);
        mProcessExecutor = Executors.newCachedThreadPool();
        mPrompt = new TerminalPrompt(activity);
        Log.d(LOG_TAG, "startConsole");
        mProcess = new TerminalProcessImpl(activity.getTerminalOutView());
        try {
            mProcess.startProcess(mProcessExecutor, mPrompt.getCurrentPath());
        } catch (IOException e) {
            Toast.makeText(activity, "Can't start main process!", Toast.LENGTH_LONG).show();
        }
    }

    // todo: when directory changed, when username changed or when privileges changed
    public void onChangePrompt() {

    }

    // todo: when directory changed
    public void onChangedDirectory(String dirName) {

    }

    public void onProcessDead() {

    }

    public void onChangeProcess(String dirName) {

    }

    /**
     * When activity destroyed or when changed configuration should be recreated process instance
     */
    public void onStopProcess() {
        mProcess.stopProcess();
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

    public TerminalInputListener getInputListener() {
        return mInputListener;
    }
}
