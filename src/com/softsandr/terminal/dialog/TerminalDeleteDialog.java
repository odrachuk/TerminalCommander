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

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.softsandr.terminal.activity.terminal.TerminalActivityImpl;
import com.softsandr.utils.string.StringUtil;
import com.softsandr.terminal.R;
import com.softsandr.terminal.data.listview.ListViewItem;
import com.softsandr.terminal.command.DeleteFileCommand;

import java.util.ArrayList;

/**
 * This class construct delete dialog
 */
public class TerminalDeleteDialog extends DialogFragment {
    private static final String LOG_TAG = TerminalDeleteDialog.class.getSimpleName();
    private static final String CUR_DIR_PATH = LOG_TAG + ".CUR_DIR_PATH";
    private static final String DST_DIR_PATH = LOG_TAG + ".DST_DIR_PATH";
    private static final String ITEMS_LIST = LOG_TAG + ".ITEMS_LIST";
    private ArrayList<ListViewItem> itemsList;
    private String curPath;
    private String dstPath;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            Dialog dialog = TerminalDeleteDialog.this.getDialog();
            switch (viewId) {
                case R.id.terminal_delete_dialog_btn_ok:
                    new DeleteFileCommand((TerminalActivityImpl) getActivity(),
                            itemsList,
                            curPath,
                            dstPath).onExecute();
                    if (dialog != null) {
                        dialog.cancel();
                    }
                    break;
                case R.id.terminal_delete_dialog_btn_cancel:
                    if (dialog != null) {
                        dialog.cancel();
                    }
                    break;
            }
        }
    };

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static TerminalDeleteDialog newInstance(ArrayList<ListViewItem> fileAbsPathList,
                                            String curPath, String dstPath) {
        TerminalDeleteDialog f = new TerminalDeleteDialog();
        // Supply arguments
        Bundle args = new Bundle();
        args.putParcelableArrayList(ITEMS_LIST, fileAbsPathList);
        args.putString(CUR_DIR_PATH, curPath);
        args.putString(DST_DIR_PATH, dstPath);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            itemsList = getArguments().getParcelableArrayList(ITEMS_LIST);
            curPath = getArguments().getString(CUR_DIR_PATH);
            dstPath = getArguments().getString(DST_DIR_PATH);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        View v = inflater.inflate(R.layout.terminal_delete_dialog_layout, container, false);
        if (v != null) {
            // Set relative notification text
            TextView deleteDescribe = (TextView) v.findViewById(R.id.terminal_delete_dialog_describe_delete_text);
            if (itemsList.size() == 1) {
                deleteDescribe.setText(getString(R.string.dlg_delete_file) + "\"" + truncateFileName() + "\"?");
            } else {
                // add attention about recursive deleting all included objects
                deleteDescribe.setText(getString(R.string.dlg_delete)
                        + itemsList.size() + getString(R.string.dlg_files) + "?");
            }
            // Setup button's listener
            v.findViewById(R.id.terminal_delete_dialog_btn_ok).setOnClickListener(mOnClickListener);
            v.findViewById(R.id.terminal_delete_dialog_btn_cancel).setOnClickListener(mOnClickListener);
        }
        return v;
    }

    private String truncateFileName() {
        String fileName = itemsList.get(0).getAbsPath();
        int fileNameLength = fileName.length();
        if (fileNameLength > 26) {
            String lastCorrectPath = fileName.substring(fileName.lastIndexOf(StringUtil.PATH_SEPARATOR));
            if (lastCorrectPath.length() > 26) {
                fileName = "..." + fileName.substring(fileNameLength - 22);
            } else {
                int firstSeparator = lastCorrectPath.indexOf(StringUtil.PATH_SEPARATOR);
                if (firstSeparator > 4) {
                    fileName = "..." + lastCorrectPath.substring(firstSeparator);
                } else {
                    fileName = "..." + lastCorrectPath;
                }
            }
        }
        return fileName;
    }
}
