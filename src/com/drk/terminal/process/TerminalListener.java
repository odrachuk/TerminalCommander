package com.drk.terminal.process;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.drk.terminal.command.FilteredCommand;
import com.drk.terminal.utils.StringUtils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static com.drk.terminal.utils.StringUtils.EMPTY;
import static com.drk.terminal.utils.StringUtils.LINE_SEPARATOR;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/27/13
 * Time: 1:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalListener implements Runnable {
    private static final String LOG_TAG = TerminalListener.class.getSimpleName();
    private static final String RESULT_KEY = "result";
    Handler mResponseHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String[] results = msg.getData().getStringArray(RESULT_KEY);
            if (results != null && results.length != 0) {
                StringBuilder oldText = new StringBuilder(mTerminalProcess.getTerminalOutView().getText());
                // write result to console
                for (String s : results) {
                    oldText.append(LINE_SEPARATOR);
                    oldText.append(s);
                }
                mTerminalProcess.getTerminalOutView().setText(oldText.toString());
            }
        }
    };
    private TerminalProcess mTerminalProcess;

    public TerminalListener(TerminalProcess terminalProcess) {
        mTerminalProcess = terminalProcess;
    }

    @Override
    public void run() {
        while (true) {
            if (mTerminalProcess.getCommand() != null && !mTerminalProcess.getCommand().isEmpty()) {
                Log.d(LOG_TAG, "execCommand -> " + mTerminalProcess.getCommand());
                try {
                    onExecuteNewCommand(mTerminalProcess.getCommand());
                } catch (IOException e) {
                    // todo create new attempt maybe
                    Log.e(LOG_TAG, "Exception when execute new command");
                }
                mTerminalProcess.clearCommand();
            }
        }
    }

    private void onExecuteNewCommand(String command) throws IOException {
        if (FilteredCommand.isFilteredCommand(command)) {
            String responseMessage = StringUtils.EMPTY;
            FilteredCommand filteredCommand = FilteredCommand.getByName(command);
            String isExecutableCallback = filteredCommand.getCommand().isExecutable(mTerminalProcess);
            if (isExecutableCallback.isEmpty()) {
                responseMessage += filteredCommand.getCommand().onExecute(mTerminalProcess);
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
            nativeExecute(command);
        }
    }

    private void nativeExecute(String command) throws IOException {
        OutputStream stdIn = mTerminalProcess.getProcess().getOutputStream();
        InputStream stdOut = mTerminalProcess.getProcess().getInputStream();
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
}
