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
package com.softsandr.terminal.activity.terminal;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.*;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.commander.CommanderActivityImpl;
import com.softsandr.terminal.activity.preference.TerminalPreferenceActivity;
import com.softsandr.terminal.activity.terminal.adapter.ListViewAdapter;
import com.softsandr.terminal.activity.terminal.async.LoadLeftListTask;
import com.softsandr.terminal.activity.terminal.async.LoadRightListTask;
import com.softsandr.terminal.activity.terminal.listener.ListViewItemClickListener;
import com.softsandr.terminal.activity.terminal.listener.ListViewTouchListener;
import com.softsandr.terminal.activity.terminal.listener.TerminalClickListener;
import com.softsandr.terminal.activity.terminal.monitor.ActionBarToggleMonitor;
import com.softsandr.terminal.activity.terminal.monitor.HistoryLocationsMonitor;
import com.softsandr.terminal.activity.terminal.monitor.SortingMenuItemsMonitor;
import com.softsandr.terminal.activity.terminal.selection.SelectionUiComponents;
import com.softsandr.terminal.activity.terminal.service.SizeComputationService;
import com.softsandr.terminal.data.listview.ListViewItem;
import com.softsandr.terminal.data.listview.ListViewSortingStrategy;
import com.softsandr.terminal.data.preferences.PreferenceController;
import com.softsandr.terminal.dialog.TerminalDialogUtil;
import com.softsandr.utils.file.FileOpeningUtil;
import com.softsandr.utils.orient.DetermineOrientationUtil;
import com.softsandr.utils.string.StringUtil;

import java.util.ArrayList;

/**
 * The main activity that contain list of files
 */
public class TerminalActivityImpl extends Activity implements TerminalActivity {
    public static final int REQUEST_CODE = 0;
    public static final String COMMON_EXIT_INTENT = TerminalActivityImpl.class.getSimpleName() + ".COMMON_EXIT_INTENT";
    public static final String CLEAR_HISTORY_INTENT = TerminalActivityImpl.class.getSimpleName() + ".CLEAR_HISTORY_INTENT";
    public static final String SETTING_CHANGED_INTENT = TerminalActivityImpl.class.getSimpleName() + ".SETTING_CHANGED_INTENT";
    public static final String HIDE_PROGRESS_INTENT = TerminalActivityImpl.class.getSimpleName() + ".HIDE_PROGRESS_INTENT";

    private static final String LOG_TAG = TerminalActivityImpl.class.getSimpleName();
    private static final String LEFT_FILE_LIST_PATH_BUNDLE = LOG_TAG + ".LEFT_FILE_LIST";
    private static final String RIGHT_FILE_LIST_PATH_BUNDLE = LOG_TAG + ".RIGHT_FILE_LIST";
    private static final String ACTIVE_PROGRESS_BUNDLE = LOG_TAG + ".ACTIVE_PROGRESS_BUNDLE";

    private AbsListView.OnTouchListener mListTouchListener;
    private View.OnClickListener mOnClickListener;
    private ListViewSortingStrategy mSortingStrategy = ListViewSortingStrategy.SORT_BY_NAME;
    private ListViewAdapter mLeftAdapter, mRightAdapter;
    private SelectionUiComponents mSelectionVisualItems;
    private ActivePage activePage = ActivePage.LEFT;
    private ListView mLeftList, mRightList;
    private String mRightListSavedLocation, mLeftListSavedLocation;
    private ActionBarToggleMonitor mActionBarToggleMonitor;
    private HistoryLocationsMonitor mLeftHistoryLocationMonitor;
    private HistoryLocationsMonitor mRightHistoryLocationMonitor;
    private SortingMenuItemsMonitor mSortingMenuItemsMonitor;
    private View rootContainer;
    private boolean isPaused, isSettingsChanged, isActiveProgress;

