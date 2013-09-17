package com.drk.terminal.console;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import java.util.LinkedList;
import java.util.List;

public class ConsoleInput extends EditText {
    public static final String DELIMITER = "\n";
    private int mLastPosition;

    public ConsoleInput(Context context) {
        super(context);
        init();
    }

    public ConsoleInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setImeOptions(EditorInfo.IME_MASK_ACTION);
        setGravity(Gravity.TOP | Gravity.LEFT);
        setBackgroundColor(R.color.transparent);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection ic = new BaseInputConnection(this, false);
        outAttrs.inputType = EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE;
        outAttrs.initialCapsMode = ic.getCursorCapsMode(outAttrs.inputType);
        return ic;
    }

    /**
     * Return the last input line. Used list, because user can done long input separated in lines
     * @return list of string lines
     */
    public List<String> readLine() {
        List<String> outLines = new LinkedList<String>();
        String text = getText().toString();
        String[] inLines = text.split(DELIMITER);
        if (mLastPosition == 0) {
            for (int i = 0; i < inLines.length; i++) {
                outLines.add(inLines[i]);
            }
        } else {
            for (int i = inLines.length - mLastPosition; i < inLines.length; i++) {
                outLines.add(inLines[i]);
            }
        }
        mLastPosition = inLines.length;
        return outLines;
    }

    public void writeLine(String line) {
        setText(getText() + DELIMITER + line);
    }

    public void newLine() {
        setText(getText() + DELIMITER);
        scrollDown();
    }

    private void scrollDown() {
        setSelection(getText().length());
    }
}