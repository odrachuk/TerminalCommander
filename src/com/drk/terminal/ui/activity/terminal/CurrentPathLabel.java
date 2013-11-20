package com.drk.terminal.ui.activity.terminal;

import android.content.res.Resources;
import android.widget.TextView;
import com.drk.terminal.R;
import com.drk.terminal.utils.OrientationUtil;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/19/13
 * Time: 4:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class CurrentPathLabel {
    private static final int MAX_PORTRAIT_SYMBOLS = 12;
    private static final int MAX_LANDSCAPE_SYMBOLS = 20;
    private final TextView ownLabel;
    private final TextView alienLabel;
    private final Resources resources;
    private String fullPath;

    public CurrentPathLabel(Resources resources,
                            TextView ownLabel, TextView alienLabel) {
        this.resources = resources;
        this.ownLabel = ownLabel;
        this.alienLabel = alienLabel;
    }

    public void setPath(String path) {
        fullPath = path;
        ownLabel.setTextColor(resources.getColor(R.color.COLOR_FFFF00));
        alienLabel.setTextColor(resources.getColor(R.color.COLOR_b2b2b2));
        if (path != null) {
            ownLabel.setText(cutIfNeeds(path));
        }
    }

    // todo dimension variant should be and prefix logic not postfix
    private String cutIfNeeds(String path) {
        if (OrientationUtil.isLandscapeOrientation(resources)) {
            if (path.length() > MAX_LANDSCAPE_SYMBOLS) {
                StringBuilder result = new StringBuilder();
                result.append(path.substring(0, MAX_LANDSCAPE_SYMBOLS - 2)).append("..");
                return result.toString();
            } else {
                return path;
            }
        } else {
            if (path.length() > MAX_PORTRAIT_SYMBOLS) {
                StringBuilder result = new StringBuilder();
                result.append(path.substring(0, MAX_PORTRAIT_SYMBOLS - 2)).append("..");
                return result.toString();
            } else {
                return path;
            }
        }
    }

    public String getCurrentLabel() {
        return ownLabel.getText().toString();
    }

    public TextView getOwnLabel() {
        return ownLabel;
    }

    public String getFullPath() {
        return fullPath;
    }
}
