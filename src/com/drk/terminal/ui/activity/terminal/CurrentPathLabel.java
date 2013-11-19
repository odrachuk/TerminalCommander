package com.drk.terminal.ui.activity.terminal;

import android.content.res.Resources;
import android.widget.TextView;
import com.drk.terminal.R;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/19/13
 * Time: 4:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class CurrentPathLabel {
    private final TextView ownLabel;
    private final TextView alienLabel;
    private final Resources resources;

    public CurrentPathLabel(Resources resources,
                            TextView ownLabel, TextView alienLabel) {
        this.resources = resources;
        this.ownLabel = ownLabel;
        this.alienLabel = alienLabel;
    }

    public void setPath(String path) {
        ownLabel.setTextColor(resources.getColor(R.color.COLOR_FFFF00));
        alienLabel.setTextColor(resources.getColor(R.color.COLOR_b2b2b2));
        if (path != null) {
            ownLabel.setText(path);
        }
    }

    private String preparePath() {
       return null;
    }
}
