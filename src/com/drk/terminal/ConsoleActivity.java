package com.drk.terminal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.drk.terminal.console.ConsoleInput;
import com.drk.terminal.console.ConsoleInputActionListener;
import com.drk.terminal.process.ConsoleInputProcess;
import com.drk.terminal.process.ConsoleProcess;

import java.io.IOException;

public class ConsoleActivity extends Activity {
    public static final String LOG_TAG = ConsoleActivity.class.getSimpleName();

    private ConsoleInput mConsole;
    private ConsoleProcess mProcess;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mConsole = (ConsoleInput) findViewById(R.id.console_input);
        mConsole.setOnEditorActionListener(new ConsoleInputActionListener(ConsoleActivity.this));
        mConsole.requestFocus();
        showSoftKeyboard();
        startConsole();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProcess.stopProcess();
    }

    private void startConsole() {
        Log.d(LOG_TAG, "startConsole");
        try {
            mProcess = new ConsoleInputProcess(mConsole);
        } catch (IOException e) {
            Toast.makeText(this, "Can't create console", Toast.LENGTH_LONG).show();
        }
        if (mProcess != null) {
            mProcess.startProcess();
        }
    }

    public void showSoftKeyboard() {
        if (mConsole.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(mConsole, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * Get active execution process from context
     * @return current execution process
     */
    public ConsoleProcess getProcess() {
        return mProcess;
    }

    /**
     * Get active EditText
     * @return instance of console
     */
    public ConsoleInput getConsole() {
        return mConsole;
    }
}
