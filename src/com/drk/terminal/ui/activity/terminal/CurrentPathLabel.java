package com.drk.terminal.ui.activity.terminal;

import android.widget.TextView;
import com.drk.terminal.utils.StringUtil;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
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

    public String getFullPath() {
        return fullPath;
    }
}
