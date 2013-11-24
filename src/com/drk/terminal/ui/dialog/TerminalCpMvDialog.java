package com.drk.terminal.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.drk.terminal.R;
import com.drk.terminal.model.listview.ListViewItem;
import com.drk.terminal.ui.activity.terminal.TerminalActivity;
import com.drk.terminal.ui.command.CopyFileCommand;
import com.drk.terminal.ui.command.MoveRenameFileCommand;
import com.drk.terminal.utils.StringUtil;

import java.util.ArrayList;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class TerminalCpMvDialog extends DialogFragment {
    private static final String LOG_TAG = TerminalCpMvDialog.class.getSimpleName();
    private static final String FILE_PATH_LIST = LOG_TAG + ".FILE_PATH_LIST";
    private static final String DST_DIRECTORY_PATH = LOG_TAG + ".DST_DIRECTORY_PATH";
    private static final String CUR_DIRECTORY_PATH = LOG_TAG + ".CUR_DIRECTORY_PATH";
    private static final String OPERATION_TYPE = LOG_TAG + ".OPERATION_TYPE";
    private ArrayList<ListViewItem> mFileAbsPathList;
    private TransferOperationType mOperationType;
    private String mCurrentAbsPath;
    private String mDstDirAbsPath;
    private EditText mInput;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String realTextFromInput = mInput.getText().toString();
            if (realTextFromInput.lastIndexOf(StringUtil.PATH_SEPARATOR) == realTextFromInput.length() - 1) {
                realTextFromInput = realTextFromInput.substring(0, realTextFromInput.length() - 1);
            }
            int viewId = v.getId();
            switch (viewId) {
                case R.id.terminal_cp_mv_dialog_btn_ok:
                    if (mOperationType.equals(TransferOperationType.COPY_OPERATION)) {
                        if (!realTextFromInput.equals(mDstDirAbsPath)) {
                            new CopyFileCommand((TerminalActivity) getActivity(),
                                    mFileAbsPathList,
                                    realTextFromInput,
                                    true).onExecute();
                        } else {
                            new CopyFileCommand((TerminalActivity) getActivity(),
                                    mFileAbsPathList,
                                    mDstDirAbsPath,
                                    false).onExecute();
                        }
                    } else if (mOperationType.equals(TransferOperationType.MOVE_OPERATION)) {
                        if (!realTextFromInput.equals(mDstDirAbsPath)) {
                            new MoveRenameFileCommand((TerminalActivity) getActivity(),
                                    mFileAbsPathList,
                                    realTextFromInput,
                                    mCurrentAbsPath,
                                    true).onExecute();
                        } else {
                            new MoveRenameFileCommand((TerminalActivity) getActivity(),
                                    mFileAbsPathList,
                                    mDstDirAbsPath,
                                    mCurrentAbsPath,
                                    false).onExecute();
                        }
                    }
                    TerminalCpMvDialog.this.getDialog().cancel();
                    break;
                case R.id.terminal_cp_mv_dialog_btn_cancel:
                    TerminalCpMvDialog.this.getDialog().cancel();
                    break;
            }
        }
    };

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static TerminalCpMvDialog newInstance(ArrayList<ListViewItem> fileAbsPaths,
                                          String dstDirAbsPath, String currentAbsPath,
                                          TransferOperationType operationType) {
        TerminalCpMvDialog f = new TerminalCpMvDialog();
        // Supply arguments
        Bundle args = new Bundle();
        args.putParcelableArrayList(FILE_PATH_LIST, fileAbsPaths);
        args.putString(DST_DIRECTORY_PATH, dstDirAbsPath);
        args.putString(CUR_DIRECTORY_PATH, currentAbsPath);
        args.putSerializable(OPERATION_TYPE, operationType);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileAbsPathList = getArguments().getParcelableArrayList(FILE_PATH_LIST);
        mDstDirAbsPath = getArguments().getString(DST_DIRECTORY_PATH);
        mCurrentAbsPath = getArguments().getString(CUR_DIRECTORY_PATH);
        mOperationType = (TransferOperationType) getArguments().getSerializable(OPERATION_TYPE);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View v = inflater.inflate(R.layout.terminal_cp_mv_dialog_layout, container, false);
        // Input
        mInput = (EditText) v.findViewById(R.id.terminal_cp_mv_dialog_input_element);
        // Dialog relative title
        TextView title = (TextView) v.findViewById(R.id.terminal_cp_mv_dialog_title);
        // Dialog attention text
        TextView describeText = (TextView) v.findViewById(R.id.terminal_cp_mv_dialog_describe_copy_text);

        switch (mOperationType) {
            case COPY_OPERATION:
                if (mFileAbsPathList.size() == 1) {
                    describeText.setText("Copy file \"" + truncateFileName() + "\" to:");
                } else {
                    describeText.setText("Copy " + mFileAbsPathList.size() + " files to:");
                }
                break;
            case MOVE_OPERATION:
                title.setText(getResources().getString(R.string.move_string));
                if (mFileAbsPathList.size() == 1) {
                    describeText.setText("Move file \"" + truncateFileName() + "\" to:");
                } else {
                    describeText.setText("Move " + mFileAbsPathList.size() + " files to:");
                }
                break;
        }
        String textForInput = !mDstDirAbsPath.equals(StringUtil.PATH_SEPARATOR) ?
                mDstDirAbsPath + "/" : mDstDirAbsPath;
        mInput.setText(textForInput);
        mInput.setSelection(textForInput.length());
        // Setup button's listener
        v.findViewById(R.id.terminal_cp_mv_dialog_btn_ok).setOnClickListener(mOnClickListener);
        v.findViewById(R.id.terminal_cp_mv_dialog_btn_cancel).setOnClickListener(mOnClickListener);
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

    public enum TransferOperationType {
        COPY_OPERATION,
        MOVE_OPERATION
    }
}
