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
package com.softsandr.commander;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.softsandr.commander.process.CommanderProcess;

import java.io.IOException;

/**
 * The container of all ui elements from commander interface
 */
public class Commander {
    private static final String LOG_TAG = Commander.class.getSimpleName();

    private final Activity commanderActivity;
    private final EditText inputEditText;
    private final TextView outTextView;
    private final TextView promptTextView;
    private final String shellExecutor;

    private CommanderProcess process;
    private PromptCompoundString promptString;

    /**
     * Only possible public constructor
     * @param commanderActivity an implementation of {@link CommanderActivity} and {@link Activity}
     * @param inputEditText     an {@link android.widget.EditText} for input command on terminalActivity
     * @param outTextView       an {@link android.widget.TextView} for displaying commands results
     * @param promptTextView    an {@link android.widget.TextView} for displaying user prompt text
     */
    public Commander(Activity commanderActivity,
                     EditText inputEditText,
                     TextView outTextView,
                     TextView promptTextView,
                     String initialLocation,
                     String shellExecutor) {
        this.commanderActivity = commanderActivity;
        this.inputEditText = inputEditText;
        this.outTextView = outTextView;
        this.promptTextView = promptTextView;
        this.shellExecutor = shellExecutor;
        promptString = new PromptCompoundString(this);
        createProcess(initialLocation);
    }

    public Activity getActivity() {
        return commanderActivity;
    }

    public PromptCompoundString getPrompt() {
        return promptString;
    }

    public EditText getInputEditText() {
        return inputEditText;
    }

    public TextView getPromptTextView() {
        return promptTextView;
    }

    public TextView getOutTextView() {
        return outTextView;
    }

    /**
     * Get path to shell executor
     * @return
     */
    public String getShellExecutor() {
        return shellExecutor;
    }

    /**
     * Only when comm not started already
     * @param path The comm execution filesystem name
     */
    private void createProcess(String path) {
        Log.d(LOG_TAG, "createProcess");
        process = new CommanderProcess(this);
        try {
            process.startExecutionProcess(path);
        } catch (IOException e) {
            Toast.makeText(commanderActivity, "Can't start main execution process", Toast.LENGTH_LONG).show();
        }
    }

    public CommanderProcess getProcess() {
        return process;
    }

    /**
     * Used when needs interrupt current interactive command execution
     */
    public void cancelInteractiveCommand() {
        if (process.getInteractiveExecution() != null) {
            process.getInteractiveExecution().cancel();
        }
    }
}
