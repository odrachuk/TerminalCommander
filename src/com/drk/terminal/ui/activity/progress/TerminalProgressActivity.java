package com.drk.terminal.ui.activity.progress;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.drk.terminal.R;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/20/13
 * Time: 8:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalProgressActivity extends Activity {
    public static final String PROGRESS_DISMISS_ACTION = TerminalProgressActivity.class.getSimpleName() +
            ".PROGRESS_DISMISS";

    BroadcastReceiver mDismissListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            removeStickyBroadcast(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_progress_layout);
        removeStickyBroadcast(new Intent(PROGRESS_DISMISS_ACTION));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mDismissListener, new IntentFilter(PROGRESS_DISMISS_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mDismissListener);
    }
}
