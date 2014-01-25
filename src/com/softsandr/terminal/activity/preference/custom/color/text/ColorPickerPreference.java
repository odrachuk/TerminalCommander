/*******************************************************************************
 * Created by o.drachuk on 20/01/2014. 
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
package com.softsandr.terminal.activity.preference.custom.color.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.softsandr.terminal.R;
import com.softsandr.terminal.data.preferences.PreferenceController;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class customize {@link android.preference.DialogPreference} and display
 * {@link android.widget.SeekBar} for setup preference int value
 */
public abstract class ColorPickerPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {
    protected final String logTag;
    private SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    private TextView redEditText, greenEditText, blueEditText;
    private TextView colorView;
    public static final String DEFAULT_VALUE = "{\"red\":255,\"green\":80,\"blue\":236}";
    private Integer redCurValue, greenCurValue, blueCurValue;
    private static final int RED_ID = 0, GREEN_ID = 1, BLUE_ID = 2;

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        logTag = getClass().getSimpleName();
        setDialogLayoutResource(R.layout.color_picker_dialog_layout);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        // read preferences
        int[] defArray = PreferenceController.parseColorComponentsFromJson(getPersistedString(DEFAULT_VALUE));
        redCurValue = defArray[RED_ID];
        greenCurValue = defArray[GREEN_ID];
        blueCurValue = defArray[BLUE_ID];
        // setup color test view
        colorView = (TextView) view.findViewById(R.id.color_picker_test_view);
        colorView.setBackgroundColor(PreferenceController.parseColorFromJson(getSharedPreferences().getString(getContext().getResources().getString(R.string.pref_term_bg_color_key), "")));
        // red
        redEditText = (TextView) view.findViewById(R.id.color_picker_red_edit_text);
        redSeekBar = (SeekBar) view.findViewById(R.id.color_picker_red_seek_bar);
        redSeekBar.setOnSeekBarChangeListener(this);
        redSeekBar.setProgress(redCurValue);
        // green
        greenEditText = (TextView) view.findViewById(R.id.color_picker_green_edit_text);
        greenSeekBar = (SeekBar) view.findViewById(R.id.color_picker_green_seek_bar);
        greenSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setProgress(greenCurValue);
        // blue
        blueEditText = (TextView) view.findViewById(R.id.color_picker_blue_edit_text);
        blueSeekBar = (SeekBar) view.findViewById(R.id.color_picker_blue_seek_bar);
        blueSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setProgress(blueCurValue);
    }

    private String prepareSaveJson() {
        JSONObject resultJson = new JSONObject();
        try {
            resultJson.put("red", redCurValue);
            resultJson.put("green", greenCurValue);
            resultJson.put("blue", blueCurValue);
        } catch (JSONException ex) {
            Log.d(logTag, "prepareSaveJson: " + ex.getMessage());
        }
        return resultJson.toString();
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            persistString(prepareSaveJson());
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            int[] defArray = PreferenceController.parseColorComponentsFromJson(this.getPersistedString(DEFAULT_VALUE));
            redCurValue = defArray[RED_ID];
            redCurValue = defArray[GREEN_ID];
            redCurValue = defArray[BLUE_ID];
        } else {
            // Set default state from the XML attribute
            int[] defArray = PreferenceController.parseColorComponentsFromJson((String) defaultValue);
            redCurValue = defArray[RED_ID];
            redCurValue = defArray[GREEN_ID];
            redCurValue = defArray[BLUE_ID];
            persistString(prepareSaveJson());
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) {
            // No need to save instance state since it's persistent, use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current setting value
        myState.value = prepareSaveJson();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // Set this Preference's widget to reflect the restored state
        int[] defArray = PreferenceController.parseColorComponentsFromJson(myState.value);
        redSeekBar.setProgress(defArray[RED_ID]);
        greenSeekBar.setProgress(defArray[GREEN_ID]);
        blueSeekBar.setProgress(defArray[BLUE_ID]);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.color_picker_red_seek_bar:
                redCurValue = progress;
                redEditText.setText(String.valueOf(redCurValue));
                break;
            case R.id.color_picker_green_seek_bar:
                greenCurValue = progress;
                greenEditText.setText(String.valueOf(greenCurValue));
                break;
            case R.id.color_picker_blue_seek_bar:
                blueCurValue = progress;
                blueEditText.setText(String.valueOf(blueCurValue));
                break;
        }
        refreshColor();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // ignored
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // ignored
    }

    private void refreshColor() {
        colorView.setTextColor(Color.rgb(redCurValue, greenCurValue, blueCurValue));
    }

    private static class SavedState extends BaseSavedState {
        // Member that holds the setting's value
        // Change this data type to match the type saved by your Preference
        String value;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            // Get the current preference's value
            value = source.readString();  // Change this to read the appropriate data type
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            // Write the preference's value
            dest.writeString(value);  // Change this to write the appropriate data type
        }

        // Standard creator object using an instance of this class
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
