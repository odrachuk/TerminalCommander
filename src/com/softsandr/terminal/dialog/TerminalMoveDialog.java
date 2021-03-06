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
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.*;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.terminal.TerminalActivityImpl;
import com.softsandr.terminal.command.MoveFileCommand;
import com.softsandr.terminal.data.listview.ListViewItem;
import com.softsandr.terminal.data.preferences.PreferenceController;
import com.softsandr.utils.string.StringUtil;

import java.util.ArrayList;

/**
 * This class construct copy dialog
 */
public class TerminalMoveDialog extends DialogFragment {
    private static final String LOG_TAG = TerminalMoveDialog.class.getSimpleName();
    private static final String ITEMS_LIST = LOG_TAG + ".ITEMS_LIST";
    private static final String DST_DIR_PATH = LOG_TAG + ".DST_DIR_PATH";
    private static final String CUR_DIR_PATH = LOG_TAG + ".CUR_DIR_PATH";
    private ArrayList<ListViewItem> itemsList;
    private String curPath;
    private String dstPath;
    private EditText mInput;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            Dialog dialog = TerminalMoveDialog.this.getDialog();
            switch (viewId) {
                case R.id.terminal_cp_mv_dialog_btn_ok:
                    Editable editable = mInput.getText();
                    if (editable != null) {
                        String realTextFromInput = editable.toString();
                        if (realTextFromInput.endsWith(StringUtil.PATH_SEPARATOR)) {
                            realTextFromInput = realTextFromInput.substring(0, realTextFromInput.length() - 1);
                        }
                        if (StringUtil.isCorrectPath(realTextFromInput)) {
                            String operationDestinationPath = dstPath;
                            if (!dstPath.equals(realTextFromInput)) {
                                operationDestinationPath = realTextFromInput;
                            }
                            new MoveFileCommand((TerminalActivityImpl) getActivity(),
                                    itemsList,
                                    operationDestinationPath,
                                    dstPath,
                                    curPath).onExecute();
                        } else {
                            showNotCorrectPathToast();
                        }
                    }
                    if (dialog != null) {
                        dialog.cancel();
                    }
                    break;
                case R.id.terminal_cp_mv_dialog_btn_cancel:
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
    static TerminalMoveDialog newInstance(ArrayList<ListViewItem> itemsList,
                                          String curPath, String dstPath) {
        TerminalMoveDialog f = new TerminalMoveDialog();
        // Supply arguments
        Bundle args = new Bundle();
        args.putParcelableArrayList(ITEMS_LIST, itemsList);
        args.putString(CUR_DIR_PATH, curPath);
        args.putString(DST_DIR_PATH, dstPath);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            itemsList = bundle.getParcelableArrayList(ITEMS_LIST);
            dstPath = bundle.getString(DST_DIR_PATH);
            curPath = bundle.getString(CUR_DIR_PATH);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        Window window;
        if (dialog != null) {
            window = dialog.getWindow();
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        View v = inflater.inflate(R.layout.terminal_dlg_cp_mv_rn_layout, container, false);
        // Input
        if (v != null) {
            mInput = (EditText) v.findViewById(R.id.terminal_cp_mv_dialog_input_element);
            // Dialog relative title
            TextView title = (TextView) v.findViewById(R.id.terminal_cp_mv_dialog_title);
            // Dialog attention text
            TextView describeText = (TextView) v.findViewById(R.id.terminal_cp_mv_dialog_describe_copy_text);
            title.setText(getResources().getString(R.string.move_title));
            if (itemsList.size() == 1) {
                describeText.setText(getString(R.string.dlg_move_file)
                        + "\"" + truncateFileName() + "\" " + getString(R.string.dlg_to));
            } else {
                describeText.setText(getString(R.string.dlg_move)
                        + itemsList.size() + getString(R.string.dlg_files_to));
            }
            String textForInput = !dstPath.equals(StringUtil.PATH_SEPARATOR) ?
                    dstPath + "/" : dstPath;
            mInput.setText(textForInput);
            mInput.setSelection(textForInput.length());
            // Setup button's listener
            v.findViewById(R.id.terminal_cp_mv_dialog_btn_ok).setOnClickListener(mOnClickListener);
            v.findViewById(R.id.terminal_cp_mv_dialog_btn_cancel).setOnClickListener(mOnClickListener);
        }
        // check and set default color
        v.setBackgroundColor(PreferenceController.loadTerminalBgColor(getActivity(),
                PreferenceManager.getDefaultSharedPreferences(getActivity())));
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

    private void showNotCorrectPathToast() {
        Toast.makeText(getActivity(), getString(R.string.toast_not_correct_path),
                Toast.LENGTH_SHORT).show();
    }
}
