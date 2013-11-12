package com.drk.terminal.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.drk.terminal.R;
import com.drk.terminal.comm.controller.KeyboardController;
import com.drk.terminal.comm.controller.ProcessController;
import com.drk.terminal.comm.controller.UiController;

public class CommanderActivity extends Activity {
    private static final String LOG_TAG = CommanderActivity.class.getSimpleName();

    private ProcessController mProcessController;
    private KeyboardController mProcessKeyboardController;
    private UiController mProcessUiController;
    private TextView mTerminalOutView;
    private TextView mTerminalPromptView;
    private EditText mTerminalInView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commander_activity_layout);
        mTerminalOutView = (TextView) findViewById(R.id.terminal_out_text_view);
        mTerminalPromptView = (TextView) findViewById(R.id.terminal_prompt_text_view);
        mTerminalInView = (EditText) findViewById(R.id.terminal_input_edit_text);
        mTerminalInView.requestFocus();
        init();
        showSoftKeyboard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        // setup shift
        MenuItem shiftItem = menu.findItem(R.id.action_shift);
        shiftItem.setVisible(false);
        // setup ctrl
        MenuItem ctrlItem = menu.findItem(R.id.action_ctrl);
        ctrlItem.setVisible(false);
        // setup comm
        MenuItem commItem = menu.findItem(R.id.action_commander);
        commItem.setVisible(false);
        // setup tab
        MenuItem tabItem = menu.findItem(R.id.action_tab);
        if (ctrlItem != null) {
            Button tabBtn = (Button) tabItem.getActionView();
            tabBtn.setOnClickListener(mOnClickListener);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_serrings:
                //todo
                return true;
            case R.id.action_quit:
                //todo
                finish();
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProcessController.onDestroyExecutionProcess();
    }

    private void init() {
        // create controllers
        mProcessUiController = new UiController(this, mTerminalInView, mTerminalOutView, mTerminalPromptView);
        mProcessController = new ProcessController(mProcessUiController);
        mProcessKeyboardController = new KeyboardController(mProcessUiController, mProcessController);
        // config ui components
        mTerminalPromptView.setText(mProcessUiController.getPrompt().getPromptText());
        mTerminalInView.setImeOptions(EditorInfo.IME_MASK_ACTION);
        mTerminalInView.setGravity(Gravity.TOP | Gravity.LEFT);
        mTerminalInView.setBackgroundColor(android.R.color.transparent);
        mTerminalInView.setSingleLine();
        mTerminalInView.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mTerminalInView.setOnEditorActionListener(mProcessKeyboardController);
        initActionBar();
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(getResources().getString(R.string.commander));
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void showSoftKeyboard() {
        if (mTerminalInView.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(mTerminalInView, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.action_tab) {
                // todo
                Toast.makeText(CommanderActivity.this, "Tabulate", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
