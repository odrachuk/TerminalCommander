package com.drk.terminal.ui.command;

import android.util.Log;
import android.widget.Toast;
import com.drk.terminal.model.listview.ListViewItem;
import com.drk.terminal.ui.activity.terminal.TerminalActivity;
import com.drk.terminal.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/20/13
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeleteFileCommand implements FileCommand {
    private static final String LOG_TAG = DeleteFileCommand.class.getSimpleName();
    private final TerminalActivity terminalActivity;
    private final List<ListViewItem> items;
    private final String currentPath;

    public DeleteFileCommand(TerminalActivity terminalActivity, List<ListViewItem> items, String currentPath) {
        this.terminalActivity = terminalActivity;
        this.items = items;
        this.currentPath = currentPath;
    }

    @Override
    public void onExecute() {
        if (items != null && !items.isEmpty()) {
            try {
                for (ListViewItem item : items) {
                    File file = new File(item.getAbsPath());
                    if (file.canRead()) {
                        FileUtil.deleteQuietly(file);
                        switch (terminalActivity.getActivePage()) {
                            case LEFT:
                                terminalActivity.getLeftListAdapter().changeDirectory(currentPath);
                                break;
                            case RIGHT:
                                terminalActivity.getRightListAdapter().changeDirectory(currentPath);
                                break;
                        }
                    } else {
                        Toast.makeText(terminalActivity, "No enough permission to delete source file " + file.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "copyFile", e);
            }
        } else {
            // todo show message about "Not object selected for copy operation"
            Toast.makeText(terminalActivity, "No object selected for copy operation", Toast.LENGTH_SHORT).show();
        }
    }
}