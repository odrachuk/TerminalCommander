package com.drk.terminal.ui.activity.terminal;

import android.widget.TextView;
import com.drk.terminal.utils.StringUtil;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/19/13
 * Time: 4:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class CurrentPathLabel {
    private final TextView pathLabel;
    private String fullPath = StringUtil.PATH_SEPARATOR;

    public CurrentPathLabel(TextView ownLabel) {
        this.pathLabel = ownLabel;
    }

    public void setPath(String path) {
        fullPath = path;
        if (path != null) {
            pathLabel.setText(path);
        }
    }

    public String getCurrentLabel() {
        return pathLabel.getText().toString();
    }

    public TextView getOwnLabel() {
        return pathLabel;
    }

    public String getFullPath() {
        return fullPath;
    }
}
