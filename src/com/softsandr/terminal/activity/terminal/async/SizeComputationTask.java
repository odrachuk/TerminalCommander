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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.softsandr.terminal.R;
import com.softsandr.utils.file.FileUtil;
import com.softsandr.terminal.model.listview.ListViewItem;

import java.io.File;

/**
 * This class used for computation size of specific directory in interactive thread
 */
public class SizeComputationTask extends AsyncTask<ListViewItem, Void, Long> {
    private static final String LOG_TAG = SizeComputationTask.class.getSimpleName();
    private Context context;

    public SizeComputationTask(Context context) {
        this.context = context;
    }

    @Override
    protected Long doInBackground(ListViewItem... params) {
        ListViewItem item = params[0];
        long size = 0l;
        try {
            size = FileUtil.getDirectorySize(new File(item.getAbsPath()));
        } catch (Exception e) {
            Log.e(LOG_TAG, "Computing directory size exception:", e);
        }
        return size;
    }

    @Override
    protected void onPostExecute(Long size) {
        Toast.makeText(context, context.getString(R.string.toast_dir_size) + ListViewItem.readableFileSize(size),
                Toast.LENGTH_SHORT).show();
    }
}
