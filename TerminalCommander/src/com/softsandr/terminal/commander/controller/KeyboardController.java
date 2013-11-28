package com.softsandr.terminal.commander.controller;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import static com.drk.terminal.util.utils.StringUtil.EMPTY;
import static com.drk.terminal.util.utils.StringUtil.LINE_SEPARATOR;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class KeyboardController implements TextView.OnEditorActionListener {
    private ProcessController mProcessController;
    private UiController mUiController;
    private boolean inFirst = true;

    public KeyboardController(UiController uiController, ProcessController processController) {
        mProcessController = processController;
        mUiController = uiController;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (event != null) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                mUiController.setHideOutView(false);
                String fullCommandText = mUiController.getTerminalInView().getText().toString();
                StringBuilder resultText = new StringBuilder(mUiController.getTerminalOutView().getText().toString());
                if (inFirst) {
                    if (!fullCommandText.isEmpty()) {
                        resultText.append(mUiController.getPrompt().getPromptText());
                        resultText.append(fullCommandText);
                        mProcessController.getProcess().execCommand(fullCommandText);
                    } else {
                        resultText.append(mUiController.getPrompt().getPromptText());
                    }
                    inFirst = false;
                } else {
                    if (!fullCommandText.isEmpty()) {
                        resultText.append(LINE_SEPARATOR);
                        resultText.append(mUiController.getPrompt().getPromptText());
                        resultText.append(fullCommandText);
                        mProcessController.getProcess().execCommand(fullCommandText);
                    } else {
                        if (!TextUtils.isEmpty(mUiController.getTerminalOutView().getText())) {
                            resultText.append(LINE_SEPARATOR);
                        }
                        resultText.append(mUiController.getPrompt().getPromptText());
                    }
                }
                if (!mUiController.isHideOutView()) {
                    mUiController.getTerminalOutView().setText(resultText);
                }
                mUiController.getTerminalInView().setText(EMPTY);
                checkVisibility();
            }
            handled = true;
        }
        return handled;
    }

    private void checkVisibility() {
        if (mUiController.isHideOutView()) {
            mUiController.getTerminalOutView().setVisibility(View.GONE);
        } else {
            mUiController.getTerminalOutView().setVisibility(View.VISIBLE);
        }
    }
}
