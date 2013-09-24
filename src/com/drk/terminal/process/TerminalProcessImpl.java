package com.drk.terminal.process;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.drk.terminal.util.StringConstant.LINE_SEPARATOR;

public class TerminalProcessImpl implements TerminalProcess {
    private static final String LOG_TAG = TerminalProcessImpl.class.getSimpleName();
    private static final String RESULT_KEY = "result";
    public static final String SYSTEM_EXECUTOR_PATH = "sh";
    private String mCommand;
    private Process mProcess;
    private BufferedReader mReader;
    private BufferedWriter mWriter;
    private TextView mTerminalOut;
    private ExecutorService mExecutorService;


    /**
     * Create new input runtime process
     * @param terminalOut TextView component for display command results
     */
    public TerminalProcessImpl(TextView terminalOut) {
        mTerminalOut = terminalOut;
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
    public void execCommand(String command) {
        mCommand = command;
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
                    Log.d(LOG_TAG, "execCommand -> " + mCommand);
                    if (mCommand != null) {
                        List<String> resultList = new LinkedList<String>();
                        if (mCommand.trim().equals("exit")) {
                            mWriter.write("exit" + LINE_SEPARATOR);
                        } else {
                            // write command to process
                            mWriter.write("((" + mCommand + ") && echo --EOF--) || echo --EOF--" + LINE_SEPARATOR);
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
                        mCommand = null;
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
                StringBuilder oldText = new StringBuilder(mTerminalOut.getText());
                // write result to console
                for (String s : results) {
                    oldText.append(LINE_SEPARATOR);
                    oldText.append(s);
                }
                mTerminalOut.setText(oldText.toString());
            }
        }
    };
}