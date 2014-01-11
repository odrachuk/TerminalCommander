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
import android.os.AsyncTask;
import android.widget.TextView;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.terminal.TerminalActivityImpl;
import com.softsandr.terminal.model.listview.ListViewFiller;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.terminal.activity.terminal.LocationLabel;
import com.softsandr.terminal.activity.terminal.TerminalActivity;
import com.softsandr.terminal.activity.terminal.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class used for loading all items from specific right directory to list and initialization relative adapter
 */
public class LoadRightListTask extends AsyncTask<Void, Void, List<ListViewItem>> {
    private final TerminalActivity terminal;

    /**
     * Constructor by default. Please note, that terminal parameter should be an instance of {@link android.app.Activity}
     * @param terminalActivity  The terminal instance of {@link android.app.Activity}
     */
    public LoadRightListTask(TerminalActivityImpl terminalActivity) {
        this.terminal = terminalActivity;
    }

    @Override
    protected List<ListViewItem> doInBackground(Void... params) {
        List<ListViewItem> list = new ArrayList<ListViewItem>();
        ListViewFiller.fillListContent(terminal.getSortingStrategy(), list, terminal.getRightListSavedLocation());
        return list;
    }

    @Override
    protected void onPostExecute(List<ListViewItem> list) {
        TextView rightPathLabel = (TextView) ((Activity) terminal).findViewById(R.id.path_location_in_right);
        ListViewAdapter rightAdapter = new ListViewAdapter((TerminalActivityImpl) terminal, list,
                new LocationLabel(rightPathLabel),
                terminal.getRightHistoryLocationManager());
        terminal.setRightListAdapter(rightAdapter);
    }
}
