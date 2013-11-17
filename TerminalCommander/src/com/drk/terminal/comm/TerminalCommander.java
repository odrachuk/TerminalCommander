package com.drk.terminal.comm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import com.drk.terminal.comm.command.FilteredCommand;
import com.drk.terminal.comm.controller.UiController;
import com.drk.terminal.comm.prompt.TerminalPrompt;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static com.drk.terminal.utils.StringUtil.EMPTY;
import static com.drk.terminal.utils.StringUtil.LINE_SEPARATOR;

public class TerminalCommander {
    private static final String LOG_TAG = TerminalCommander.class.getSimpleName();
    private static final String RESULT_KEY = "result";
    private static final String SYSTEM_EXECUTOR = "sh";
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
    private TerminalPrompt mTerminalPrompt;
    private UiController mUiController;
    private TextView mTerminalOutView;
    private Process mExecutionProcess;
    private String mCommandText;

    /**
     * Create new input runtime comm
     */
    public TerminalCommander(UiController uiController) {
        mUiController = uiController;
        mTerminalOutView = uiController.getTerminalOutView();
        mTerminalPrompt = uiController.getPrompt();
    }

    public void startExecutionProcess(String path) throws IOException {
        Log.d(LOG_TAG, "startProcess");
        File pathDirectory = new File(path);
        if (pathDirectory.isDirectory()) {
            ProcessBuilder builder = new ProcessBuilder(SYSTEM_EXECUTOR);
            builder.redirectErrorStream(true);
            builder.directory(pathDirectory);
            try {
                mExecutionProcess = builder.start();
            } catch (IOException ex) {
                Log.e(LOG_TAG, "Exception when create console main comm: " + ex.getMessage());
                throw ex;
            }
        } else {
            Log.d(LOG_TAG, "Wrong comm init filesystem ");
            mTerminalOutView.setText("FAIL!");
        }
        mTerminalPrompt.setUserLocation(path);
    }

    public void execCommand(String commandText) {
        mCommandText = commandText;
        if (FilteredCommand.isFilteredCommand(mCommandText)) {
            String responseMessage = EMPTY;
            FilteredCommand filteredCommand = FilteredCommand.parseCommandTypeFromString(mCommandText);
            String isExecutableCallback = filteredCommand.getCommand().isExecutable(this);
            if (isExecutableCallback.isEmpty()) { // executable
                responseMessage += filteredCommand.getCommand().onExecute(this);
            } else { // not executable
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
                nativeExecute(mCommandText);
            } catch (IOException e) {
                mTerminalOutView.setText("FAIL!!!");
            }
        }
        mCommandText = null;
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
            // write command to comm
            writer.write("((" + command + ") && echo --EOF--) || echo --EOF--" + LINE_SEPARATOR);
        }
        writer.flush();
        // read result of command from comm
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

    public void onChangeDirectory(String targetPath) throws Exception {
        stopExecutionProcess();
        startExecutionProcess(targetPath);
    }

    public void onExit() {
        stopExecutionProcess();
        mUiController.getActivity().finish();
    }

    public void onClear() {
        mTerminalOutView.setText("");
        mUiController.setHideOutView(true);
    }

    public void stopExecutionProcess() {
        Log.d(LOG_TAG, "stopExecutionProcess");
        mExecutionProcess.destroy();
    }

    public String getCommandText() {
        return mCommandText;
    }

    public String getProcessPath() {
        return mTerminalPrompt.getUserLocation();
    }
}