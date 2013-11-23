package com.drk.terminal.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.drk.terminal.R;
import com.drk.terminal.model.listview.ListViewItem;
import com.drk.terminal.ui.activity.terminal.TerminalActivity;
import com.drk.terminal.ui.command.DeleteFileCommand;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/21/13
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalMkDirectoryDialog extends DialogFragment {
    private static final String LOG_TAG = TerminalMkDirectoryDialog.class.getSimpleName();
    private static final String CUR_DIRECTORY_PATH = LOG_TAG + ".CUR_DIRECTORY_PATH";
    private static final String FILE_PATH_LIST = LOG_TAG + ".FILE_PATH_LIST";
    private ArrayList<ListViewItem> mFileAbsPathList;
    private String mCurrentAbsPath;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static TerminalMkDirectoryDialog newInstance(ArrayList<ListViewItem> fileAbsPathList, String currentPath) {
        TerminalMkDirectoryDialog f = new TerminalMkDirectoryDialog();
        // Supply arguments
        Bundle args = new Bundle();
        args.putParcelableArrayList(FILE_PATH_LIST, fileAbsPathList);
        args.putString(CUR_DIRECTORY_PATH, currentPath);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileAbsPathList = getArguments().getParcelableArrayList(FILE_PATH_LIST);
        mCurrentAbsPath = getArguments().getString(CUR_DIRECTORY_PATH);
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
            deleteDescribe.setText("Delete file \"" + mFileAbsPathList.get(0).getAbsPath() + "\"?");
        } else {
            // add attention about recursive deleting all included objects
            deleteDescribe.setText("Delete " + mFileAbsPathList.size() + " files?");
        }
        // Setup button's listener
        v.findViewById(R.id.terminal_delete_dialog_btn_ok).setOnClickListener(mOnClickListener);
        v.findViewById(R.id.terminal_delete_dialog_btn_cancel).setOnClickListener(mOnClickListener);
        return v;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            switch (viewId) {
                case R.id.terminal_delete_dialog_btn_ok:
                    new DeleteFileCommand((TerminalActivity) getActivity(),
                            mFileAbsPathList,
                            mCurrentAbsPath).onExecute();
                    TerminalMkDirectoryDialog.this.getDialog().cancel();
                    break;
                case R.id.terminal_delete_dialog_btn_cancel:
                    TerminalMkDirectoryDialog.this.getDialog().cancel();
                    break;
            }
        }
    };
}
