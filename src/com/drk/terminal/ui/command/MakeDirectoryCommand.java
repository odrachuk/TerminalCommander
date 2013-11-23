package com.drk.terminal.ui.command;

import android.util.Log;
import android.widget.Toast;
import com.drk.terminal.model.listview.ListViewItem;
import com.drk.terminal.ui.activity.terminal.TerminalActivity;
import com.drk.terminal.utils.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/20/13
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class MakeDirectoryCommand implements FileCommand {
    private static final String LOG_TAG = MakeDirectoryCommand.class.getSimpleName();
    private final TerminalActivity terminalActivity;
    private final String currentPath;
    private final String directoryPath;

    public MakeDirectoryCommand(TerminalActivity terminalActivity, String directoryPath, String currentPath) {
        this.terminalActivity = terminalActivity;
        this.directoryPath = directoryPath;
        this.currentPath = currentPath;
    }

    @Override
    public void onExecute() {
        try {
            FileUtil.forceMakeDir(new File(directoryPath));
            // clear selected and refresh directory after deleting
            makeClearSelection();
        } catch (IOException e) {
            Log.e(LOG_TAG, "makeDirectory", e);
            Toast.makeText(terminalActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void makeClearSelection() {
        switch (terminalActivity.getActivePage()) {
            case LEFT:
                terminalActivity.getLeftListAdapter().clearSelection();
                terminalActivity.getLeftListAdapter().changeDirectory(currentPath);
                break;
            case RIGHT:
                terminalActivity.getRightListAdapter().clearSelection();
                terminalActivity.getRightListAdapter().changeDirectory(currentPath);
                break;
        }
    }
}
