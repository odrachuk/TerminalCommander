package com.drk.terminal.ui.activity.terminal.selection;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;
import com.drk.terminal.R;
import com.drk.terminal.ui.activity.terminal.TerminalActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class SelectionVisualItems {
    private final Resources resources;
    private final List<View> leftPanelBorders = new ArrayList<View>();
    private final List<View> rightPanelBorders = new ArrayList<View>();
    private final TextView rightPathLabel;
    private final TextView leftPathLabel;


    public SelectionVisualItems(TerminalActivity activity) {
        this.resources = activity.getResources();
        // left panel
        leftPanelBorders.add(activity.findViewById(R.id.top_border_image_1_in_left));
        leftPanelBorders.add(activity.findViewById(R.id.top_border_image_2_in_left));
        leftPanelBorders.add(activity.findViewById(R.id.top_border_image_3_in_left));
        leftPanelBorders.add(activity.findViewById(R.id.top_border_image_4_in_left));
        leftPanelBorders.add(activity.findViewById(R.id.top_border_image_5_in_left));
        leftPanelBorders.add(activity.findViewById(R.id.right_triangle_img_in_left));
        leftPanelBorders.add(activity.findViewById(R.id.left_triangle_img_in_left));
        leftPanelBorders.add(activity.findViewById(R.id.bottom_border_image_in_left));
        leftPanelBorders.add(activity.findViewById(R.id.left_border_image_in_left));
        leftPanelBorders.add(activity.findViewById(R.id.right_border_image_in_left));
        leftPathLabel = (TextView) activity.findViewById(R.id.path_location_in_left);
        // right panel
        rightPanelBorders.add(activity.findViewById(R.id.top_border_image_1_in_right));
        rightPanelBorders.add(activity.findViewById(R.id.top_border_image_2_in_right));
        rightPanelBorders.add(activity.findViewById(R.id.top_border_image_3_in_right));
        rightPanelBorders.add(activity.findViewById(R.id.top_border_image_4_in_right));
        rightPanelBorders.add(activity.findViewById(R.id.top_border_image_5_in_right));
        rightPanelBorders.add(activity.findViewById(R.id.right_triangle_img_in_right));
        rightPanelBorders.add(activity.findViewById(R.id.left_triangle_img_in_right));
        rightPanelBorders.add(activity.findViewById(R.id.bottom_border_image_in_right));
        rightPanelBorders.add(activity.findViewById(R.id.left_border_image_in_right));
        rightPanelBorders.add(activity.findViewById(R.id.right_border_image_in_right));
        rightPathLabel = (TextView) activity.findViewById(R.id.path_location_in_right);
    }

    public void selectLeft() {
        for (View left : leftPanelBorders) {
            left.setBackgroundColor(resources.getColor(R.color.COLOR_FFFF00));
        }
        for (View right : rightPanelBorders) {
            right.setBackgroundColor(resources.getColor(R.color.COLOR_C2C2C2));
        }
        leftPathLabel.setTextColor(resources.getColor(R.color.COLOR_FFFF00));
        rightPathLabel.setTextColor(resources.getColor(R.color.COLOR_C2C2C2));
    }

    public void selectRight() {
        for (View right : rightPanelBorders) {
            right.setBackgroundColor(resources.getColor(R.color.COLOR_FFFF00));
        }
        for (View left : leftPanelBorders) {
            left.setBackgroundColor(resources.getColor(R.color.COLOR_C2C2C2));
        }
        rightPathLabel.setTextColor(resources.getColor(R.color.COLOR_FFFF00));
        leftPathLabel.setTextColor(resources.getColor(R.color.COLOR_C2C2C2));
    }
}
