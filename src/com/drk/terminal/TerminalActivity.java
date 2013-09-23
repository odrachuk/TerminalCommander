package com.drk.terminal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.drk.terminal.console.TerminalInputActionListener;
import com.drk.terminal.console.TerminalInput;
import com.drk.terminal.process.TerminalInputProcess;
import com.drk.terminal.process.TerminalProcess;

import java.io.IOException;

public class TerminalActivity extends Activity {
    public static final String LOG_TAG = TerminalActivity.class.getSimpleName();

    private TerminalInput mTerminalInput;
    private TerminalProcess mProcess;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTerminalInput = (TerminalInput) findViewById(R.id.console_input);
        mTerminalInput.setOnEditorActionListener(new TerminalInputActionListener(TerminalActivity.this));
        mTerminalInput.requestFocus();
        showSoftKeyboard();
        startTerminal();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProcess.stopProcess();
    }

    private void startTerminal() {
        Log.d(LOG_TAG, "startConsole");
        mProcess = new TerminalInputProcess(mTerminalInput);
        try {
            mProcess.startProcess();
        } catch (IOException e) {
            Toast.makeText(this, "Can't start main process!", Toast.LENGTH_LONG).show();
        }
    }

    public void showSoftKeyboard() {
        if (mTerminalInput.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(mTerminalInput, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * Get active execution process from context
     * @return current execution process
     */
    public TerminalProcess getProcess() {
        return mProcess;
    }

    /**
     * Get active EditText
     * @return instance of console
     */
    public TerminalInput getConsole() {
        return mTerminalInput;
    }
}
