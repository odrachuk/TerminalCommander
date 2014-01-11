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
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.softsandr.terminal.activity.terminal.TerminalActivityImpl;
import com.softsandr.utils.string.StringUtil;
import com.softsandr.terminal.R;
import com.softsandr.terminal.command.MakeDirectoryCommand;

/**
 * This class contain logic that build correct make directory dialog
 */
public class TerminalMkDirDialog extends DialogFragment {
    private static final String LOG_TAG = TerminalMkDirDialog.class.getSimpleName();
    private static final String CUR_DIRECTORY_PATH = LOG_TAG + ".CUR_DIRECTORY_PATH";
    private static final String DST_DIRECTORY_PATH = LOG_TAG + ".DST_DIRECTORY_PATH";
    private String mCurrentAbsPath;
    private String mDestinationPath;
    private EditText mInput;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            switch (viewId) {
                case R.id.terminal_mk_dir_dialog_btn_ok:
                    Editable newFileName = mInput.getText();
                    new MakeDirectoryCommand((TerminalActivityImpl) getActivity(),
                            newFileName.toString(),
                            mCurrentAbsPath,
                            mDestinationPath).onExecute();
                    TerminalMkDirDialog.this.getDialog().cancel();
                    break;
                case R.id.terminal_mk_dir_dialog_btn_cancel:
                    TerminalMkDirDialog.this.getDialog().cancel();
                    break;
            }
        }
    };

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static TerminalMkDirDialog newInstance(String currentAbsPath, String destinationPath) {
        TerminalMkDirDialog f = new TerminalMkDirDialog();
        // Supply arguments
        Bundle args = new Bundle();
        args.putString(CUR_DIRECTORY_PATH, currentAbsPath);
        args.putString(DST_DIRECTORY_PATH, destinationPath);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentAbsPath = getArguments().getString(CUR_DIRECTORY_PATH);
        mDestinationPath = getArguments().getString(DST_DIRECTORY_PATH);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.terminal_mk_dir_dialog_layout, container, false);
        // Input
        String inputText = !mCurrentAbsPath.equals(StringUtil.PATH_SEPARATOR) ?
                mCurrentAbsPath + "/" : mCurrentAbsPath;
        mInput = (EditText) v.findViewById(R.id.terminal_mk_dir_dialog_input_element);
        mInput.setText(inputText);
        mInput.setSelection(inputText.length());
        // Setup button's listener
        v.findViewById(R.id.terminal_mk_dir_dialog_btn_ok).setOnClickListener(mOnClickListener);
        v.findViewById(R.id.terminal_mk_dir_dialog_btn_cancel).setOnClickListener(mOnClickListener);
        return v;
    }
}
