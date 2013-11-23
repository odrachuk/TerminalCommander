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
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/21/13
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
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
            int viewId = v.getId();
            switch (viewId) {
                case R.id.terminal_cp_mv_dialog_btn_ok:
                    if (mOperationType.equals(TransferOperationType.COPY_OPERATION)) {
                        new CopyFileCommand((TerminalActivity) getActivity(),
                                mFileAbsPathList,
                                mDstDirAbsPath).onExecute(); // todo read actual dst path
                    } else if (mOperationType.equals(TransferOperationType.MOVE_OPERATION)) {
                        new MoveRenameFileCommand((TerminalActivity) getActivity(),
                                mFileAbsPathList,
                                mDstDirAbsPath, mCurrentAbsPath).onExecute(); // todo read actual dst path
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
                    describeText.setText("Copy file \"" + mFileAbsPathList.get(0).getAbsPath() + "\" to:");
                } else {
                    describeText.setText("Copy " + mFileAbsPathList.size() + " files to:");
                }
                break;
            case MOVE_OPERATION:
                title.setText(getResources().getString(R.string.move_string));
                if (mFileAbsPathList.size() == 1) {
                    describeText.setText("Move file \"" + mFileAbsPathList.get(0).getAbsPath() + "\" to:");
                } else {
                    describeText.setText("Move " + mFileAbsPathList.size() + " files to:");
                }
                break;
        }
        String dstText = !mDstDirAbsPath.equals(StringUtil.PATH_SEPARATOR) ?
                mDstDirAbsPath + "/" : mDstDirAbsPath;
        mInput.setText(dstText);
        mInput.setSelection(dstText.length());
        // Setup button's listener
        v.findViewById(R.id.terminal_cp_mv_dialog_btn_ok).setOnClickListener(mOnClickListener);
        v.findViewById(R.id.terminal_cp_mv_dialog_btn_cancel).setOnClickListener(mOnClickListener);
        return v;
    }

    public enum TransferOperationType {
        COPY_OPERATION,
        MOVE_OPERATION
    }
}
