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
import android.util.Log;
import android.view.View;
import com.softsandr.commander.Commander;
import com.softsandr.commander.commands.interactive.InteractiveCommands;
import com.softsandr.commander.commands.local.LocalCommands;
import com.softsandr.commander.process.execution.InteractiveCommandExecution;
import com.softsandr.commander.process.execution.LocalCommandExecution;
import com.softsandr.commander.process.execution.NativeCommandExecution;
import com.softsandr.terminal.R;

import java.io.File;
import java.io.IOException;

/**
 * The main class of console commander.
 */
public class CommanderProcess {
    private static final String LOG_TAG = CommanderProcess.class.getSimpleName();
    private final CommandsResponseHandler mResponseHandler;
    private final Commander commander;

    private InteractiveCommandExecution interactiveExecution;
    private ProcessBuilder processBuilder;
    private Process process;

    /**
     * Create new input runtime comm
     */
    public CommanderProcess(Commander commander) {
        this.commander = commander;
        mResponseHandler = new CommandsResponseHandler(commander);
        processBuilder = new ProcessBuilder(commander.getShellExecutor());
        processBuilder.redirectErrorStream(true);
    }

    public void setProcessDirectory(String path) throws IOException {
        Log.d(LOG_TAG, "setProcessDirectory");
        File pathDirectory = new File(path);
        if (pathDirectory.isDirectory()) {
            processBuilder.directory(pathDirectory);
        } else {
            Log.d(LOG_TAG, "Wrong initial process directory");
            commander.getOutTextView().setText(commander.getActivity().getString(R.string.bad_initial_directory));
        }
        commander.getPrompt().setUserLocation(path);
    }

    public void execCommand(String commandText) {
        Log.d(LOG_TAG, "execCommand(" + commandText + ")");
        try {
            // terminate if needs previous
            if (process != null) {
                process.destroy();
            }
            process = processBuilder.start();
            if (LocalCommands.isLocalCommand(commandText)) {
                new LocalCommandExecution(mResponseHandler, commandText,
                        commander.getPrompt().getUserLocation(), this).execute();
            } else if (InteractiveCommands.isInteractiveCommand(commandText)) {
                interactiveExecution = new InteractiveCommandExecution(mResponseHandler, commandText,
                        commander.getPrompt().getUserLocation(), this);
                interactiveExecution.execute();
            } else {
                new NativeCommandExecution(mResponseHandler, commandText,
                        commander.getPrompt().getUserLocation(), this).execute();
            }
        } catch (IOException ex) {
            Log.e(LOG_TAG, "Exception when start execution process: " + ex.getMessage());
            commander.showToast(R.string.cannot_start_main_process);
        }
    }

    public void onChangeDirectory(String targetPath) throws Exception {
        stopExecutionProcess();
        setProcessDirectory(targetPath);
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

    /**
     * Can be null.
     *
     * @return Local instance {@link InteractiveCommandExecution}
     */
    public InteractiveCommandExecution getInteractiveExecution() {
        return interactiveExecution;
    }
}