    private ProgressDialog progressDialog;

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(COMMON_EXIT_INTENT)) {
                    finish();
                } else if (action.equals(CLEAR_HISTORY_INTENT)) {
                    mLeftHistoryLocationMonitor.clearHistory();
                    mRightHistoryLocationMonitor.clearHistory();
                } else if (action.equals(SETTING_CHANGED_INTENT)) {
                    isSettingsChanged = true;
                } else if (action.equals(HIDE_PROGRESS_INTENT)) {
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        TerminalDialogUtil.showPropertiesDialog(TerminalActivityImpl.this,                                (ListViewItem) extras.getParcelable(SizeComputationService.OUTPUT_ITEM));
                    }
                    hideProgress();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_activity_layout);
        mSelectionVisualItems = new SelectionUiComponents(this);
        mListTouchListener = new ListViewTouchListener(this);
        mOnClickListener = new TerminalClickListener(this);
        readPreferences();
        initView();
        initActionBar();
    }

    private void readPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // read last locations
        mLeftListSavedLocation = PreferenceController.loadLastLeftLocation(sharedPreferences);
        mRightListSavedLocation = PreferenceController.loadLastRightLocation(sharedPreferences);
        mSortingStrategy = PreferenceController.loadSortingStrategy(sharedPreferences);
        // read locations history
        mLeftHistoryLocationMonitor = new HistoryLocationsMonitor(PreferenceController.loadLeftHistoryLocations(sharedPreferences));
        mRightHistoryLocationMonitor = new HistoryLocationsMonitor(PreferenceController.loadRightHistoryLocations(sharedPreferences));
    }

    private void initView() {
        findViewById(R.id.copy_btn).setOnClickListener(mOnClickListener);
        findViewById(R.id.move_btn).setOnClickListener(mOnClickListener);
        findViewById(R.id.rename_btn).setOnClickListener(mOnClickListener);
        findViewById(R.id.mkdir_btn).setOnClickListener(mOnClickListener);
        findViewById(R.id.delete_btn).setOnClickListener(mOnClickListener);
        findViewById(R.id.history_btn_in_left).setOnClickListener(mOnClickListener);
        findViewById(R.id.history_btn_in_right).setOnClickListener(mOnClickListener);
        mLeftList = (ListView) findViewById(R.id.left_directory_list);
        mRightList = (ListView) findViewById(R.id.right_directory_list);
        rootContainer = findViewById(R.id.terminal_main_container);
        registerForContextMenu(mLeftList);
        registerForContextMenu(mRightList);
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

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(COMMON_EXIT_INTENT);
        intentFilter.addAction(CLEAR_HISTORY_INTENT);
        intentFilter.addAction(SETTING_CHANGED_INTENT);
        intentFilter.addAction(HIDE_PROGRESS_INTENT);
        registerReceiver(mBroadcastReceiver, intentFilter);
        checkSettingsChanges();
        // refresh panel if show activity in first or some settings ui components was changed
        if (!isPaused || isSettingsChanged) {
            new LoadLeftListTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new LoadRightListTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        isPaused = false;
        isSettingsChanged = false;
        // reshow progress if needs
        if (isActiveProgress) {
            showProgress();
        }
    }

    /**
     * Used for change ui parameters if settings setup changes for that
     */
    private void checkSettingsChanges() {
        final int color = PreferenceController.loadTerminalBgColor(this, PreferenceManager.getDefaultSharedPreferences(this));
        rootContainer.setBackgroundColor(color);
        if (!DetermineOrientationUtil.isLandscapeOrientation(getResources())) {
            findViewById(R.id.contentLayout).setBackgroundColor(color);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
        if (isActiveProgress) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        mActionBarToggleMonitor.clear();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mLeftAdapter != null) {
            outState.putString(LEFT_FILE_LIST_PATH_BUNDLE, mLeftAdapter.getLocationLabel().getPath());
        }
        if (mRightAdapter != null) {
            outState.putString(RIGHT_FILE_LIST_PATH_BUNDLE, mRightAdapter.getLocationLabel().getPath());
        }
        outState.putBoolean(ACTIVE_PROGRESS_BUNDLE, isActiveProgress);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mLeftListSavedLocation = savedInstanceState.getString(LEFT_FILE_LIST_PATH_BUNDLE);
            mRightListSavedLocation = savedInstanceState.getString(RIGHT_FILE_LIST_PATH_BUNDLE);
            isActiveProgress = savedInstanceState.getBoolean(ACTIVE_PROGRESS_BUNDLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data.hasExtra(CommanderActivityImpl.WORK_PATH_EXTRA)) {
                String pathFromCommander = data.getStringExtra(CommanderActivityImpl.WORK_PATH_EXTRA);
                boolean isLeftActive = data.getBooleanExtra(CommanderActivityImpl.ACTIVE_PAGE_EXTRA, true);
                String destinationPath = data.getStringExtra(CommanderActivityImpl.OTHER_PATH_EXTRA);
                String[] splitPath = new String[0];
                if (pathFromCommander != null) {
                    splitPath = pathFromCommander.substring(1).split(StringUtil.PATH_SEPARATOR);
                }
                if (mLeftAdapter != null && mRightAdapter != null) {
                    switch (activePage) {
                        case LEFT:
                            mLeftAdapter.clearLocationHistory(splitPath);
                            mLeftAdapter.changeDirectory(splitPath[splitPath.length - 1]);
                            break;
                        case RIGHT:
                            mRightAdapter.clearLocationHistory(splitPath);
                            mRightAdapter.changeDirectory(splitPath[splitPath.length - 1]);
                            break;
                    }
                } else {
                    activePage = isLeftActive ? ActivePage.LEFT : ActivePage.RIGHT;
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
            case R.id.action_refresh:
                switch (activePage) {
                    case LEFT:
                        if (mLeftAdapter != null) {
                            mLeftAdapter.changeDirectory(mLeftAdapter.getLocationLabel().getPath());
                            mLeftAdapter.getSelectionMonitor().clear();
                            mLeftList.smoothScrollToPosition(0);
                        }
                        break;
                    case RIGHT:
                        if (mRightAdapter != null) {
                            mRightAdapter.changeDirectory(mRightAdapter.getLocationLabel().getPath());
                            mRightAdapter.getSelectionMonitor().clear();
                            mRightList.smoothScrollToPosition(0);
                        }
                        break;
                }
                return true;
            case R.id.action_quit:
                sendBroadcast(new Intent(COMMON_EXIT_INTENT));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, TerminalPreferenceActivity.class));
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
        unregisterReceiver(mBroadcastReceiver);
        saveDataBeforeDestroy();
        super.onDestroy();
    }

    private void saveDataBeforeDestroy() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences != null && mLeftAdapter != null && mRightAdapter != null) {
            PreferenceController.saveLastLocations(
                    sharedPreferences,
                    mLeftAdapter.getLocationLabel().getPath(),
                    mRightAdapter.getLocationLabel().getPath());
            PreferenceController.saveLeftHistoryLocations(
                    sharedPreferences,
                    mLeftHistoryLocationMonitor.getActualHistoryLocations());
            PreferenceController.saveRightHistoryLocations(
                    sharedPreferences,
                    mRightHistoryLocationMonitor.getActualHistoryLocations());
            PreferenceController.saveSortingStrategy(sharedPreferences, mSortingStrategy);
        }
    }

    @Override
    public void setLeftListAdapter(ListViewAdapter adapter) {
        mLeftAdapter = adapter;
        mLeftList.setAdapter(mLeftAdapter);
        mLeftList.setOnItemClickListener(new ListViewItemClickListener(this, mLeftAdapter, mLeftList));
        mLeftList.setOnTouchListener(mListTouchListener);
        mLeftAdapter.restoreHistoryLocation(mLeftListSavedLocation);
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
        mRightList.setOnTouchListener(mListTouchListener);
        mRightAdapter.restoreHistoryLocation(mRightListSavedLocation);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.equals(mLeftList)) {
            MenuInflater inflater = getMenuInflater();
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            ListViewItem selectedItem = mLeftAdapter.getItem(info.position);
            menu.setHeaderTitle(selectedItem.getFileName());
            if (selectedItem.isParentDots()) {
                inflater.inflate(R.menu.context_menu_for_parent, menu);
            } else if (selectedItem.isDirectory()) {
                inflater.inflate(R.menu.context_menu_for_directory, menu);
            } else {
                inflater.inflate(R.menu.context_menu_for_file, menu);
            }
        } else if (v.equals(mRightList)) {
            MenuInflater inflater = getMenuInflater();
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            ListViewItem selectedItem = mRightAdapter.getItem(info.position);
            menu.setHeaderTitle(selectedItem.getFileName());
            if (selectedItem.isParentDots()) {
                inflater.inflate(R.menu.context_menu_for_parent, menu);
            } else if (selectedItem.isDirectory()) {
                inflater.inflate(R.menu.context_menu_for_directory, menu);
            } else {
                inflater.inflate(R.menu.context_menu_for_file, menu);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "onContextItemSelected");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ListViewItem listViewItem = null;
        if (info != null) {
            if (activePage == ActivePage.LEFT) {
                listViewItem = mLeftAdapter.getItem(info.position);
            } else {
                listViewItem = mRightAdapter.getItem(info.position);
            }
        }
        switch (item.getItemId()) {
            case R.id.context_action_file_properties:
                TerminalDialogUtil.showPropertiesDialog(TerminalActivityImpl.this, listViewItem);
                return true;
            case R.id.context_action_parent_properties:
            case R.id.context_action_dir_properties:
                if (listViewItem != null) {
                    showProgress();
                    Intent intent = new Intent(this, SizeComputationService.class);
                    intent.putExtra(SizeComputationService.INPUT_ITEM, listViewItem);
                    startService(intent);
                }
                return true;
//            case_man R.id.context_action_file_share:
//                if (listViewItem != null) {
//                    FileOpeningUtil.shareFile(TerminalActivityImpl.this, listViewItem.getAbsPath());
//                }
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(TerminalActivityImpl.this);
        progressDialog.setMessage(getString(R.string.context_menu_text_size_computation));
        progressDialog.setCancelable(false);
        progressDialog.show();
        isActiveProgress = true;
    }

    private void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
            isActiveProgress = false;
        }
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
    public HistoryLocationsMonitor getLeftHistoryLocationMonitor() {
        return mLeftHistoryLocationMonitor;
    }

    @Override
    public HistoryLocationsMonitor getRightHistoryLocationMonitor() {
        return mRightHistoryLocationMonitor;
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
        return activePage == ActivePage.LEFT ? mLeftAdapter.getSelectedList() : mRightAdapter.getSelectedList();
    }

    @Override
    public SelectionUiComponents getSelectionVisualItems() {
        return mSelectionVisualItems;
    }

    @Override
    public void setActivePage(ActivePage activePage) {
        this.activePage = activePage;
    }

    @Override
    public ActionBarToggleMonitor getActionBarToggleMonitor() {
        return mActionBarToggleMonitor;
    }

    @Override
    public ActivePage getActivePage() {
        return activePage;
    }
}
