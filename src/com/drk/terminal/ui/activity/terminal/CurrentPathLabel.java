package com.drk.terminal.ui.activity.terminal;

import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/19/13
 * Time: 4:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class CurrentPathLabel {
    private TextView label;

    public CurrentPathLabel(TextView label) {
        this.label = label;
    }

    public void setPath(String path) {
        label.setText(path);
    }
}
