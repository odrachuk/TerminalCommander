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

import android.app.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.terminal.adapter.ListViewAdapter;
import com.softsandr.terminal.data.listview.ListViewItem;
import com.softsandr.terminal.activity.terminal.ActivePage;
import com.softsandr.utils.bool.BoolUtil;

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

    /**
     * Used for show dialog about properties of file
     * @param activePage    the current active page {@link com.softsandr.terminal.activity.terminal.ActivePage}
     * @param info          the floating context menu selected item's info
     * @param leftAdapter   the left panel adapter {@link com.softsandr.terminal.activity.terminal.adapter.ListViewAdapter}
     * @param rightAdapter  the right panel adapter {@link com.softsandr.terminal.activity.terminal.adapter.ListViewAdapter}
     * @param activity      the foreground Activity {@link android.app.Activity}
     */
    public static void showPropertiesDialog(ActivePage activePage,
                                      AdapterView.AdapterContextMenuInfo info,
                                      ListViewAdapter leftAdapter,
                                      ListViewAdapter rightAdapter,
                                      Activity activity) {
        if (info != null) {
            ListViewItem item = null;
            if (activePage == ActivePage.LEFT) {
                item = leftAdapter.getItem(info.position);
            } else {
                item = rightAdapter.getItem(info.position);
            }
            if (item != null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                View layout = inflater.inflate(R.layout.terminal_dlg_properties_layout,
                        (ViewGroup) activity.findViewById(R.id.toast_layout_root));
                if (layout != null) {
                    // init text elements
                    TextView textType = (TextView) layout.findViewById(R.id.properties_toast_type);
                    TextView textPath = (TextView) layout.findViewById(R.id.properties_toast_path);
                    TextView textModified = (TextView) layout.findViewById(R.id.properties_toast_modified);
                    TextView textSize = (TextView) layout.findViewById(R.id.properties_toast_size);
                    TextView textCanRead = (TextView) layout.findViewById(R.id.properties_toast_can_read);
                    TextView textCanWrite = (TextView) layout.findViewById(R.id.properties_toast_can_write);
                    TextView textCanExecute = (TextView) layout.findViewById(R.id.properties_toast_can_exec);
                    // set text
                    textType.setText(activity.getText(R.string.context_menu_text_type) + ": " +
                            (item.isDirectory()? activity.getString(R.string.directory) : activity.getString(R.string.file)));
                    textPath.setText(activity.getText(R.string.context_menu_text_path) + ": " + item.getAbsPath());
                    textModified.setText(activity.getText(R.string.context_menu_text_modified) + ": " + item.getFileModifyTime());
                    textSize.setText(activity.getText(R.string.context_menu_text_size) + ": " + item.getFileSize());
                    textCanRead.setText(activity.getText(R.string.context_menu_text_can_read) + ": " + BoolUtil.valToYesNo(item.isCanRead()));
                    textCanWrite.setText(activity.getText(R.string.context_menu_text_can_write) + ": " + BoolUtil.valToYesNo(item.isCanWrite()));
                    textCanExecute.setText(activity.getText(R.string.context_menu_text_can_exec) + ": " + BoolUtil.valToYesNo(item.isCanExecute()));
                    // show dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(activity.getString(R.string.context_menu_properties));
                    builder.setView(layout);
                    final Dialog dialog = builder.create();
                    dialog.show();
                }
            }
        }
    }
}
