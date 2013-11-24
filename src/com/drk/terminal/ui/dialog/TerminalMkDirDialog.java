package com.drk.terminal.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.drk.terminal.R;
import com.drk.terminal.ui.activity.terminal.TerminalActivity;
import com.drk.terminal.ui.command.MakeDirectoryCommand;
import com.drk.terminal.utils.StringUtil;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
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
                    new MakeDirectoryCommand((TerminalActivity) getActivity(),
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
