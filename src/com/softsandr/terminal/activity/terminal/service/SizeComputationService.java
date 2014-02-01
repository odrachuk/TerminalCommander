/*******************************************************************************
 * Created by o.drachuk on 01/02/2014. 
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
package com.softsandr.terminal.activity.terminal.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.softsandr.terminal.activity.terminal.TerminalActivityImpl;
import com.softsandr.terminal.data.listview.ListViewItem;
import com.softsandr.utils.file.FileUtil;

import java.io.File;

/**
 * This service compute size of specific directory in separate thread
 */
public class SizeComputationService extends IntentService {
    private static final String LOG_TAG = SizeComputationService.class.getSimpleName();
    public static final String INPUT_ITEM = LOG_TAG + ".INPUT_ITEM";
    public static final String OUTPUT_ITEM = LOG_TAG + ".OUTPUT_ITEM";

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public SizeComputationService() {
        super(LOG_TAG);
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "onHandleIntent");
        Intent responseIntent = new Intent(TerminalActivityImpl.HIDE_PROGRESS_INTENT);
        Bundle bundle = intent.getExtras();
        long size = 0l;
        if (bundle != null) {
            ListViewItem item = bundle.getParcelable(INPUT_ITEM);
            if (item != null) {
                try {
                    String path = item.getAbsPath();
                    if (path != null && !path.isEmpty()) {
                        size = FileUtil.getDirectorySize(new File(path));
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Computing directory size exception:", e);
                }
                item.setFileSize(size);
                responseIntent.putExtra(OUTPUT_ITEM, item);
            }
        }
        sendBroadcast(responseIntent);
    }
}
