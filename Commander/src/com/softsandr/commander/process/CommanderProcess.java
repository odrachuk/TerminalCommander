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
package com.softsandr.commander.process;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import com.softsandr.commander.Commander;
import com.softsandr.commander.commands.interactive.InteractiveCommands;
import com.softsandr.commander.commands.local.LocalCommands;
import com.softsandr.commander.process.execution.InteractiveCommandExecution;
import com.softsandr.commander.process.execution.LocalCommandExecution;
import com.softsandr.commander.process.execution.NativeCommandExecution;

import java.io.File;
import java.io.IOException;

/**
 * The main class of console commander.
 */
public class CommanderProcess {
    private static final String LOG_TAG = CommanderProcess.class.getSimpleName();
    public static final String SYSTEM_EXECUTOR = "/system/bin/sh";
    private final CommandsResponseHandler mResponseHandler;
    private final Commander commander;
    private Process process;

    /**
     * Create new input runtime comm
     */
    public CommanderProcess(Commander commander) {
        this.commander = commander;
        mResponseHandler = new CommandsResponseHandler(commander);
    }

    public void startExecutionProcess(String path) throws IOException {
        Log.d(LOG_TAG, "newExecutionProcess");
        File pathDirectory = new File(path);
        if (pathDirectory.isDirectory()) {
            ProcessBuilder builder = new ProcessBuilder(SYSTEM_EXECUTOR);
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
            commander.getOutTextView().setText("The initial process directory wrong");
        }
        commander.getPrompt().setUserLocation(path);
    }

    public void execCommand(String commandText) {
        if (LocalCommands.isLocalCommand(commandText)) {
            new LocalCommandExecution(mResponseHandler, commandText,
                    commander.getPrompt().getUserLocation(), this).execute();
        } else if (InteractiveCommands.isInteractiveCommand(commandText)) {
            new InteractiveCommandExecution(mResponseHandler, commandText,
                    commander.getPrompt().getUserLocation(), this).execute();
        } else {
            new NativeCommandExecution(mResponseHandler, commandText,
                    commander.getPrompt().getUserLocation(), this).execute();
        }
    }

    public void onChangeDirectory(String targetPath) throws Exception {
        stopExecutionProcess();
        startExecutionProcess(targetPath);
    }

    public void onClear() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                commander.getOutTextView().setText("");
                commander.getOutTextView().setVisibility(View.GONE);
            }
        });
    }

    public void stopExecutionProcess() {
        Log.d(LOG_TAG, "stopExecutionProcess");
        if (process != null) {
            process.destroy();
        }
    }

    public Commander getCommander() {
        return commander;
    }

    public Process getProcess() {
        return process;
    }
}