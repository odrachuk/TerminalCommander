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
package com.softsandr.terminal.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.softsandr.terminal.activity.terminal.TerminalActivityImpl;
import com.softsandr.utils.string.StringUtil;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.terminal.ActivePage;

/**
 * This class contain logic for constructing history locations dialog
 */
public class TerminalHistoryDialog extends DialogFragment {
    private static final String LOG_TAG = TerminalHistoryDialog.class.getSimpleName();
    private static final String ACTIVE_PAGE = LOG_TAG + ".ACTIVE_PAGE";
    private static final String HISTORY_LOCATIONS = LOG_TAG + ".HISTORY_LOCATIONS";
    private ActivePage mActivePage;
    private String[] mHistoryLocations;
    private final AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String[] splitPath = mHistoryLocations[position].substring(1).split(StringUtil.PATH_SEPARATOR);
            if (mActivePage.equals(ActivePage.LEFT)) {
                ((TerminalActivityImpl) getActivity()).getLeftListAdapter().clearLocationHistory(splitPath);
                ((TerminalActivityImpl) getActivity()).getLeftListAdapter().changeDirectory(splitPath[splitPath.length - 1]);
            } else {
                ((TerminalActivityImpl) getActivity()).getRightListAdapter().clearLocationHistory(splitPath);
                ((TerminalActivityImpl) getActivity()).getRightListAdapter().changeDirectory(splitPath[splitPath.length - 1]);
            }
            getDialog().cancel();
        }
    };

    /**
     * Create a new instance of TerminalHistoryDialog, providing {@link com.softsandr.terminal.activity.terminal.ActivePage}
     * as argument.
     */
    static TerminalHistoryDialog newInstance(ActivePage activePage, String[] historyLocations) {
        TerminalHistoryDialog f = new TerminalHistoryDialog();
        // Supply arguments
        Bundle args = new Bundle();
        args.putSerializable(ACTIVE_PAGE, activePage);
        args.putStringArray(HISTORY_LOCATIONS, historyLocations);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivePage = (ActivePage) getArguments().getSerializable(ACTIVE_PAGE);
        mHistoryLocations = getArguments().getStringArray(HISTORY_LOCATIONS);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.terminal_history_dialog_layout, container, false);
        TextView title = (TextView) v.findViewById(R.id.terminal_history_dialog_title);
        title.setText(mActivePage.equals(ActivePage.LEFT)?
            "Left panel history" : "Right panel history");
        ListView list = (ListView) v.findViewById(R.id.history_dialog_list);
        ArrayAdapter listAdapter = new ArrayAdapter(getActivity(), R.layout.terminal_history_list_row_layout,
                mHistoryLocations);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(mItemClickListener);
        return v;
    }
}
