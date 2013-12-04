package com.softsandr.terminal.ui.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.terminal.ui.activity.terminal.TerminalActivity;

import java.util.ArrayList;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public final class TerminalDialogUtil {
    public static final String COPY_DIALOG_TAG = TerminalCpMvDialog.class.getCanonicalName() +
            TerminalCpMvDialog.TransferOperationType.COPY_OPERATION;
    public static final String MOVE_RENAME_DIALOG_TAG = TerminalCpMvDialog.class.getCanonicalName() +
            TerminalCpMvDialog.TransferOperationType.MOVE_OPERATION;
    public static final String MK_DIR_DIALOG_TAG = TerminalMkDirDialog.class.getCanonicalName();
    public static final String DELETE_DIALOG_TAG = TerminalDeleteDialog.class.getCanonicalName();
    public static final String HISTORY_DIALOG_TAG = TerminalHistoryDialog.class.getCanonicalName();
    public static final String APP_DIALOG_TAG = TerminalAppListDialog.class.getCanonicalName();

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

    public static void showHistoryDialog(Activity activity, String[] historyLocations,
                                         TerminalActivity.ActivePage activePage) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(HISTORY_DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = TerminalHistoryDialog.newInstance(activePage, historyLocations);
        newFragment.show(ft, HISTORY_DIALOG_TAG);
    }

    public static void showAppDialog(Activity activity, String fileName) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(APP_DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        DialogFragment newFragment = TerminalAppListDialog.newInstance(fileName);
        newFragment.show(ft, APP_DIALOG_TAG);
    }
}
