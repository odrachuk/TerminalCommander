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
    public static final String COPY_DIALOG_TAG = TerminalCpMvDialog.class.getCanonicalName() +
            TerminalCpMvDialog.TransferOperationType.COPY_OPERATION;
    public static final String MOVE_RENAME_DIALOG_TAG = TerminalCpMvDialog.class.getCanonicalName() +
            TerminalCpMvDialog.TransferOperationType.MOVE_OPERATION;
    public static final String MK_DIR_DIALOG_TAG = TerminalMkDirDialog.class.getCanonicalName();
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
        DialogFragment newFragment = TerminalCpMvDialog.newInstance(filePaths,
                directoryPath, currentPath,
                TerminalCpMvDialog.TransferOperationType.COPY_OPERATION);
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
        DialogFragment newFragment = TerminalCpMvDialog.newInstance(filePaths,
                directoryPath, currentPath,
                TerminalCpMvDialog.TransferOperationType.MOVE_OPERATION);
        newFragment.show(ft, MOVE_RENAME_DIALOG_TAG);
    }

    public static void showMkDirDialog(Activity activity, String currentPath, String destinationPath) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(MK_DIR_DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = TerminalMkDirDialog.newInstance(currentPath, destinationPath);
        newFragment.show(ft, MK_DIR_DIALOG_TAG);
    }

    public static void showDeleteDialog(Activity activity, ArrayList<ListViewItem> filePaths,
                                        String currentPath, String destinationPath) {
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
        DialogFragment newFragment = TerminalDeleteDialog.newInstance(filePaths, currentPath, destinationPath);
        newFragment.show(ft, DELETE_DIALOG_TAG);
    }
}
