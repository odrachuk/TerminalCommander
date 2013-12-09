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
    public static final String OTHER_PATH_EXTRA = LOG_TAG + ".OTHER_PATH";
    public static final String ACTIVE_PAGE_EXTRA = LOG_TAG + ".ACTIVE_PAGE";
    private ProcessController mProcessController;
    private UiController mProcessUiController;
    private TextView mTerminalOutView;
    private TextView mTerminalPromptView;
    private EditText mTerminalInView;
    private String mInitialPath;
    private String mOtherPath;
    private boolean mInitialPageLeft;
    private Button mTabMenuBtn;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.equals(mTabMenuBtn)) {
                // todo
                Toast.makeText(CommanderActivity.this, "Tabulate", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commander_activity_layout);
        mTerminalOutView = (TextView) findViewById(R.id.terminal_out_text_view);
        mTerminalPromptView = (TextView) findViewById(R.id.terminal_prompt_text_view);
        mTerminalInView = (EditText) findViewById(R.id.terminal_input_edit_text);
        mTerminalInView.requestFocus();
        showSoftKeyboard();
        initActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
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
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void finish() {
        // Prepare data intent
        Intent data = new Intent();
        data.putExtra(WORK_PATH_EXTRA, mProcessUiController.getPrompt().getUserLocation());
        data.putExtra(OTHER_PATH_EXTRA, mOtherPath);
        data.putExtra(ACTIVE_PAGE_EXTRA, mInitialPageLeft);
        // TerminalActivity finished ok, return the data
        setResult(RESULT_OK, data);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProcessController.onDestroyExecutionProcess();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(WORK_PATH_EXTRA, mProcessUiController.getPrompt().getUserLocation());
        outState.putString(OTHER_PATH_EXTRA, mOtherPath);
        outState.putBoolean(ACTIVE_PAGE_EXTRA, mInitialPageLeft);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mInitialPath = savedInstanceState.getString(WORK_PATH_EXTRA);
        mOtherPath = savedInstanceState.getString(OTHER_PATH_EXTRA);
        mInitialPageLeft = savedInstanceState.getBoolean(ACTIVE_PAGE_EXTRA);
    }

    private void init() {
        Intent startIntent = getIntent();
        if (mInitialPath == null) {
            mInitialPath = StringUtil.PATH_SEPARATOR;
            if (startIntent != null) {
                mInitialPath = startIntent.getStringExtra(WORK_PATH_EXTRA);
                mOtherPath = startIntent.getStringExtra(OTHER_PATH_EXTRA);
                mInitialPageLeft = startIntent.getBooleanExtra(ACTIVE_PAGE_EXTRA, true);
            }
        }
        // create controllers
        mProcessUiController = new UiController(this, mTerminalInView, mTerminalOutView, mTerminalPromptView);
        mProcessController = new ProcessController(mProcessUiController, mInitialPath);
        // config ui components
        mTerminalPromptView.setText(mProcessUiController.getPrompt().getPromptText());
        mTerminalInView.setImeOptions(EditorInfo.IME_MASK_ACTION);
        mTerminalInView.setGravity(Gravity.TOP | Gravity.LEFT);
        mTerminalInView.setBackgroundColor(android.R.color.transparent);
        mTerminalInView.setSingleLine();
        mTerminalInView.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mTerminalInView.setOnEditorActionListener(new KeyboardController(mProcessUiController, mProcessController));
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(getResources().getString(R.string.commander));
        actionBar.setDisplayHomeAsUpEnabled(true);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT;
        LayoutInflater li = getLayoutInflater();
        View customView = li.inflate(R.layout.commander_action_bar_custom_view, null);
        mTabMenuBtn = (Button) customView.findViewById(R.id.action_bar_tab_btn);
        mTabMenuBtn.setOnClickListener(mOnClickListener);
        actionBar.setCustomView(customView, lp);
    }

    public void showSoftKeyboard() {
        if (mTerminalInView.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(mTerminalInView, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
