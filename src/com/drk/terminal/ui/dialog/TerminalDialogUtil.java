package com.drk.terminal.ui.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/21/13
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public final class TerminalDialogUtil {
    public static final String COPY_DIALOG_TAG = TerminalDialogUtil.class.getCanonicalName();

    private TerminalDialogUtil() {
    }

    public static void showCopyDialog(Activity activity, String filePath, String directoryPath) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = TerminalCopyDialog.newInstance(filePath, directoryPath);
        newFragment.show(ft, COPY_DIALOG_TAG);
    }
}
