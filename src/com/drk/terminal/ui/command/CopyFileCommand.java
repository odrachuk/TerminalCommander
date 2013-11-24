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
public class CopyFileCommand implements FileCommand {
    private static final String LOG_TAG = CopyFileCommand.class.getSimpleName();
    private final TerminalActivity terminalActivity;
    private final List<ListViewItem> items;
    private final String destinationPath;
    private final boolean pathChanged;

    public CopyFileCommand(TerminalActivity terminalActivity,
                           List<ListViewItem> items,
                           String destinationPath,
                           boolean pathChanged) {
        this.terminalActivity = terminalActivity;
        this.items = items;
        this.destinationPath = destinationPath;
        this.pathChanged = pathChanged;
    }

    @Override
    public void onExecute() {
        if (items != null && !items.isEmpty()) {
            try {
                File dstDirectory = new File(destinationPath);
                if (dstDirectory.exists()) {
                    if (dstDirectory.canWrite()) {
                        for (ListViewItem item : items) {
                            File srcFile = new File(item.getAbsPath());
                            if (srcFile.canRead()) {
                                if (item.isDirectory()) {
                                    FileUtil.copyDirectoryToDirectory(new File(item.getAbsPath()), new File(destinationPath));
                                } else {
                                    FileUtil.copyFileToDirectory(new File(item.getAbsPath()), new File(destinationPath), true);
                                }
                            } else {
                                Toast.makeText(terminalActivity, "No enough permission to read file " + srcFile + ".", Toast.LENGTH_SHORT).show();
                                makeClearSelection();
                            }
                        }
                        // clear selected
                        makeClearSelection();
                        makeRefreshDirectory();
                    } else {
                        Toast.makeText(terminalActivity, "No enough permission to write in directory " + destinationPath + ".", Toast.LENGTH_SHORT).show();
                        makeClearSelection();
                    }
                } else {
                    Toast.makeText(terminalActivity, "Destination directory " + destinationPath + " not exists.", Toast.LENGTH_SHORT).show();
                    makeClearSelection();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "copyFile", e);
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
                terminalActivity.getLeftListAdapter().clearSelection();
                break;
            case RIGHT:
                terminalActivity.getRightListAdapter().clearSelection();
                break;
        }
    }

    private void makeRefreshDirectory() {
        if (!pathChanged) {
            switch (terminalActivity.getActivePage()) {
                case LEFT:
                    terminalActivity.getRightListAdapter().changeDirectory(destinationPath);
                    break;
                case RIGHT:
                    terminalActivity.getLeftListAdapter().changeDirectory(destinationPath);
                    break;
            }
        }
    }
}
