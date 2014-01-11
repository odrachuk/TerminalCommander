/*******************************************************************************
 * Created by o.drachuk on 10/01/2014.
 *
 * Copyright Oleksandr Drachuk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.softsandr.terminal.ui.activity.terminal.selection;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;
import com.softsandr.terminal.R;
import com.softsandr.terminal.ui.activity.terminal.TerminalActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * The container for all ui components that marking selection on ui.
 */
public class SelectionUiComponents {
    private final Resources resources;
    private final List<View> leftPanelBorders = new ArrayList<View>();
    private final List<View> rightPanelBorders = new ArrayList<View>();
    private final TextView rightPathLabel;
    private final TextView leftPathLabel;


    public SelectionUiComponents(TerminalActivity activity) {
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
            left.setBackgroundColor(resources.getColor(R.color.COLOR_FECE0A));
        }
        for (View right : rightPanelBorders) {
            right.setBackgroundColor(resources.getColor(R.color.COLOR_C2C2C2));
        }
        leftPathLabel.setTextColor(resources.getColor(R.color.COLOR_FECE0A));
        rightPathLabel.setTextColor(resources.getColor(R.color.COLOR_C2C2C2));
    }

    public void selectRight() {
        for (View right : rightPanelBorders) {
            right.setBackgroundColor(resources.getColor(R.color.COLOR_FECE0A));
        }
        for (View left : leftPanelBorders) {
            left.setBackgroundColor(resources.getColor(R.color.COLOR_C2C2C2));
        }
        rightPathLabel.setTextColor(resources.getColor(R.color.COLOR_FECE0A));
        leftPathLabel.setTextColor(resources.getColor(R.color.COLOR_C2C2C2));
    }
}
