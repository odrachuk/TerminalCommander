package com.drk.terminal.ui.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import com.drk.terminal.model.listview.ListViewItem;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/21/13
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public final class TerminalDialogUtil {
    public static final String COPY_DIALOG_TAG = TerminalCopyMoveDialog.class.getCanonicalName() + TerminalCopyMoveDialog.TransferOperationType.COPY_OPERATION;
    public static final String MOVE_RENAME_DIALOG_TAG = TerminalCopyMoveDialog.class.getCanonicalName() + TerminalCopyMoveDialog.TransferOperationType.MOVE_OPERATION;
    public static final String DELETE_DIALOG_TAG = TerminalDeleteDialog.class.getCanonicalName();

    private TerminalDialogUtil() {
    }

    public static void showCopyDialog(Activity activity, ArrayList<ListViewItem> filePaths,
                                      String directoryPath, String currentPath) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(COPY_DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = TerminalCopyMoveDialog.newInstance(filePaths,
                directoryPath, currentPath,
                TerminalCopyMoveDialog.TransferOperationType.COPY_OPERATION);
        newFragment.show(ft, COPY_DIALOG_TAG);
    }

    public static void showMoveRenameDialog(Activity activity, ArrayList<ListViewItem> filePaths,
                                            String directoryPath, String currentPath) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(MOVE_RENAME_DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = TerminalCopyMoveDialog.newInstance(filePaths,
                directoryPath, currentPath,
                TerminalCopyMoveDialog.TransferOperationType.MOVE_OPERATION);
        newFragment.show(ft, MOVE_RENAME_DIALOG_TAG);
    }

    public static void showDeleteDialog(Activity activity, ArrayList<ListViewItem> filePaths, String currentPath) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(DELETE_DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = TerminalDeleteDialog.newInstance(filePaths, currentPath);
        newFragment.show(ft, DELETE_DIALOG_TAG);
    }
}
