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
package com.softsandr.terminal.ui.activity.terminal;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import com.drk.terminal.util.utils.StringUtil;
import com.softsandr.terminal.R;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.terminal.model.listview.ListViewSortingStrategy;
import com.softsandr.terminal.model.preferences.HistoryLocationsManager;
import com.softsandr.terminal.model.preferences.TerminalPreferences;
import com.softsandr.terminal.ui.activity.commander.CommanderActivity;
import com.softsandr.terminal.ui.activity.terminal.adapter.ListViewAdapter;
import com.softsandr.terminal.ui.activity.terminal.async.LoadLeftListTask;
import com.softsandr.terminal.ui.activity.terminal.async.LoadRightListTask;
import com.softsandr.terminal.ui.activity.terminal.listener.ListViewItemClickListener;
import com.softsandr.terminal.ui.activity.terminal.listener.ListViewItemLongClickListener;
import com.softsandr.terminal.ui.activity.terminal.listener.ListViewTouchListener;
import com.softsandr.terminal.ui.activity.terminal.listener.TerminalClickListener;
import com.softsandr.terminal.ui.activity.terminal.monitor.ActionBarToggleMonitor;
import com.softsandr.terminal.ui.activity.terminal.monitor.SortingMenuItemsMonitor;
import com.softsandr.terminal.ui.activity.terminal.selection.SelectionViewComponents;

import java.util.ArrayList;

/**
 * The main activity that contain list of files
 */
public class TerminalActivity extends android.app.Activity implements Terminal {
    public static final int REQUEST_CODE = 0;
    public static final String COMMON_EXIT_INTENT = TerminalActivity.class.getSimpleName() + ".COMMON_EXIT_INTENT";

    private static final String LOG_TAG = TerminalActivity.class.getSimpleName();
    private static final String LEFT_FILE_LIST_PATH_BUNDLE = LOG_TAG + ".LEFT_FILE_LIST";
    private static final String RIGHT_FILE_LIST_PATH_BUNDLE = LOG_TAG + ".RIGHT_FILE_LIST";

    private AbsListView.OnTouchListener mListTouchListener;
    private View.OnClickListener mOnClickListener;
    private ListViewSortingStrategy mSortingStrategy = ListViewSortingStrategy.SORT_BY_NAME;
    private ListViewAdapter mLeftAdapter, mRightAdapter;
    private SelectionViewComponents mSelectionVisualItems;
    private TerminalActivePage activePage = TerminalActivePage.LEFT;
    private ActionBarToggleMonitor mActionBarToggleMonitor;
    private ListView mLeftList, mRightList;
    private String mRightListSavedLocation, mLeftListSavedLocation;
    private TerminalPreferences mPreferences;
    private HistoryLocationsManager mLeftHistoryLocationManager;
    private HistoryLocationsManager mRightHistoryLocationManager;
    private SortingMenuItemsMonitor mSortingMenuItemsMonitor;
    private boolean isPaused;

