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
package com.softsandr.terminal.activity.terminal.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AdapterView;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.terminal.TerminalActivity;
import com.softsandr.terminal.activity.terminal.TerminalActivityImpl;
import com.softsandr.terminal.data.listview.ListViewItem;
import com.softsandr.utils.file.FileUtil;

import java.io.File;

/**
 * This class used for computation size of specific directory in interactive thread
 */
public class SizeComputationTask extends AsyncTask<ListViewItem, Void, Void> {
    private static final String LOG_TAG = SizeComputationTask.class.getSimpleName();
    private final TerminalActivity terminalActivity;
    private final AdapterView.AdapterContextMenuInfo info;
    private ProgressDialog progressDialog;

    /**
     * Start computation task for calculation size of directory
     * @param terminalActivity  the instance of {@link com.softsandr.terminal.activity.terminal.TerminalActivity}
     * @param info              the information object from floating context menu
     */
    public SizeComputationTask(TerminalActivity terminalActivity, AdapterView.AdapterContextMenuInfo info) {
        this.terminalActivity = terminalActivity;
        this.info = info;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Activity activity = (TerminalActivityImpl) terminalActivity;
        progressDialog= ProgressDialog.show(activity,
                activity.getString(R.string.context_menu_text_size),
                activity.getString(R.string.context_menu_text_size_computation),
                false);
    }

    @Override
    protected Void doInBackground(ListViewItem... params) {
        ListViewItem item = params[0];
        long size = 0l;
        try {
            size = FileUtil.getDirectorySize(new File(item.getAbsPath()));
        } catch (Exception e) {
            Log.e(LOG_TAG, "Computing directory size exception:", e);
        }
        item.setFileSize(size);
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        progressDialog.dismiss();
        terminalActivity.showFilePropDialog(info);
    }
}
