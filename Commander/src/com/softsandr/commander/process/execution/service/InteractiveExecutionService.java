/*******************************************************************************
 * Created by o.drachuk on 13/01/2014. 
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
package com.softsandr.commander.process.execution.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

/**
 * This class used for execution interactive commands in background service
 */
public class InteractiveExecutionService extends Service {
    // Binder given to clients
    private final IBinder binder = new InteractiveExecutionBinder();
    // Random number generator
    private final Random generator = new Random();

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class InteractiveExecutionBinder extends Binder {

        public InteractiveExecutionService getService() {
            // Return this instance of LocalService so clients can call public methods
            return InteractiveExecutionService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /** Method for clients */
    public int getRandomNumber() {
        return generator.nextInt(100);
    }
}
