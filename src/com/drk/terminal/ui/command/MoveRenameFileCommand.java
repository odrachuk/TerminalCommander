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
public class MoveRenameFileCommand implements FileCommand {
    private static final String LOG_TAG = MoveRenameFileCommand.class.getSimpleName();
    private final TerminalActivity terminalActivity;
    private final List<ListViewItem> items;
    private final String destinationPath;
    private final String currentPath;

    public MoveRenameFileCommand(TerminalActivity terminalActivity,
                                 List<ListViewItem> items,
                                 String destinationPath,
                                 String currentPath) {
        this.terminalActivity = terminalActivity;
        this.items = items;
        this.destinationPath = destinationPath;
        this.currentPath = currentPath;
    }

    @Override
    public void onExecute() {
        if (items != null && !items.isEmpty()) {
        try {
            File dstDirectory = new File(destinationPath);
            if (dstDirectory.canWrite()) {
                for (ListViewItem item : items) {
                    File srcFile = new File(item.getAbsPath());
                    if (srcFile.canRead()) {
                        FileUtil.moveToDirectory(srcFile, dstDirectory, true);
                    } else {
                        Toast.makeText(terminalActivity, "No enough permission to read source file.", Toast.LENGTH_SHORT).show();
                    }
                }
                // clear selected
                makeClearSelection();
            } else {
                Toast.makeText(terminalActivity, "No enough permission to write in directory " + destinationPath + ".", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Move/Rename", e);
            Toast.makeText(terminalActivity, e.getMessage(), Toast.LENGTH_LONG).show();
            makeClearSelection();
        }
        } else {
            // todo show message about "Not object selected for copy operation"
            Toast.makeText(terminalActivity, "No object selected for copy operation", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeClearSelection() {
        switch (terminalActivity.getActivePage()) {
            case LEFT:
                terminalActivity.getRightListAdapter().changeDirectory(destinationPath);
                terminalActivity.getLeftListAdapter().clearSelection();
                terminalActivity.getLeftListAdapter().changeDirectory(currentPath);
                break;
            case RIGHT:
                terminalActivity.getLeftListAdapter().changeDirectory(destinationPath);
                terminalActivity.getRightListAdapter().clearSelection();
                terminalActivity.getRightListAdapter().changeDirectory(currentPath);
                break;
        }
    }
}
