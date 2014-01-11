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
package com.softsandr.terminal.commander.controller;

import android.util.Log;
import android.widget.Toast;
import com.softsandr.terminal.commander.TerminalCommander;

import java.io.IOException;

/**
 * The main controller as entry point to console logic
 */
public class ProcessController {
    private static final String LOG_TAG = ProcessController.class.getSimpleName();

    private UiController mUiController;
    private TerminalCommander mProcess;

    public ProcessController(UiController uiController, String path) {
        Log.d(LOG_TAG, "constructor");
        mUiController = uiController;
        createTerminalProcess(path);
    }

    /**
     * Only when comm not started already
     * @param path The comm execution filesystem name
     */
    private void createTerminalProcess(String path) {
        Log.d(LOG_TAG, "createTerminalProcess");
        mProcess = new TerminalCommander(mUiController);
        try {
            mProcess.startExecutionProcess(path);
        } catch (IOException e) {
            Toast.makeText(mUiController.getActivity(), "Can't start main execution process", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * When activity destroyed or when changed configuration should be recreated comm instance
     */
    public void onDestroyExecutionProcess() {
        mProcess.stopExecutionProcess();
    }

    public TerminalCommander getProcess() {
        return mProcess;
    }
}
