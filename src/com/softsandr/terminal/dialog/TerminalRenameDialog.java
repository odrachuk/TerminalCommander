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
import com.softsandr.terminal.command.RenameFileCommand;
import com.softsandr.terminal.data.listview.ListViewItem;
import com.softsandr.terminal.data.preferences.PreferenceController;
import com.softsandr.utils.file.FileUtil;
import com.softsandr.utils.string.StringUtil;

/**
 * This class construct copy dialog
 */
public class TerminalRenameDialog extends DialogFragment {
    private static final String LOG_TAG = TerminalRenameDialog.class.getSimpleName();
    private static final String ITEM = LOG_TAG + ".ITEM";
    private static final String DST_DIR_PATH = LOG_TAG + ".DST_DIR_PATH";
    private static final String CUR_DIR_PATH = LOG_TAG + ".CUR_DIR_PATH";
    private ListViewItem item;
    private String curPath;
    private String dstPath;
    private EditText mInput;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            Dialog dialog = TerminalRenameDialog.this.getDialog();
            switch (viewId) {
                case R.id.terminal_cp_mv_dialog_btn_ok:
                    Editable editable = mInput.getText();
                    if (editable != null) {
                        String realTextFromInput = editable.toString();
                        if (realTextFromInput.endsWith(StringUtil.PATH_SEPARATOR)) {
                            realTextFromInput = realTextFromInput.substring(0, realTextFromInput.length() - 1);
                        }
                        if (FileUtil.isFilenameValid(curPath, realTextFromInput)) {
                            new RenameFileCommand((TerminalActivityImpl) getActivity(),
                                    item,
                                    realTextFromInput,
                                    dstPath,
                                    curPath).onExecute();
                        } else {
                            showNotCorrectFileName();
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
    static TerminalRenameDialog newInstance(ListViewItem fileAbsPath,
                                            String dstDirAbsPath, String currentAbsPath) {
        TerminalRenameDialog f = new TerminalRenameDialog();
        // Supply arguments
        Bundle args = new Bundle();
        args.putParcelable(ITEM, fileAbsPath);
        args.putString(DST_DIR_PATH, dstDirAbsPath);
        args.putString(CUR_DIR_PATH, currentAbsPath);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            item = bundle.getParcelable(ITEM);
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
            title.setText(getResources().getString(R.string.rename_title));
            describeText.setText(getString(R.string.dlg_rename_file)
                    + "\"" + truncateFileName() + "\"");
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
        String fileName = item.getAbsPath();
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

    private void showNotCorrectFileName() {
        Toast.makeText(getActivity(), getString(R.string.toast_not_correct_file_name),
                Toast.LENGTH_SHORT).show();
    }
}
