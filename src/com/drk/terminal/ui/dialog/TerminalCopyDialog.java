package com.drk.terminal.ui.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.drk.terminal.R;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/21/13
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalCopyDialog extends DialogFragment {
    int mNum;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static TerminalCopyDialog newInstance(int num) {
        TerminalCopyDialog f = new TerminalCopyDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
