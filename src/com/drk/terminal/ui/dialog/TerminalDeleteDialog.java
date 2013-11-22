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
public class TerminalDeleteDialog extends DialogFragment {
    private static final String LOG_TAG = TerminalDeleteDialog.class.getSimpleName();
    private static final String FILE_PATH = LOG_TAG + ".FILE_PATH";
    private String mFileAbsPath;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static TerminalDeleteDialog newInstance(String fileAbsPath) {
        TerminalDeleteDialog f = new TerminalDeleteDialog();
        // Supply arguments
        Bundle args = new Bundle();
        args.putString(FILE_PATH, fileAbsPath);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileAbsPath = getArguments().getString(FILE_PATH);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View v = inflater.inflate(R.layout.terminal_copy_dialog_layout, container, false);
        return v;
    }
}