    private final BroadcastReceiver mFinishBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(COMMON_EXIT_INTENT)) {
                    finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_activity_layout);
        mSelectionVisualItems = new SelectionViewComponents(this);
        mListTouchListener = new ListViewTouchListener(this);
        mOnClickListener = new TerminalClickListener(this);
        readPreferences();
        initView();
        initActionBar();
    }

    private void initActionBar() {
        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setTitle(getString(R.string.terminal));
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.RIGHT;
            LayoutInflater li = getLayoutInflater();
            View customView = li.inflate(R.layout.terminal_action_bar_custom_view, null);
            if (customView != null) {
                Button mShiftMenuBtn = (Button) customView.findViewById(R.id.action_bar_shift_btn);
                Button mCtrlMenuBtn = (Button) customView.findViewById(R.id.action_bar_ctrl_btn);
                Button mCommMenuBtn = (Button) customView.findViewById(R.id.action_bar_comm_btn);
                mShiftMenuBtn.setOnClickListener(mOnClickListener);
                mCommMenuBtn.setOnClickListener(mOnClickListener);
                mCtrlMenuBtn.setOnClickListener(mOnClickListener);
                View mShiftBtnContainer = customView.findViewById(R.id.action_bar_shift_btn_container);
                View mCtrlBtnContainer = customView.findViewById(R.id.action_bar_ctrl_btn_container);
                mActionBarToggleMonitor = new ActionBarToggleMonitor(this, mShiftBtnContainer, mCtrlBtnContainer);
                bar.setCustomView(customView, lp);
            }
        }
    }

    private void readPreferences() {
        mPreferences = new TerminalPreferences(this);
        // read last locations
        mLeftListSavedLocation = mPreferences.loadLastLeftLocations();
        mRightListSavedLocation = mPreferences.loadLastRightLocations();
        mSortingStrategy = mPreferences.loadSortingStrategy();
        // read locations history
        mLeftHistoryLocationManager = new HistoryLocationsManager(mPreferences, TerminalActivePage.LEFT);
        mRightHistoryLocationManager = new HistoryLocationsManager(mPreferences, TerminalActivePage.RIGHT);
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        registerReceiver(mFinishBroadcastReceiver, new IntentFilter(COMMON_EXIT_INTENT));
        if (!isPaused) {
            new LoadLeftListTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new LoadRightListTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        isPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mLeftAdapter != null) {
            outState.putString(LEFT_FILE_LIST_PATH_BUNDLE, mLeftAdapter.getPathLabel().getFullPath());
        }
        if (mRightAdapter != null) {
            outState.putString(RIGHT_FILE_LIST_PATH_BUNDLE, mRightAdapter.getPathLabel().getFullPath());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mLeftListSavedLocation = savedInstanceState.getString(LEFT_FILE_LIST_PATH_BUNDLE);
            mRightListSavedLocation = savedInstanceState.getString(RIGHT_FILE_LIST_PATH_BUNDLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data.hasExtra(CommanderActivity.WORK_PATH_EXTRA)) {
                String pathFromCommander = data.getStringExtra(CommanderActivity.WORK_PATH_EXTRA);
                boolean isLeftActive = data.getBooleanExtra(CommanderActivity.ACTIVE_PAGE_EXTRA, true);
                String destinationPath = data.getStringExtra(CommanderActivity.OTHER_PATH_EXTRA);
                String[] splitPath = new String[0];
                if (pathFromCommander != null) {
                    splitPath = pathFromCommander.substring(1).split(StringUtil.PATH_SEPARATOR);
                }
                if (mLeftAdapter != null && mRightAdapter != null) {
                    switch (activePage) {
                        case LEFT:
                            mLeftAdapter.clearBackPath(splitPath);
                            mLeftAdapter.changeDirectory(splitPath[splitPath.length - 1]);
                            break;
                        case RIGHT:
                            mRightAdapter.clearBackPath(splitPath);
                            mRightAdapter.changeDirectory(splitPath[splitPath.length - 1]);
                            break;
                    }
                } else {
                    activePage = isLeftActive ? TerminalActivePage.LEFT : TerminalActivePage.RIGHT;
                    switch (activePage) {
                        case LEFT:
                            mLeftListSavedLocation = pathFromCommander;
                            mRightListSavedLocation = destinationPath;
                            break;
                        case RIGHT:
                            mRightListSavedLocation = pathFromCommander;
                            mLeftListSavedLocation = destinationPath;
                            break;
                        default:
                            new LoadLeftListTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            new LoadRightListTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem sortingItem = menu.findItem(R.id.action_sorting);
        if (sortingItem != null) {
            sortingItem.setVisible(true);
        }
        MenuItem sortByName = menu.findItem(R.id.sorting_by_name);
        MenuItem sortBySize = menu.findItem(R.id.sorting_by_size);
        MenuItem sortByModify = menu.findItem(R.id.sorting_by_modify);
        switch (mSortingStrategy) {
            case SORT_BY_NAME:
                if (sortByName != null) {
                    sortByName.setChecked(true);
                }
                break;
            case SORT_BY_SIZE:
                if (sortBySize != null) {
                    sortBySize.setChecked(true);
                }
                break;
            case SORT_BY_DATE:
                if (sortByModify != null) {
                    sortByModify.setChecked(true);
                }
                break;
        }
        mSortingMenuItemsMonitor = new SortingMenuItemsMonitor(this, sortByName, sortBySize, sortByModify);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
      /*      case R.id.action_settings:
                //todo
                return true;*/
            case R.id.action_refresh:
                switch (activePage) {
                    case LEFT:
                        if (mLeftAdapter != null) {
                            mLeftAdapter.changeDirectory(mLeftAdapter.getPathLabel().getFullPath());
                            mLeftAdapter.getSelectionStrategy().clear();
                        }
                        break;
                    case RIGHT:
                        if (mRightAdapter != null) {
                            mRightAdapter.changeDirectory(mRightAdapter.getPathLabel().getFullPath());
                            mRightAdapter.getSelectionStrategy().clear();
                        }
                        break;
                }
                return true;
            case R.id.action_quit:
                sendBroadcast(new Intent(COMMON_EXIT_INTENT));
                return true;
            case R.id.sorting_by_name:
                mSortingMenuItemsMonitor.onMenuSelected(item);
                return true;
            case R.id.sorting_by_size:
                mSortingMenuItemsMonitor.onMenuSelected(item);
                return true;
            case R.id.sorting_by_modify:
                mSortingMenuItemsMonitor.onMenuSelected(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mFinishBroadcastReceiver);
        saveDataBeforeDestroy();
        super.onDestroy();
    }

    private void saveDataBeforeDestroy() {
        mPreferences.saveLastLocations(mLeftAdapter.getPathLabel().getFullPath(),
                mRightAdapter.getPathLabel().getFullPath());
        mPreferences.saveLeftHistoryLocations(mLeftHistoryLocationManager.getActualHistoryLocations());
        mPreferences.saveRightHistoryLocations(mRightHistoryLocationManager.getActualHistoryLocations());
        mPreferences.saveSortingStrategy(mSortingStrategy);
    }

    private void initView() {
        findViewById(R.id.copy_btn).setOnClickListener(mOnClickListener);
        findViewById(R.id.rename_btn).setOnClickListener(mOnClickListener);
        findViewById(R.id.mkdir_btn).setOnClickListener(mOnClickListener);
        findViewById(R.id.delete_btn).setOnClickListener(mOnClickListener);
        findViewById(R.id.history_btn_in_left).setOnClickListener(mOnClickListener);
        findViewById(R.id.history_btn_in_right).setOnClickListener(mOnClickListener);
        mLeftList = (ListView) findViewById(R.id.left_directory_list);
        mRightList = (ListView) findViewById(R.id.right_directory_list);
    }

    @Override
    public void setLeftListAdapter(ListViewAdapter adapter) {
        mLeftAdapter = adapter;
        mLeftList.setAdapter(mLeftAdapter);
        mLeftList.setOnItemClickListener(new ListViewItemClickListener(this, mLeftAdapter, mLeftList));
        mLeftList.setOnItemLongClickListener(new ListViewItemLongClickListener(this, mLeftAdapter));
        mLeftList.setOnTouchListener(mListTouchListener);
        mLeftAdapter.restoreBackPath(mLeftListSavedLocation);
    }

    @Override
    public ListViewAdapter getLeftListAdapter() {
        return mLeftAdapter;
    }

    @Override
    public void setRightListAdapter(ListViewAdapter adapter) {
        mRightAdapter = adapter;
        mRightList.setAdapter(mRightAdapter);
        mRightList.setOnItemClickListener(new ListViewItemClickListener(this, mRightAdapter, mRightList));
        mRightList.setOnItemLongClickListener(new ListViewItemLongClickListener(this, mRightAdapter));
        mRightList.setOnTouchListener(mListTouchListener);
        mRightAdapter.restoreBackPath(mRightListSavedLocation);
    }

    @Override
    public ListViewAdapter getRightListAdapter() {
        return mRightAdapter;
    }

    @Override
    public ListViewSortingStrategy getSortingStrategy() {
        return mSortingStrategy;
    }

    @Override
    public void setSortingStrategy(ListViewSortingStrategy sortingStrategy) {
        this.mSortingStrategy = sortingStrategy;
    }

    @Override
    public Resources getContextResources() {
        return getResources();
    }

    @Override
    public HistoryLocationsManager getLeftHistoryLocationManager() {
        return mLeftHistoryLocationManager;
    }

    @Override
    public HistoryLocationsManager getRightHistoryLocationManager() {
        return mRightHistoryLocationManager;
    }

    @Override
    public String getRightListSavedLocation() {
        return mRightListSavedLocation;
    }

    @Override
    public String getLeftListSavedLocation() {
        return mLeftListSavedLocation;
    }

    @Override
    public ArrayList<ListViewItem> getOperationItems() {
        return activePage == TerminalActivePage.LEFT ? mLeftAdapter.getSelectedItems() : mRightAdapter.getSelectedItems();
    }

    @Override
    public SelectionViewComponents getSelectionVisualItems() {
        return mSelectionVisualItems;
    }

    @Override
    public void setActivePage(TerminalActivePage activePage) {
        this.activePage = activePage;
    }

    @Override
    public ActionBarToggleMonitor getActionBarToggleMonitor() {
        return mActionBarToggleMonitor;
    }

    @Override
    public TerminalActivePage getActivePage() {
        return activePage;
    }
}
