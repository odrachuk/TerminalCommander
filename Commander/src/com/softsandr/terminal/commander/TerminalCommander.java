/*******************************************************************************
 * Created by o.drachuk on 10/01/2014.
 *
 * Copyright Oleksandr Drachuk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.softsandr.terminal.commander;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.softsandr.terminal.commander.commands.interactive.InteractiveCommands;
import com.softsandr.terminal.commander.commands.local.LocalCommands;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static com.softsandr.utils.string.StringUtil.EMPTY;
import static com.softsandr.utils.string.StringUtil.LINE_SEPARATOR;

/**
 * The main class of console commander.
 */
public class TerminalCommander {
    private static final String LOG_TAG = TerminalCommander.class.getSimpleName();
    private static final String SYSTEM_EXECUTOR = "/system/bin/sh";
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
        DisplayMetrics dm = new DisplayMetrics();
        mUiController.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mResponseHandler = new CommandResponseHandler(
                dm.widthPixels,
                uiController.getActivity().getResources(), mTerminalOutView);
    }

    public void startExecutionProcess(String path) throws IOException {
        Log.d(LOG_TAG, "startExecutionProcess");
        File pathDirectory = new File(path);
        if (pathDirectory.isDirectory()) {
            ProcessBuilder builder = new ProcessBuilder(SYSTEM_EXECUTOR);
            builder.redirectErrorStream(true);
            builder.directory(pathDirectory);
            try {
                mExecutionProcess = builder.start();
            } catch (IOException ex) {
                Log.e(LOG_TAG, "Exception when start execution process: " + ex.getMessage());
                throw ex;
            }
        } else {
            Log.d(LOG_TAG, "Wrong initial process directory");
            mTerminalOutView.setText("The initial process directory wrong");
        }
        mTerminalPrompt.setUserLocation(path);
    }

    public void execCommand(String commandText) {
        mCommandText = commandText;
        if (LocalCommands.isLocalCommand(mCommandText)) {
            localExecute(mCommandText);
        } else if (InteractiveCommands.isInteractiveCommand(mCommandText)) {
            interactiveExecute(mCommandText);
        } else {
            nativeExecute(mCommandText);
        }
        mCommandText = null;
    }

    /**
     * Execute command using async executor
     * @param commandText The command from iput string
     */
    private void interactiveExecute(String commandText) {
        String[] resultArray = new String[]{"NOTHING!!!"};
        Bundle resultBundle = new Bundle();
        resultBundle.putStringArray(CommandResponseHandler.COMMAND_EXECUTION_RESPONSE_KEY, resultArray);
        resultBundle.putString(CommandResponseHandler.COMMAND_EXECUTION_STRING_KEY, commandText);
        Message resultMessage = mResponseHandler.obtainMessage();
        resultMessage.setData(resultBundle);
        mResponseHandler.sendMessage(resultMessage);
    }

    /**
     * Execute command using custom logic
     * @param commandText The command from input string
     */
    private void localExecute(String commandText) {
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
    }

    /**
     * Execute command using native Unix logic
     * @param commandText The command from input string
     * @throws IOException
     */
    private void nativeExecute(String commandText) {
        OutputStream stdIn = mExecutionProcess.getOutputStream();
        InputStream stdOut = mExecutionProcess.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdOut));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdIn));
        List<String> resultList = new LinkedList<String>();
        // execution
        try {
            if (commandText.trim().equals("exit")) {
                writer.write("exit" + LINE_SEPARATOR);
            } else {
                // write command to comm
                writer.write("((" + commandText + ") && echo --EOF--) || echo --EOF--" + LINE_SEPARATOR);
            }
            writer.flush();
            // read result of command from comm
            String out = reader.readLine();
            while (out != null && !out.trim().equals("--EOF--")) {
                // write output to listview
                resultList.add(out);
                out = reader.readLine();
            }
        } catch (IOException ex) {
            resultList.add(ex.getMessage());
        }
        // parsing responses
        String[] resultArray = new String[resultList.size()];
        int i = 0;
        for (String s : resultList) {
            resultArray[i] = eraseAbsent(s);
            i++;
        }
        Bundle resultBundle = new Bundle();
        resultBundle.putStringArray(CommandResponseHandler.COMMAND_EXECUTION_RESPONSE_KEY, resultArray);
        resultBundle.putString(CommandResponseHandler.COMMAND_EXECUTION_STRING_KEY, commandText);
        Message resultMessage = mResponseHandler.obtainMessage();
        resultMessage.setData(resultBundle);
        mResponseHandler.sendMessage(resultMessage);
    }

    private String eraseAbsent(String message) {
        String result = message;
        if (message.contains("<stdin>[")) {
            result = message.substring(0, message.indexOf("<s") - 2)
                    + message.substring(message.indexOf("]:") + 1, message.length());
        }
        return result;
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
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mTerminalOutView.setText("");
                mTerminalOutView.setVisibility(View.GONE);
            }
        });
    }

    public void stopExecutionProcess() {
        Log.d(LOG_TAG, "stopExecutionProcess");
        if (mExecutionProcess != null) {
            mExecutionProcess.destroy();
        }
    }

    public UiController getUiController() {
        return mUiController;
    }

    public String getCommandText() {
        return mCommandText;
    }

    public String getProcessPath() {
        return mTerminalPrompt.getUserLocation();
    }
}