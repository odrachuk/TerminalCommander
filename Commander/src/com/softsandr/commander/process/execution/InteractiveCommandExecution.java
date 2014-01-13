/*******************************************************************************
 * Created by o.drachuk on 11/01/2014. 
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
package com.softsandr.commander.process.execution;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.softsandr.commander.commands.interactive.InteractiveCommands;
import com.softsandr.commander.process.CommanderProcess;
import com.softsandr.commander.process.CommandsResponseHandler;

import java.io.*;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import static com.softsandr.utils.string.StringUtil.LINE_SEPARATOR;

/**
 * This class used for execution all commands described in
 * {@link com.softsandr.commander.commands.interactive.InteractiveCommands}
 */
public class InteractiveCommandExecution extends CommandExecution {
    private static final String LOG_TAG = InteractiveCommandExecution.class.getSimpleName();
    private final int DELAY = 1000; // delay for sec.
    private final int PERIOD = 5000; // repeat every 5 sec.
    private boolean onePostIgnored;
    private LinkedList<String> resultList;
    private InteractiveCommands command;
    private Timer timer;

    public InteractiveCommandExecution(CommandsResponseHandler responseHandler,
                                       String commandText, String userLocation,
                                       CommanderProcess commanderProcess) {
        super(responseHandler, commandText, userLocation, commanderProcess);
    }

    @Override
    public void prepareExecution() {
        timer = new Timer();
        commanderProcess.stopExecutionProcess();
        command = InteractiveCommands.parseCommandTypeFromString(commandText);
    }

    @Override
    public void makeExecution() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    Process process = newExecutionProcess();
                    if (process != null) {
                        OutputStream stdIn = process.getOutputStream();
                        InputStream stdOut = process.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(stdOut));
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdIn));
                        resultList = new LinkedList<String>();
                        // write command to stdIn
                        writer.write("((" + command.getSpecificText() + ") && echo --EOF--) || echo --EOF--" + LINE_SEPARATOR);
                        writer.flush();
                        // read result from strOut
                        String out = reader.readLine();
                        while (out != null && !out.trim().equals("--EOF--")) {
                            // save one row result to list
                            if (!out.isEmpty()) {
                                resultList.add(out);
                            }
                            out = reader.readLine();
                        }
                        process.destroy();
                    }
                } catch (IOException ex) {
                    resultList.add(ex.getMessage());
                }
                postExecution();
            }
        }, DELAY, PERIOD);
    }

    @Override
    public void postExecution() {
        // in first call we ignore logic of method
        if (!onePostIgnored) {
            onePostIgnored = true;
            Bundle resultBundle = new Bundle();
            resultBundle.putBoolean(CommandsResponseHandler.COMMAND_EXECUTION_HIDE_KEY, true);
            Message resultMessage = responseHandler.obtainMessage();
            resultMessage.setData(resultBundle);
            responseHandler.sendMessage(resultMessage);
        } else {
            String[] resultArray = new String[resultList.size()];
            int i = 0;
            for (String s : resultList) {
                resultArray[i] = s;
                i++;
            }
            Bundle resultBundle = new Bundle();
            resultBundle.putStringArray(CommandsResponseHandler.COMMAND_EXECUTION_RESPONSE_KEY, resultArray);
            resultBundle.putString(CommandsResponseHandler.COMMAND_EXECUTION_STRING_KEY, commandText);
            resultBundle.putBoolean(CommandsResponseHandler.COMMAND_EXECUTION_CLEAR_KEY, true);
            Message resultMessage = responseHandler.obtainMessage();
            resultMessage.setData(resultBundle);
            responseHandler.sendMessage(resultMessage);
        }
    }

    private Process newExecutionProcess() throws IOException {
        Log.d(LOG_TAG, "newExecutionProcess");
        Process process = null;
        File pathDirectory = new File(commanderProcess.getCommander().getPrompt().getUserLocation());
        if (pathDirectory.isDirectory()) {
            ProcessBuilder builder = new ProcessBuilder(CommanderProcess.SYSTEM_EXECUTOR);
            builder.redirectErrorStream(true);
            builder.directory(pathDirectory);
            try {
                process = builder.start();
            } catch (IOException ex) {
                Log.e(LOG_TAG, "Exception when start execution process: " + ex.getMessage());
                throw ex;
            }
        } else {
            Log.d(LOG_TAG, "Wrong initial process directory");
        }
        return process;
    }

    /**
     * Canceled timer and recreate main Process
     */
    public void cancel() {
        timer.cancel();
        try {
            commanderProcess.startExecutionProcess(commanderProcess.getCommander().getPrompt().getUserLocation());
        } catch (IOException e) {
            Toast.makeText(commanderProcess.getCommander().getActivity(),
                    "Can't start main execution process.", Toast.LENGTH_LONG).show();
        }
    }
}
