package com.softsandr.terminal.ui.activity.terminal;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.drk.terminal.util.utils.FileOpeningUtil;
import com.drk.terminal.util.utils.FileUtil;
import com.drk.terminal.util.utils.StringUtil;
import com.softsandr.terminal.R;
import com.softsandr.terminal.model.listview.ListViewFiller;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.terminal.model.listview.ListViewSortingStrategy;
import com.softsandr.terminal.model.preferences.HistoryLocationsManager;
import com.softsandr.terminal.model.preferences.TerminalPreferences;
import com.softsandr.terminal.ui.activity.commander.CommanderActivity;
import com.softsandr.terminal.ui.activity.terminal.selection.SelectionStrategy;
import com.softsandr.terminal.ui.activity.terminal.selection.SelectionViewComponents;
import com.softsandr.terminal.ui.dialog.TerminalDialogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class TerminalActivity extends android.app.Activity {
    public static final int REQUEST_CODE = 0;
    private static final String LOG_TAG = TerminalActivity.class.getSimpleName();
    private static final String LEFT_FILE_LIST_PATH_BUNDLE = LOG_TAG + ".LEFT_FILE_LIST";
    private static final String RIGHT_FILE_LIST_PATH_BUNDLE = LOG_TAG + ".RIGHT_FILE_LIST";
    public static final String COMMON_EXIT_INTENT = TerminalActivity.class.getSimpleName() + ".COMMON_EXIT_INTENT";
    private final AbsListView.OnTouchListener mListTouchListener = new AbsListView.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Detect active list
            if (v.getId() == mLeftList.getId()) {
                activePage = TerminalActivePage.LEFT;
                mSelectionVisualItems.selectLeft();
            } else if (v.getId() == mRightList.getId()) {
                activePage = TerminalActivePage.RIGHT;
                mSelectionVisualItems.selectRight();
            }
            return false;
        }
    };
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            String destinationLocation = !activePage.equals(TerminalActivePage.LEFT) ? mLeftAdapter.getPathLabel().getFullPath() :
                    mRightAdapter.getPathLabel().getFullPath();
            String currentLocation = activePage.equals(TerminalActivePage.LEFT) ? mLeftAdapter.getPathLabel().getFullPath() :
                    mRightAdapter.getPathLabel().getFullPath();
            /* Menu items */
            if (v.equals(mCommMenuBtn)) {
                Intent startIntent = new Intent(TerminalActivity.this, CommanderActivity.class);
                startIntent.putExtra(CommanderActivity.WORK_PATH_EXTRA, currentLocation);
                startIntent.putExtra(CommanderActivity.OTHER_PATH_EXTRA, destinationLocation);
                startIntent.putExtra(CommanderActivity.ACTIVE_PAGE_EXTRA, activePage.equals(TerminalActivePage.LEFT));
                startActivityForResult(startIntent, REQUEST_CODE);
            } else if (v.equals(mShiftMenuBtn)) {
                mActionBarToggleMonitor.onClickShift();
            } else if (v.equals(mCtrlMenuBtn)) {
                mActionBarToggleMonitor.onClickCtrl();
            }
            /* Control buttons */
            else if (viewId == R.id.copy_btn) {
                if (!getOperationItems().isEmpty()) {
                    TerminalDialogUtil.showCopyDialog(TerminalActivity.this,
                            getOperationItems(),
                            destinationLocation,
                            currentLocation);
                }
            } else if (viewId == R.id.rename_btn) {
                if (!getOperationItems().isEmpty()) {
                    TerminalDialogUtil.showMoveRenameDialog(TerminalActivity.this,
                            getOperationItems(),
                            destinationLocation,
                            currentLocation);
                }
            } else if (viewId == R.id.mkdir_btn) {
                TerminalDialogUtil.showMkDirDialog(TerminalActivity.this, currentLocation, destinationLocation);
            } else if (viewId == R.id.delete_btn) {
                if (!getOperationItems().isEmpty()) {
                    TerminalDialogUtil.showDeleteDialog(TerminalActivity.this,
                            getOperationItems(),
                            currentLocation, destinationLocation);
                }
            }
            /* History buttons */
            else if (viewId == R.id.history_btn_in_left) {
                String[] locations = mLeftHistoryLocationManager.getActualHistoryLocations();
                if (locations.length > 0) {
                    TerminalDialogUtil.showHistoryDialog(TerminalActivity.this,
                            locations,
                            TerminalActivePage.LEFT);
                }
            } else if (viewId == R.id.history_btn_in_right) {
                String[] locations = mRightHistoryLocationManager.getActualHistoryLocations();
                if (locations.length > 0) {
                    TerminalDialogUtil.showHistoryDialog(TerminalActivity.this,
                            locations,
                            TerminalActivePage.RIGHT);
                }
            }
        }
    };
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
    private ListViewSortingStrategy mSortingStrategy = ListViewSortingStrategy.SORT_BY_NAME;
    private ListViewAdapter mLeftAdapter, mRightAdapter;
    private SelectionViewComponents mSelectionVisualItems;
    private TerminalActivePage activePage = TerminalActivePage.LEFT;
    private ActionBarButtonsToggleMonitor mActionBarToggleMonitor;
    private Button mShiftMenuBtn, mCommMenuBtn, mCtrlMenuBtn;
    private View mShiftBtnContainer, mCtrlBtnContainer;
    private ListView mLeftList, mRightList;
    private String mRightListSavedLocation;
    private String mLeftListSavedLocation;
    private TerminalPreferences mPreferences;
    private HistoryLocationsManager mLeftHistoryLocationManager;
    private HistoryLocationsManager mRightHistoryLocationManager;
    private SortingMenuItemsMonitor mSortingMenuItemsMonitor;
    private boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_activity_layout);
        mSelectionVisualItems = new SelectionViewComponents(this);
        readPreferences();
        initView();
        initActionBar();
    }

    private void initActionBar() {
        ActionBar bar = getActionBar();
        if (bar != null) {
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.RIGHT;
            LayoutInflater li = getLayoutInflater();
            View customView = li.inflate(R.layout.terminal_action_bar_custom_view, null);
            if (customView != null) {
                mShiftBtnContainer = customView.findViewById(R.id.action_bar_shift_btn_container);
                mCtrlBtnContainer = customView.findViewById(R.id.action_bar_ctrl_btn_container);
                mShiftMenuBtn = (Button) customView.findViewById(R.id.action_bar_shift_btn);
                mCtrlMenuBtn = (Button) customView.findViewById(R.id.action_bar_ctrl_btn);
                mCommMenuBtn = (Button) customView.findViewById(R.id.action_bar_comm_btn);
                mShiftMenuBtn.setOnClickListener(mOnClickListener);
                mCommMenuBtn.setOnClickListener(mOnClickListener);
                mCtrlMenuBtn.setOnClickListener(mOnClickListener);
                mActionBarToggleMonitor = new ActionBarButtonsToggleMonitor();
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
            new LoadLeftListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new LoadRightListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                            new LoadLeftListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            new LoadRightListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        mSortingMenuItemsMonitor = new SortingMenuItemsMonitor(sortByName, sortBySize, sortByModify);
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

    public ListViewAdapter getLeftListAdapter() {
        return mLeftAdapter;
    }

    public ListViewAdapter getRightListAdapter() {
        return mRightAdapter;
    }

    private ArrayList<ListViewItem> getOperationItems() {
        return activePage == TerminalActivePage.LEFT ? mLeftAdapter.getSelectedItems() : mRightAdapter.getSelectedItems();
    }

//    public void showProgress() {
//        startActivity(new Intent(this, TerminalProgressActivity.class));
//    }
//
//    public void hideProgress() {
//        sendStickyBroadcast(new Intent(TerminalProgressActivity.PROGRESS_DISMISS_ACTION));
//    }

    public TerminalActivePage getActivePage() {
        return activePage;
    }

    public ListViewSortingStrategy getSortingStrategy() {
        return mSortingStrategy;
    }

    private final class ListViewItemClickListener implements AdapterView.OnItemClickListener {
        private final SelectionStrategy selectionStrategy;
        private final ListViewAdapter adapter;
        private final ListView listView;

        private ListViewItemClickListener(ListViewAdapter adapter, ListView listView) {
            this.adapter = adapter;
            this.listView = listView;
            this.selectionStrategy = adapter.getSelectionStrategy();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // If parent dots clicked go up
            ListViewItem selectedItem = adapter.getItem(position);
            if (selectedItem.isParentDots()) {
                String backPath = adapter.getBackPath();
                if (backPath != null) {
                    selectionStrategy.clear();
                    adapter.changeDirectory(backPath);
                    listView.smoothScrollToPosition(0);
                }
            } else {
                if (selectionStrategy.isCtrlToggle() ||
                        selectionStrategy.isShiftToggle()) {
                    selectionStrategy.addSelection(position);
                } else {
                    selectionStrategy.clear();
                    if (selectedItem.isDirectory()) {
                        if (selectedItem.canRead()) {
                            if (selectedItem.isLink()) {
                                String[] splitPath = selectedItem.getAbsPath().
                                        substring(1).split(StringUtil.PATH_SEPARATOR);
                                adapter.clearBackPath(splitPath);
                                adapter.changeDirectory(splitPath[splitPath.length - 1]);
                            } else {
                                adapter.changeDirectory(selectedItem.getFileName());
                            }
                            listView.smoothScrollToPosition(0);
                        } else {
                            Toast.makeText(TerminalActivity.this, "Can't read directory " +
                                    adapter.getItem(position).getFileName() + ".",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        FileOpeningUtil.openFile(TerminalActivity.this, selectedItem.getAbsPath());
                    }
                }
            }
        }
    }

    private final class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener {
        private final ListViewAdapter adapter;

        private ListViewItemLongClickListener(ListViewAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ListViewItem item = adapter.getItem(position);
            if (item.isParentDots()) {
                Toast.makeText(TerminalActivity.this, "Directory path: " +
                        item.getAbsPath() + ".",
                        Toast.LENGTH_LONG).show();
            } else if (item.isDirectory()) {
                new DirectorySizeComputationTask().execute(item);
            }
            return true;
        }
    }

    private final class DirectorySizeComputationTask extends AsyncTask<ListViewItem, Void, Long> {

        @Override
        protected Long doInBackground(ListViewItem... params) {
            ListViewItem item = params[0];
            long size = 0l;
            try {
                size = FileUtil.getDirectorySize(new File(item.getAbsPath()));
            } catch (Exception e) {
                Log.e(LOG_TAG, "compute directory size:", e);
            }
            return size;
        }

        @Override
        protected void onPostExecute(Long size) {
            Toast.makeText(TerminalActivity.this, "Directory size = " + ListViewItem.readableFileSize(size),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private final class LoadLeftListTask extends AsyncTask<Void, Void, List<ListViewItem>> {

        @Override
        protected List<ListViewItem> doInBackground(Void... params) {
            List<ListViewItem> list = new ArrayList<ListViewItem>();
            ListViewFiller.fillListContent(mSortingStrategy, list, mLeftListSavedLocation);
            return list;
        }

        @Override
        protected void onPostExecute(List<ListViewItem> list) {
            TextView leftPathLabel = (TextView) findViewById(R.id.path_location_in_left);
            mLeftAdapter = new ListViewAdapter(TerminalActivity.this, list,
                    new CurrentPathLabel(leftPathLabel),
                    mLeftHistoryLocationManager);
            mLeftList.setAdapter(mLeftAdapter);
            mLeftAdapter.restoreBackPath(mLeftListSavedLocation);
            mLeftList.setOnItemClickListener(new ListViewItemClickListener(mLeftAdapter, mLeftList));
            mLeftList.setOnItemLongClickListener(new ListViewItemLongClickListener(mLeftAdapter));
            mLeftList.setOnTouchListener(mListTouchListener);
        }
    }

    private final class LoadRightListTask extends AsyncTask<Void, Void, List<ListViewItem>> {

        @Override
        protected List<ListViewItem> doInBackground(Void... params) {
            List<ListViewItem> list = new ArrayList<ListViewItem>();
            ListViewFiller.fillListContent(mSortingStrategy, list, mRightListSavedLocation);
            return list;
        }

        @Override
        protected void onPostExecute(List<ListViewItem> list) {
            TextView rightPathLabel = (TextView) findViewById(R.id.path_location_in_right);
            mRightAdapter = new ListViewAdapter(TerminalActivity.this, list,
                    new CurrentPathLabel(rightPathLabel),
                    mRightHistoryLocationManager);
            mRightList.setAdapter(mRightAdapter);
            mRightAdapter.restoreBackPath(mRightListSavedLocation);
            mRightList.setOnItemClickListener(new ListViewItemClickListener(mRightAdapter, mRightList));
            mRightList.setOnItemLongClickListener(new ListViewItemLongClickListener(mRightAdapter));
            mRightList.setOnTouchListener(mListTouchListener);
        }
    }

    private final class ActionBarButtonsToggleMonitor {
        private boolean isShiftToggled;
        private boolean isCtrlToggled;

        private void onClickShift() {
            if (isCtrlToggled) {
                isCtrlToggled = false;
                mCtrlBtnContainer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                mLeftAdapter.getSelectionStrategy().setCtrlToggle(false);
                mRightAdapter.getSelectionStrategy().setCtrlToggle(false);
            }
            if (isShiftToggled) {
                isShiftToggled = false;
                mShiftBtnContainer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            } else {
                isShiftToggled = true;
                mShiftBtnContainer.setBackgroundColor(getResources().getColor(R.color.COLOR_FEA50A));
            }
            mLeftAdapter.getSelectionStrategy().setShiftToggle(isShiftToggled);
            mRightAdapter.getSelectionStrategy().setShiftToggle(isShiftToggled);
        }

        private void onClickCtrl() {
            if (isShiftToggled) {
                isShiftToggled = false;
                mShiftBtnContainer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                mLeftAdapter.getSelectionStrategy().setShiftToggle(false);
                mRightAdapter.getSelectionStrategy().setShiftToggle(false);
            }
            if (isCtrlToggled) {
                isCtrlToggled = false;
                mCtrlBtnContainer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            } else {
                isCtrlToggled = true;
                mCtrlBtnContainer.setBackgroundColor(getResources().getColor(R.color.COLOR_FEA50A));
            }
            mLeftAdapter.getSelectionStrategy().setCtrlToggle(isCtrlToggled);
            mRightAdapter.getSelectionStrategy().setCtrlToggle(isCtrlToggled);
        }
    }

    private final class SortingMenuItemsMonitor {
        private final MenuItem mSortByName;
        private final MenuItem mSortBySize;
        private final MenuItem mSortByModify;

        private SortingMenuItemsMonitor(MenuItem mSortByName, MenuItem mSortBySize, MenuItem mSortByModify) {
            this.mSortByName = mSortByName;
            this.mSortBySize = mSortBySize;
            this.mSortByModify = mSortByModify;
        }

        public void onMenuSelected(MenuItem menuItem) {
            menuItem.setChecked(true);
            if (!menuItem.equals(mSortByName)) {
                mSortByName.setChecked(false);
            }
            if (!menuItem.equals(mSortBySize)) {
                mSortBySize.setChecked(false);
            }
            if (!menuItem.equals(mSortByModify)) {
                mSortByModify.setChecked(false);
            }
            // sorting type
            if (menuItem.equals(mSortByName)) {
                mSortingStrategy = ListViewSortingStrategy.SORT_BY_NAME;
            } else if (menuItem.equals(mSortBySize)) {
                mSortingStrategy = ListViewSortingStrategy.SORT_BY_SIZE;
            } else if (menuItem.equals(mSortByModify)) {
                mSortingStrategy = ListViewSortingStrategy.SORT_BY_DATE;
            }
            mLeftAdapter.changeDirectory(mLeftAdapter.getPathLabel().getFullPath());
            mRightAdapter.changeDirectory(mRightAdapter.getPathLabel().getFullPath());
            mLeftAdapter.getSelectionStrategy().clear();
            mRightAdapter.getSelectionStrategy().clear();
        }
    }
}
