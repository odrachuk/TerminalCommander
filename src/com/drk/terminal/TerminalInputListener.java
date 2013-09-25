package com.drk.terminal;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import static com.drk.terminal.utils.StringUtils.LINE_SEPARATOR;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalInputListener implements TextView.OnEditorActionListener {
    private TerminalController mTerminalController;
    private boolean inFirst = true;

    public TerminalInputListener(TerminalController terminalController) {
        mTerminalController = terminalController;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (event != null) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                String text = mTerminalController.getActivity().getTerminalInView().getText().toString();
                StringBuilder resultText = new StringBuilder(mTerminalController.getActivity().getTerminalOutView().getText().toString());
                if (inFirst) {
                    mTerminalController.getActivity().getTerminalOutView().setVisibility(View.VISIBLE);
                    if (!text.isEmpty()) {
                        resultText.append(mTerminalController.getTerminalPrompt().getPrompt() + text);
                        mTerminalController.getProcess().execCommand(text);
                    } else {
                        resultText.append(mTerminalController.getTerminalPrompt().getPrompt());
                    }
                    inFirst = false;
                } else {
                    if (!text.isEmpty()) {
                        resultText.append(LINE_SEPARATOR + mTerminalController.getTerminalPrompt().getPrompt() + text);
                        mTerminalController.getProcess().execCommand(text);
                    } else {
                        resultText.append(LINE_SEPARATOR + mTerminalController.getTerminalPrompt().getPrompt());
                    }
                }
                mTerminalController.getActivity().getTerminalOutView().setText(resultText);
                mTerminalController.getActivity().getTerminalInView().setText("");
            }
            return true;
        }
        return false;
    }
}
