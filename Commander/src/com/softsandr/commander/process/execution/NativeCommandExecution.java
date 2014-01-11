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
import com.softsandr.commander.process.CommanderProcess;
import com.softsandr.commander.process.CommandsResponseHandler;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static com.softsandr.utils.string.StringUtil.LINE_SEPARATOR;

/**
 * This class used for execution all native linux commands
 */
public class NativeCommandExecution extends CommandExecution {
    private BufferedReader reader;
    private BufferedWriter writer;
    private List<String> resultList;

    public NativeCommandExecution(CommandsResponseHandler responseHandler,
                                     String commandText, String userLocation,
                                     CommanderProcess commanderProcess) {
        super(responseHandler, commandText, userLocation, commanderProcess);
    }

    @Override
    public void prepareExecution() {
        OutputStream stdIn = commanderProcess.getProcess().getOutputStream();
        InputStream stdOut = commanderProcess.getProcess().getInputStream();
        reader = new BufferedReader(new InputStreamReader(stdOut));
        writer = new BufferedWriter(new OutputStreamWriter(stdIn));
        resultList = new LinkedList<String>();
    }

    @Override
    public void makeExecution() {
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
    }

    @Override
    public void postExecution() {
        String[] resultArray = new String[resultList.size()];
        int i = 0;
        for (String s : resultList) {
            resultArray[i] = eraseAbsent(s);
            i++;
        }
        Bundle resultBundle = new Bundle();
        resultBundle.putStringArray(CommandsResponseHandler.COMMAND_EXECUTION_RESPONSE_KEY, resultArray);
        resultBundle.putString(CommandsResponseHandler.COMMAND_EXECUTION_STRING_KEY, commandText);
        Message resultMessage = responseHandler.obtainMessage();
        resultMessage.setData(resultBundle);
        responseHandler.sendMessage(resultMessage);
    }

    private String eraseAbsent(String message) {
        String result = message;
        if (message.contains("<stdin>[")) {
            result = message.substring(0, message.indexOf("<s") - 2)
                    + message.substring(message.indexOf("]:") + 1, message.length());
        }
        return result;
    }
}
