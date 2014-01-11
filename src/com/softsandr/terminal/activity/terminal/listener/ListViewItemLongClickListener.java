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
package com.softsandr.terminal.activity.terminal.listener;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.terminal.activity.terminal.adapter.ListViewAdapter;
import com.softsandr.terminal.activity.terminal.async.SizeComputationTask;

/**
 * This class used uniform {@link com.softsandr.terminal.activity.terminal.listener.ListViewItemLongClickListener}
 * for specific panel of TerminalActivity.
 */
public class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener {
    private final ListViewAdapter adapter;
    private final Context context;

    public ListViewItemLongClickListener(Context context, ListViewAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ListViewItem item = adapter.getItem(position);
        if (item.isParentDots()) {
            Toast.makeText(context, "Directory path: " +
                    item.getAbsPath() + ".",
                    Toast.LENGTH_LONG).show();
        } else if (item.isDirectory()) {
            new SizeComputationTask(context).execute(item);
        }
        return true;
    }
}
