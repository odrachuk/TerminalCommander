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

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/21/13
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalCopyDialog extends DialogFragment {
    private static final String LOG_TAG = TerminalCopyDialog.class.getSimpleName();
    private static final String FILE_PATH = LOG_TAG + ".FILE_PATH";
    private static final String DIRECTORY_PATH = LOG_TAG + ".DIRECTORY_PATH";
    private String mDstDirAbsPath;
    private String mFileAbsPath;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static TerminalCopyDialog newInstance(String fileAbsPath, String dstDirAbsPath) {
        TerminalCopyDialog f = new TerminalCopyDialog();
        // Supply arguments
        Bundle args = new Bundle();
        args.putString(FILE_PATH, fileAbsPath);
        args.putString(DIRECTORY_PATH, dstDirAbsPath);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileAbsPath = getArguments().getString(FILE_PATH);
        mDstDirAbsPath = getArguments().getString(DIRECTORY_PATH);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View v = inflater.inflate(R.layout.terminal_copy_dialog_layout, container, false);
        TextView describeText = (TextView) v.findViewById(R.id.terminal_copy_dialog_describe_copy_text);
        describeText.setText("Copy file \"" + mFileAbsPath + "\" to:");
        EditText dstInput = (EditText) v.findViewById(R.id.terminal_copy_dialog_destination_input);
        dstInput.setText(mDstDirAbsPath + "/");
        dstInput.setSelection(new String(mDstDirAbsPath + "/").length());
        return v;
    }
}
