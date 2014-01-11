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
package com.softsandr.terminal.activity.progress;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.softsandr.terminal.R;

/**
 * The Activity configured as {@link android.widget.ProgressBar}
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
