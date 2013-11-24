package com.drk.terminal.ui.activity.terminal;

import android.content.res.Resources;
import android.view.View;
import com.drk.terminal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/24/13
 * Time: 12:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class SelectionVisualItems {
    private final Resources resources;
    private final List<View> leftPanelBorders = new ArrayList<View>();
    private final List<View> rightPanelBorders = new ArrayList<View>();


    public SelectionVisualItems(TerminalActivity terminalActivity) {
        this.resources = terminalActivity.getResources();
        // left panel
        leftPanelBorders.add(terminalActivity.findViewById(R.id.top_border_image_1_in_left));
        leftPanelBorders.add(terminalActivity.findViewById(R.id.top_border_image_2_in_left));
        leftPanelBorders.add(terminalActivity.findViewById(R.id.top_border_image_3_in_left));
        leftPanelBorders.add(terminalActivity.findViewById(R.id.top_border_image_4_in_left));
        leftPanelBorders.add(terminalActivity.findViewById(R.id.top_border_image_5_in_left));
        leftPanelBorders.add(terminalActivity.findViewById(R.id.right_triangle_img_in_left));
        leftPanelBorders.add(terminalActivity.findViewById(R.id.left_triangle_img_in_left));
        leftPanelBorders.add(terminalActivity.findViewById(R.id.bottom_border_image_in_left));
        leftPanelBorders.add(terminalActivity.findViewById(R.id.left_border_image_in_left));
        leftPanelBorders.add(terminalActivity.findViewById(R.id.right_border_image_in_left));
        // right panel
        rightPanelBorders.add(terminalActivity.findViewById(R.id.top_border_image_1_in_right));
        rightPanelBorders.add(terminalActivity.findViewById(R.id.top_border_image_2_in_right));
        rightPanelBorders.add(terminalActivity.findViewById(R.id.top_border_image_3_in_right));
        rightPanelBorders.add(terminalActivity.findViewById(R.id.top_border_image_4_in_right));
        rightPanelBorders.add(terminalActivity.findViewById(R.id.top_border_image_5_in_right));
        rightPanelBorders.add(terminalActivity.findViewById(R.id.right_triangle_img_in_right));
        rightPanelBorders.add(terminalActivity.findViewById(R.id.left_triangle_img_in_right));
        rightPanelBorders.add(terminalActivity.findViewById(R.id.bottom_border_image_in_right));
        rightPanelBorders.add(terminalActivity.findViewById(R.id.left_border_image_in_right));
        rightPanelBorders.add(terminalActivity.findViewById(R.id.right_border_image_in_right));
    }

    public void selectLeft() {
        for (View left : leftPanelBorders) {
            left.setBackgroundColor(resources.getColor(R.color.COLOR_FFFF00));
        }
        for (View right : rightPanelBorders) {
            right.setBackgroundColor(resources.getColor(R.color.COLOR_C2C2C2));
        }
    }

    public void selectRight() {
        for (View right : rightPanelBorders) {
            right.setBackgroundColor(resources.getColor(R.color.COLOR_FFFF00));
        }
        for (View left : leftPanelBorders) {
            left.setBackgroundColor(resources.getColor(R.color.COLOR_C2C2C2));
        }
    }
}
