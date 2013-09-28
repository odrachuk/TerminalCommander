package com.drk.terminal.process;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import com.drk.terminal.command.FilteredCommand;
import com.drk.terminal.ui.TerminalPrompt;
import com.drk.terminal.utils.StringUtils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static com.drk.terminal.utils.StringUtils.LINE_SEPARATOR;

public class TerminalProcess {
    private static final String LOG_TAG = TerminalProcess.class.getSimpleName();
    private static final String RESULT_KEY = "result";
    private static final String SYSTEM_EXECUTOR = "sh";
    private TerminalPrompt mTerminalPrompt;
    private TextView mTerminalOutView;
    private String mProcessDirectory;
    private Process mExecutionProcess;
    private String mCommand;

    Handler mResponseHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String[] results = msg.getData().getStringArray(RESULT_KEY);
            if (results != null && results.length != 0) {
                StringBuilder oldText = new StringBuilder(mTerminalOutView.getText());
                // write result to console
                for (String s : results) {
                    oldText.append(LINE_SEPARATOR);
                    oldText.append(s);
                }
                mTerminalOutView.setText(oldText.toString());
            }
        }
    };

    /**
     * Create new input runtime process
     *
     * @param terminalOutView TextView component for display command results
     */
    public TerminalProcess(TextView terminalOutView, TerminalPrompt terminalPrompt) {
        mTerminalOutView = terminalOutView;
        mTerminalPrompt = terminalPrompt;
    }

    public void startExecutionProcess(String processDirectory) throws IOException {
        Log.d(LOG_TAG, "startProcess");
        File pathDirectory = new File(processDirectory);
        if (pathDirectory.isDirectory()) {
            ProcessBuilder builder = new ProcessBuilder(SYSTEM_EXECUTOR);
            builder.redirectErrorStream(true);
            builder.directory(pathDirectory);
            try {
                mExecutionProcess = builder.start();
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
        stopExecutionProcess();
        startExecutionProcess(targetPath);
    }

    public void execCommand(String command) {
        mCommand = command;
        if (command != null && !command.isEmpty()) {
            if (FilteredCommand.isFilteredCommand(mCommand)) {
                String responseMessage = StringUtils.EMPTY;
                FilteredCommand filteredCommand = FilteredCommand.getByName(mCommand);
                String isExecutableCallback = filteredCommand.getCommand().isExecutable(this);
                if (isExecutableCallback.isEmpty()) {
                    responseMessage += filteredCommand.getCommand().onExecute(this);
                } else {
                    responseMessage += isExecutableCallback;
                }
                if (!responseMessage.isEmpty()) {
                    String[] resultArray = new String[]{responseMessage};
                    Bundle resultBundle = new Bundle();
                    resultBundle.putStringArray(RESULT_KEY, resultArray);
                    Message resultMessage = mResponseHandler.obtainMessage();
                    resultMessage.setData(resultBundle);
                    mResponseHandler.sendMessage(resultMessage);
                }
            } else {
                try {
                    nativeExecute(mCommand);
                } catch (IOException e) {
                    mTerminalOutView.setText("FAIL!!!");
                }
            }
            mCommand = null;
        }
    }

    private void nativeExecute(String command) throws IOException {
        OutputStream stdIn = mExecutionProcess.getOutputStream();
        InputStream stdOut = mExecutionProcess.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdOut));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdIn));
        List<String> resultList = new LinkedList<String>();
        if (command.trim().equals("exit")) {
            writer.write("exit" + LINE_SEPARATOR);
        } else {
            // write command to process
            writer.write("((" + command + ") && echo --EOF--) || echo --EOF--" + LINE_SEPARATOR);
        }
        writer.flush();
        // read result of command from process
        String out = reader.readLine();
        while (out != null && !out.trim().equals("--EOF--")) {
            // write output to list
            resultList.add(out);
            out = reader.readLine();
        }
        String[] resultArray = new String[resultList.size()];
        int i = 0;
        for (String s : resultList) {
            resultArray[i] = s;
            i++;
        }
        Bundle resultBundle = new Bundle();
        resultBundle.putStringArray(RESULT_KEY, resultArray);
        Message resultMessage = mResponseHandler.obtainMessage();
        resultMessage.setData(resultBundle);
        mResponseHandler.sendMessage(resultMessage);
    }

    public void stopExecutionProcess() {
        Log.d(LOG_TAG, "stopExecutionProcess");
        mExecutionProcess.destroy();
    }

    public String getCommand() {
        return mCommand;
    }

    public String getProcessDirectory() {
        return mProcessDirectory;
    }
}