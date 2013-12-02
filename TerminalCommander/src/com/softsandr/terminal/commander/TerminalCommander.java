package com.softsandr.terminal.commander;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import com.softsandr.terminal.commander.command.filtered.FilteredCommands;
import com.softsandr.terminal.commander.command.local.LocalCommands;
import com.softsandr.terminal.commander.controller.UiController;
import com.softsandr.terminal.commander.prompt.TerminalPrompt;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static com.drk.terminal.util.utils.StringUtil.EMPTY;
import static com.drk.terminal.util.utils.StringUtil.LINE_SEPARATOR;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class TerminalCommander {
    private static final String LOG_TAG = TerminalCommander.class.getSimpleName();
    private static final String SYSTEM_EXECUTOR = "sh";
    private final CommandResponseHandler mResponseHandler;
    private final TerminalPrompt mTerminalPrompt;
    private final UiController mUiController;
    private final TextView mTerminalOutView;
    private Process mExecutionProcess;
    private String mCommandText;

    /**
     * Create new input runtime comm
     */
    public TerminalCommander(UiController uiController) {
        mUiController = uiController;
        mTerminalOutView = uiController.getTerminalOutView();
        mTerminalPrompt = uiController.getPrompt();
        mResponseHandler = new CommandResponseHandler(uiController.getActivity().getResources(), mTerminalOutView);
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
        if (LocalCommands.isLocalCommand(mCommandText)) {
            String responseMessage = EMPTY;
            LocalCommands filteredCommand = LocalCommands.parseCommandTypeFromString(mCommandText);
            String isExecutableCallback = filteredCommand.getCommand().isExecutable(this);
            if (isExecutableCallback.isEmpty()) { // executable
                responseMessage += filteredCommand.getCommand().onExecute(this);
            } else { // not executable
                responseMessage += isExecutableCallback;
            }
            if (!responseMessage.isEmpty()) {
                String[] resultArray = new String[]{responseMessage};
                Bundle resultBundle = new Bundle();
                resultBundle.putStringArray(CommandResponseHandler.COMMAND_EXECUTION_RESPONSE_KEY, resultArray);
                resultBundle.putString(CommandResponseHandler.COMMAND_EXECUTION_STRING_KEY, commandText);
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
            // write output to listview
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
        resultBundle.putStringArray(CommandResponseHandler.COMMAND_EXECUTION_RESPONSE_KEY, resultArray);
        resultBundle.putString(CommandResponseHandler.COMMAND_EXECUTION_STRING_KEY, command);
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
        if (mExecutionProcess != null) {
            mExecutionProcess.destroy();
        }
    }

    public String getCommandText() {
        return mCommandText;
    }

    public String getProcessPath() {
        return mTerminalPrompt.getUserLocation();
    }
}