package com.drk.terminal.controller;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import static com.drk.terminal.utils.StringUtils.EMPTY;
import static com.drk.terminal.utils.StringUtils.LINE_SEPARATOR;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
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
                String text = mUiController.getActivity().getTerminalInView().getText().toString();
                StringBuilder resultText = new StringBuilder(mUiController.getActivity().getTerminalOutView().getText().toString());
                if (inFirst) {
                    if (!text.isEmpty()) {
                        resultText.append(mUiController.getPrompt().getFullText());
                        resultText.append(text);
                        mProcessController.getProcess().execCommand(text);
                    } else {
                        resultText.append(mUiController.getPrompt().getFullText());
                    }
                    inFirst = false;
                } else {
                    if (!text.isEmpty()) {
                        resultText.append(LINE_SEPARATOR);
                        resultText.append(mUiController.getPrompt().getFullText());
                        resultText.append(text);
                        mProcessController.getProcess().execCommand(text);
                    } else {
                        resultText.append(LINE_SEPARATOR);
                        resultText.append(mUiController.getPrompt().getFullText());
                    }
                }
                mUiController.getActivity().getTerminalOutView().setText(resultText);
                mUiController.getActivity().getTerminalInView().setText(EMPTY);
                checkVisibility();
            }
            handled = true;
        }
        return handled;
    }

    private void checkVisibility() {
        if (mUiController.isHideOutView()) {
            mUiController.getActivity().getTerminalOutView().setVisibility(View.GONE);
        } else {
            mUiController.getActivity().getTerminalOutView().setVisibility(View.VISIBLE);
        }
    }
}
