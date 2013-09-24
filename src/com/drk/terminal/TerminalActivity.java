package com.drk.terminal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.drk.terminal.process.TerminalProcess;
import com.drk.terminal.process.TerminalProcessImpl;

import java.io.IOException;

import static com.drk.terminal.util.StringConstant.LINE_SEPARATOR;

public class TerminalActivity extends Activity {
    public static final String LOG_TAG = TerminalActivity.class.getSimpleName();

    private StringBuilder mPromptText = new StringBuilder("user:/local/path $");
    private TextView mTerminalOut;
    private TextView mTerminalPrompt;
    private EditText mTerminalIn;
    private TerminalProcess mProcess;
    private boolean inFirst = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTerminalOut = (TextView) findViewById(R.id.terminal_out_text_view);
        mTerminalPrompt = (TextView) findViewById(R.id.terminal_prompt_text_view);
        mTerminalIn = (EditText) findViewById(R.id.terminal_input_edit_text);
        mTerminalIn.requestFocus();
        init();
        showSoftKeyboard();
        startTerminal();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProcess.stopProcess();
    }

    private void init() {
        mTerminalPrompt.setText(mPromptText.toString());
        mTerminalIn.setImeOptions(EditorInfo.IME_MASK_ACTION);
        mTerminalIn.setGravity(Gravity.TOP | Gravity.LEFT);
        mTerminalIn.setBackgroundColor(android.R.color.transparent);
        mTerminalIn.setSingleLine();
        mTerminalIn.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mTerminalIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        String text = mTerminalIn.getText().toString();
                        StringBuilder resultText = new StringBuilder(mTerminalOut.getText().toString());
                        if (inFirst) {
                            mTerminalOut.setVisibility(View.VISIBLE);
                            if (!text.isEmpty()) {
                                resultText.append(mPromptText.toString() + text);
                                mProcess.execCommand(text);
                            } else {
                                resultText.append(mPromptText.toString());
                            }
                            inFirst = false;
                        } else {
                            if (!text.isEmpty()) {
                                resultText.append(LINE_SEPARATOR + mPromptText.toString() + text);
                                mProcess.execCommand(text);
                            } else {
                                resultText.append(LINE_SEPARATOR + mPromptText.toString());
                            }
                        }
                        mTerminalOut.setText(resultText);
                        mTerminalIn.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void startTerminal() {
        Log.d(LOG_TAG, "startConsole");
        mProcess = new TerminalProcessImpl(mTerminalOut);
        try {
            mProcess.startProcess();
        } catch (IOException e) {
            Toast.makeText(this, "Can't start main process!", Toast.LENGTH_LONG).show();
        }
    }

    public void showSoftKeyboard() {
        if (mTerminalIn.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(mTerminalIn, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
