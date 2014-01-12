/*******************************************************************************
 * Created by o.drachuk on 10/01/2014.
 *
 * Copyright Oleksandr Drachuk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.softsandr.terminal.activity.commander;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.softsandr.commander.Commander;
import com.softsandr.commander.CommanderActivity;
import com.softsandr.commander.KeyboardController;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.terminal.TerminalActivityImpl;
import com.softsandr.utils.string.StringUtil;

/**
 * This activity represent CommanderActivityImpl - console
 */
public class CommanderActivityImpl extends Activity implements CommanderActivity {
    private static final String LOG_TAG = CommanderActivityImpl.class.getSimpleName();
    public static final String WORK_PATH_EXTRA = LOG_TAG + ".WORK_PATH";
    public static final String OTHER_PATH_EXTRA = LOG_TAG + ".OTHER_PATH";
    public static final String ACTIVE_PAGE_EXTRA = LOG_TAG + ".ACTIVE_PAGE";
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.equals(mTabMenuBtn)) {
                // todo
                Toast.makeText(CommanderActivityImpl.this, "Tabulate", Toast.LENGTH_SHORT).show();
            } else if (v.equals(mCancelMenuBtn)) {
                mTerminalPromptView.setVisibility(View.VISIBLE);
                mTerminalInView.setVisibility(View.VISIBLE);
                mCancelMenuBtn.setVisibility(View.GONE);
                commander.cancelInteractiveCommand();
                showSoftKeyboard();
            }
        }
    };
    private final View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                showSoftKeyboard();
            }
            return false;
        }
    };
    private final BroadcastReceiver mFinishBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(TerminalActivityImpl.COMMON_EXIT_INTENT)) {
                    finish();
                }
            }
        }
    };
    private Commander commander;
    private TextView mTerminalOutView;
    private TextView mTerminalPromptView;
    private EditText mTerminalInView;
    private Button mTabMenuBtn;
    private Button mCancelMenuBtn;
    private String mInitialPath;
    private String mOtherPath;
    private boolean mInitialPageLeft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commander_activity_layout);
        findViewById(R.id.commander_main_container).setOnTouchListener(mOnTouchListener);
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
        MenuItem sortingItem = menu.findItem(R.id.action_sorting);
        if (sortingItem != null) {
            sortingItem.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.action_settings:
                //todo
                return true;*/
            case R.id.action_refresh:
                commander.getProcess().onClear();
                return true;
            case R.id.action_quit:
                sendBroadcast(new Intent(TerminalActivityImpl.COMMON_EXIT_INTENT));
                return true;
            case android.R.id.home:
                exitActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mFinishBroadcastReceiver, new IntentFilter(TerminalActivityImpl.COMMON_EXIT_INTENT));
        init();
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
        commander = new Commander(this, mTerminalInView,
                mTerminalOutView, mTerminalPromptView, mInitialPath);
        // config ui components
        mTerminalPromptView.setText(commander.getPrompt().getCompoundString());
        mTerminalInView.setImeOptions(EditorInfo.IME_MASK_ACTION);
        mTerminalInView.setGravity(Gravity.TOP | Gravity.LEFT);
        mTerminalInView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mTerminalInView.setSingleLine();
        mTerminalInView.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mTerminalInView.setOnEditorActionListener(new KeyboardController(commander));
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.commander));
            actionBar.setDisplayHomeAsUpEnabled(true);
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.RIGHT;
            LayoutInflater li = getLayoutInflater();
            View customView = li.inflate(R.layout.commander_action_bar_custom_view, null);
            if (customView != null) {
                mTabMenuBtn = (Button) customView.findViewById(R.id.action_bar_tab_btn);
                mTabMenuBtn.setOnClickListener(mOnClickListener);
                mCancelMenuBtn = (Button) customView.findViewById(R.id.action_bar_cancel_btn);
                mCancelMenuBtn.setOnClickListener(mOnClickListener);
                actionBar.setCustomView(customView, lp);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mFinishBroadcastReceiver);
        super.onDestroy();
        commander.getProcess().stopExecutionProcess();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(WORK_PATH_EXTRA, commander.getPrompt().getUserLocation());
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

    @Override
    public void showSoftKeyboard() {
        if (mTerminalInView.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(mTerminalInView, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTerminalInView.getWindowToken(), 0);
    }

    @Override
    public void showCancelBtn() {
        mCancelMenuBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void exitActivity() {
        // Stop processes
        commander.getProcess().stopExecutionProcess();
        // Prepare data intent
        Intent data = new Intent();
        data.putExtra(WORK_PATH_EXTRA, commander.getPrompt().getUserLocation());
        data.putExtra(OTHER_PATH_EXTRA, mOtherPath);
        data.putExtra(ACTIVE_PAGE_EXTRA, mInitialPageLeft);
        setResult(RESULT_OK, data);
        finish();
    }
}