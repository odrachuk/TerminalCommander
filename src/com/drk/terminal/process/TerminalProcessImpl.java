package com.drk.terminal.process;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.drk.terminal.console.TerminalInput;
import com.drk.terminal.util.StringConstant;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TerminalProcessImpl implements TerminalProcess {
    private static final String LOG_TAG = TerminalProcessImpl.class.getSimpleName();
    private static final String RESULT_KEY = "result";
    public static final String SYSTEM_EXECUTOR_PATH = "sh";
    private String mInputText = StringConstant.EMPTY_CHAR;
    private Process mProcess;
    private BufferedReader mReader;
    private BufferedWriter mWriter;
    private TerminalInput mInput;
    private ExecutorService mExecutorService;
    private boolean inputReady;


    /**
     * Create new input runtime process
     * @throws IOException if an I/O error happens when create new process {@link ProcessBuilder#start()}
     */
    public TerminalProcessImpl(TerminalInput input) {
        mInput = input;
    }

    @Override
    public void startProcess() throws IOException {
        Log.d(LOG_TAG, "startProcess");
        ProcessBuilder builder = new ProcessBuilder(SYSTEM_EXECUTOR_PATH);
        builder.redirectErrorStream(true);
        try {
            mProcess = builder.start();
            mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            mWriter = new BufferedWriter(new OutputStreamWriter(mProcess.getOutputStream()));
            mExecutorService = Executors.newSingleThreadExecutor();
            mExecutorService.submit(new InputListener());
        } catch (IOException ex) {
            Log.d(LOG_TAG, "Exception when create console main process: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void execCommand() {
        for (String line : mInput.readLine()) {
            mInputText += line;
        }
        inputReady = true;
    }

    @Override
    public void stopProcess() {
        Log.d(LOG_TAG, "stopProcess");
        mProcess.destroy();
        mExecutorService.shutdownNow();
    }

    private class InputListener implements Runnable {

        @Override
        public void run() {
            for (;;) {
                // exec command
                try {
                    Log.d(LOG_TAG, "execCommand -> " + mInputText);
                    if (!mInputText.isEmpty() && inputReady) {
                        List<String> resultList = new LinkedList<String>();
                        if (mInputText.trim().equals("exit")) {
                            mWriter.write("exit\n");
                        } else {
                            // write command to process
                            mWriter.write("((" + mInputText + ") && echo --EOF--) || echo --EOF--\n");
                        }
                        mWriter.flush();
                        // read result of command from process
                        String out = mReader.readLine();
                        while (out != null && !out.trim().equals("--EOF--")) {
                            // write output to list
                            resultList.add(out);
                            out = mReader.readLine();
                        }

                        String[] resultArray = new String[resultList.size()];
                        int i = 0;
                        for (String s : resultList) {
                            resultArray[i] = s;
                            i++;
                        }
                        Bundle resultBundle = new Bundle();
                        resultBundle.putStringArray(RESULT_KEY, resultArray);
                        Message resultMessage = mResultHandler.obtainMessage();
                        resultMessage.setData(resultBundle);
                        mResultHandler.sendMessage(resultMessage);
                        mInputText = StringConstant.EMPTY_CHAR;
                        inputReady = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Handler mResultHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String[] results = msg.getData().getStringArray(RESULT_KEY);
            if (results != null && results.length != 0) {
                // write result to console
                for (String s : results) {
                    mInput.writeLine(s);
                }
                mInput.newLine();
            }
        }
    };
}