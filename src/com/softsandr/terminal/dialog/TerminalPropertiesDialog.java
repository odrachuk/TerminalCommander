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
import android.widget.TextView;
import com.softsandr.terminal.R;
import com.softsandr.terminal.data.listview.ListViewItem;
import com.softsandr.utils.bool.BoolUtil;

/**
 * This class contain logic for constructing history locations dialog
 */
public class TerminalPropertiesDialog extends DialogFragment {
    private static final String LOG_TAG = TerminalPropertiesDialog.class.getSimpleName();
    private static final String ITEM = LOG_TAG + ".ITEM";

    private ListViewItem item;

    /**
     * Create a new instance of TerminalHistoryDialog, providing {@link com.softsandr.terminal.activity.terminal.ActivePage}
     * as argument.
     */
    static TerminalPropertiesDialog newInstance(ListViewItem item) {
        TerminalPropertiesDialog f = new TerminalPropertiesDialog();
        // Supply arguments
        Bundle args = new Bundle();
        args.putParcelable(ITEM, item);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            item = args.getParcelable(ITEM);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.terminal_dlg_properties_layout, container, false);
        if (v != null) {
            // init text elements
            TextView textPath = (TextView) v.findViewById(R.id.properties_toast_path);
            TextView textModified = (TextView) v.findViewById(R.id.properties_toast_modified);
            TextView textSize = (TextView) v.findViewById(R.id.properties_toast_size);
            TextView textCanRead = (TextView) v.findViewById(R.id.properties_toast_can_read);
            TextView textCanWrite = (TextView) v.findViewById(R.id.properties_toast_can_write);
            TextView textCanExecute = (TextView) v.findViewById(R.id.properties_toast_can_exec);
            // set text
            textPath.setText(getText(R.string.context_menu_text_path) + ": " + item.getAbsPath());
            textModified.setText(getText(R.string.context_menu_text_modified) + ": " + item.getFileModifyTime());
            textSize.setText(getText(R.string.context_menu_text_size) + ": " + item.getFileSize());
            textCanRead.setText(getText(R.string.context_menu_text_can_read) + ": " + BoolUtil.valToYesNo(item.isCanRead()));
            textCanWrite.setText(getText(R.string.context_menu_text_can_write) + ": " + BoolUtil.valToYesNo(item.isCanWrite()));
            textCanExecute.setText(getText(R.string.context_menu_text_can_exec) + ": " + BoolUtil.valToYesNo(item.isCanExecute()));
        }
        return v;
    }
}
