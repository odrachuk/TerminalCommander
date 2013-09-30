package com.drk.terminal.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.drk.terminal.R;
import com.drk.terminal.controller.KeyboardController;
import com.drk.terminal.controller.ProcessController;
import com.drk.terminal.controller.UiController;

public class TerminalActivity extends Activity {
    private static final String LOG_TAG = TerminalActivity.class.getSimpleName();

    private ProcessController mProcessController;
    private KeyboardController mKeyboardController;
    private UiController mUiController;
    private TextView mTerminalOutView;
    private TextView mTerminalPromptView;
    private EditText mTerminalInView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTerminalOutView = (TextView) findViewById(R.id.terminal_out_text_view);
        mTerminalPromptView = (TextView) findViewById(R.id.terminal_prompt_text_view);
        mTerminalInView = (EditText) findViewById(R.id.terminal_input_edit_text);
        mTerminalInView.requestFocus();
        init();
        showSoftKeyboard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProcessController.onDestroyExecutionProcess();
    }

    private void init() {
        // create controllers
        mUiController = new UiController(this);
        mProcessController = new ProcessController(mUiController);
        mKeyboardController = new KeyboardController(mUiController, mProcessController);
        // config ui components
        mTerminalPromptView.setText(mUiController.getPrompt().getPromptText());
        mTerminalInView.setImeOptions(EditorInfo.IME_MASK_ACTION);
        mTerminalInView.setGravity(Gravity.TOP | Gravity.LEFT);
        mTerminalInView.setBackgroundColor(android.R.color.transparent);
        mTerminalInView.setSingleLine();
        mTerminalInView.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mTerminalInView.setOnEditorActionListener(mKeyboardController);
    }

    public void showSoftKeyboard() {
        if (mTerminalInView.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(mTerminalInView, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public EditText getTerminalInView() {
        return mTerminalInView;
    }

    public TextView getTerminalPromptView() {
        return mTerminalPromptView;
    }

    public TextView getTerminalOutView() {
        return mTerminalOutView;
    }
}
