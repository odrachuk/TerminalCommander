/*******************************************************************************
 * Created by o.drachuk on 10/01/2014.
 *
 * Copyright Oleksandr Drachuk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.softsandr.terminal.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.terminal.activity.terminal.ActivePage;

import java.util.ArrayList;

/**
 * Utility class used for managing showing App dialogs
 */
public final class TerminalDialogUtil {
    public static final String COPY_DIALOG_TAG = TerminalCopyDialog.class.getCanonicalName() +
            ".COPY_DIALOG_TAG";
    public static final String MOVE_DIALOG_TAG = TerminalCopyDialog.class.getCanonicalName() +
            ".MOVE_DIALOG_TAG";
    public static final String RENAME_DIALOG_TAG = TerminalCopyDialog.class.getCanonicalName() +
            ".RENAME_DIALOG_TAG";
    public static final String MK_DIR_DIALOG_TAG = TerminalMkDirDialog.class.getCanonicalName();
    public static final String DELETE_DIALOG_TAG = TerminalDeleteDialog.class.getCanonicalName();
    public static final String HISTORY_DIALOG_TAG = TerminalHistoryDialog.class.getCanonicalName();

    private TerminalDialogUtil() {
    }

    public static void showCopyDialog(Activity activity, ArrayList<ListViewItem> filePaths, String dstPath) {
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
        DialogFragment newFragment = TerminalCopyDialog.newInstance(filePaths, dstPath);
        newFragment.show(ft, COPY_DIALOG_TAG);
    }

    public static void showMoveDialog(Activity activity, ArrayList<ListViewItem> filePaths,
                                      String curPath, String dstPath) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(MOVE_DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = TerminalMoveDialog.newInstance(filePaths, curPath, dstPath);
        newFragment.show(ft, MOVE_DIALOG_TAG);
    }

    public static void showRenameDialog(Activity activity, ListViewItem filePath,
                                        String curPath, String dstPath) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(RENAME_DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = TerminalRenameDialog.newInstance(filePath, curPath, dstPath);
        newFragment.show(ft, RENAME_DIALOG_TAG);
    }

    public static void showMkDirDialog(Activity activity, String curPath, String dstPath) {
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
        DialogFragment newFragment = TerminalMkDirDialog.newInstance(curPath, dstPath);
        newFragment.show(ft, MK_DIR_DIALOG_TAG);
    }

    public static void showDeleteDialog(Activity activity, ArrayList<ListViewItem> filePaths,
                                        String curPath, String dstPath) {
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
        DialogFragment newFragment = TerminalDeleteDialog.newInstance(filePaths, curPath, dstPath);
        newFragment.show(ft, DELETE_DIALOG_TAG);
    }

    public static void showHistoryDialog(Activity activity, String[] historyLocations,
                                         ActivePage activePage) {
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
}
