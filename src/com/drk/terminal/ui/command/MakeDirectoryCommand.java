package com.drk.terminal.ui.command;

import android.util.Log;
import android.widget.Toast;
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
    private final String directoryName;
    private final String currentPath;

    public MakeDirectoryCommand(TerminalActivity terminalActivity, String directoryName, String currentPath) {
        this.terminalActivity = terminalActivity;
        this.directoryName = directoryName;
        this.currentPath = currentPath;
    }

    @Override
    public void onExecute() {
        try {
            FileUtil.forceMakeDir(new File(directoryName));
            // clear selected and refresh directory after deleting
            makeClearSelection();
        } catch (IOException e) {
            Log.e(LOG_TAG, "makeDirectory", e);
            Toast.makeText(terminalActivity, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void makeClearSelection() {
        // todo if other panel is same as this, should be also refreshed
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
