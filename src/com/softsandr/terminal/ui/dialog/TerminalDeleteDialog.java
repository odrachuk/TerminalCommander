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
package com.softsandr.terminal.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.drk.terminal.util.utils.StringUtil;
import com.softsandr.terminal.R;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.terminal.ui.activity.terminal.TerminalActivity;
import com.softsandr.terminal.ui.command.DeleteFileCommand;

import java.util.ArrayList;

/**
 * This class construct delete dialog
 */
public class TerminalDeleteDialog extends DialogFragment {
    private static final String LOG_TAG = TerminalDeleteDialog.class.getSimpleName();
    private static final String CUR_DIRECTORY_PATH = LOG_TAG + ".CUR_DIRECTORY_PATH";
    private static final String DST_DIRECTORY_PATH = LOG_TAG + ".DST_DIRECTORY_PATH";
    private static final String FILE_PATH_LIST = LOG_TAG + ".FILE_PATH_LIST";
    private ArrayList<ListViewItem> mFileAbsPathList;
    private String mCurrentAbsPath;
    private String mDestinationPath;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            switch (viewId) {
                case R.id.terminal_delete_dialog_btn_ok:
                    new DeleteFileCommand((TerminalActivity) getActivity(),
                            mFileAbsPathList,
                            mCurrentAbsPath,
                            mDestinationPath).onExecute();
                    TerminalDeleteDialog.this.getDialog().cancel();
                    break;
                case R.id.terminal_delete_dialog_btn_cancel:
                    TerminalDeleteDialog.this.getDialog().cancel();
                    break;
            }
        }
    };

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static TerminalDeleteDialog newInstance(ArrayList<ListViewItem> fileAbsPathList,
                                            String currentPath, String destinationPath) {
        TerminalDeleteDialog f = new TerminalDeleteDialog();
        // Supply arguments
        Bundle args = new Bundle();
        args.putParcelableArrayList(FILE_PATH_LIST, fileAbsPathList);
        args.putString(CUR_DIRECTORY_PATH, currentPath);
        args.putString(DST_DIRECTORY_PATH, destinationPath);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileAbsPathList = getArguments().getParcelableArrayList(FILE_PATH_LIST);
        mCurrentAbsPath = getArguments().getString(CUR_DIRECTORY_PATH);
        mDestinationPath = getArguments().getString(DST_DIRECTORY_PATH);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View v = inflater.inflate(R.layout.terminal_delete_dialog_layout, container, false);
        // Set relative notification text
        TextView deleteDescribe = (TextView) v.findViewById(R.id.terminal_delete_dialog_describe_delete_text);
        if (mFileAbsPathList.size() == 1) {
            deleteDescribe.setText("Delete file \"" + truncateFileName() + "\"?");
        } else {
            // add attention about recursive deleting all included objects
            deleteDescribe.setText("Delete " + mFileAbsPathList.size() + " files?");
        }
        // Setup button's listener
        v.findViewById(R.id.terminal_delete_dialog_btn_ok).setOnClickListener(mOnClickListener);
        v.findViewById(R.id.terminal_delete_dialog_btn_cancel).setOnClickListener(mOnClickListener);
        return v;
    }

    private String truncateFileName() {
        String fileName = mFileAbsPathList.get(0).getAbsPath();
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
