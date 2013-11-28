package com.softsandr.terminal.ui.activity.commander;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.drk.terminal.util.utils.StringUtil;
import com.softsandr.terminal.R;
import com.softsandr.terminal.commander.controller.KeyboardController;
import com.softsandr.terminal.commander.controller.ProcessController;
import com.softsandr.terminal.commander.controller.UiController;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class CommanderActivity extends Activity {
    private static final String LOG_TAG = CommanderActivity.class.getSimpleName();
    public static final String WORK_PATH_EXTRA = LOG_TAG + ".WORK_PATH";

    private ProcessController mProcessController;
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
        Button tabBtn = (Button) tabItem.getActionView();
        tabBtn.setOnClickListener(mOnClickListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
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
    public void finish() {
        // Prepare data intent
        Intent data = new Intent();
        data.putExtra(WORK_PATH_EXTRA, mProcessUiController.getPrompt().getUserLocation());
        // TerminalActivity finished ok, return the data
        setResult(RESULT_OK, data);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProcessController.onDestroyExecutionProcess();
    }

    private void init() {
        Intent startIntent = getIntent();
        String path = StringUtil.PATH_SEPARATOR;
        if (startIntent != null) {
            path = startIntent.getStringExtra(WORK_PATH_EXTRA);
        }
        // create controllers
        mProcessUiController = new UiController(this, mTerminalInView, mTerminalOutView, mTerminalPromptView);
        mProcessController = new ProcessController(mProcessUiController, path);
        // config ui components
        mTerminalPromptView.setText(mProcessUiController.getPrompt().getPromptText());
        mTerminalInView.setImeOptions(EditorInfo.IME_MASK_ACTION);
        mTerminalInView.setGravity(Gravity.TOP | Gravity.LEFT);
        mTerminalInView.setBackgroundColor(android.R.color.transparent);
        mTerminalInView.setSingleLine();
        mTerminalInView.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mTerminalInView.setOnEditorActionListener(new KeyboardController(mProcessUiController, mProcessController));
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
