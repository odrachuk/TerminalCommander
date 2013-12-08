package com.softsandr.terminal.ui.activity.terminal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import com.softsandr.terminal.model.shpref.HistoryLocationsManager;
import com.softsandr.terminal.model.shpref.TerminalPreferences;
import com.softsandr.terminal.ui.activity.commander.CommanderActivity;
import com.softsandr.terminal.ui.activity.progress.TerminalProgressActivity;
import com.softsandr.terminal.ui.activity.terminal.adapter.ListViewAdapter;
import com.softsandr.terminal.ui.activity.terminal.selection.SelectionStrategy;
import com.softsandr.terminal.ui.activity.terminal.selection.SelectionVisualItems;
import com.softsandr.terminal.ui.dialog.TerminalAppListDialog;
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
    private final CompoundButton.OnCheckedChangeListener mOnToggleListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.action_shift) {
                mLeftAdapter.getSelectionStrategy().setShiftToggle(isChecked);
                mRightAdapter.getSelectionStrategy().setShiftToggle(isChecked);
                if (isChecked && mCtrlBtn.isChecked()) {
                    mCtrlBtn.setChecked(false);
                    mLeftAdapter.getSelectionStrategy().setCtrlToggle(false);
                    mRightAdapter.getSelectionStrategy().setCtrlToggle(false);
                }
            } else if (buttonView.getId() == R.id.action_ctrl) {
                mLeftAdapter.getSelectionStrategy().setCtrlToggle(isChecked);
                mRightAdapter.getSelectionStrategy().setCtrlToggle(isChecked);
                if (isChecked && mShiftBtn.isChecked()) {
                    mShiftBtn.setChecked(false);
                    mLeftAdapter.getSelectionStrategy().setShiftToggle(false);
                    mRightAdapter.getSelectionStrategy().setShiftToggle(false);
                }
            }
        }
    };
    private final AbsListView.OnTouchListener mListTouchListener = new AbsListView.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Detect active list
            if (v.getId() == mLeftList.getId()) {
                activePage = ActivePage.LEFT;
                mSelectionVisualItems.selectLeft();
            } else if (v.getId() == mRightList.getId()) {
                activePage = ActivePage.RIGHT;
                mSelectionVisualItems.selectRight();
            }
            return false;
        }
    };
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            String destinationLocation = !activePage.equals(ActivePage.LEFT) ? mLeftAdapter.getPathLabel().getFullPath() :
                    mRightAdapter.getPathLabel().getFullPath();
            String currentLocation = activePage.equals(ActivePage.LEFT) ? mLeftAdapter.getPathLabel().getFullPath() :
                    mRightAdapter.getPathLabel().getFullPath();
            if (viewId == R.id.action_commander) {
                Intent startIntent = new Intent(TerminalActivity.this, CommanderActivity.class);
                startIntent.putExtra(CommanderActivity.WORK_PATH_EXTRA, currentLocation);
                startIntent.putExtra(CommanderActivity.OTHER_PATH_EXTRA, destinationLocation);
                startIntent.putExtra(CommanderActivity.ACTIVE_PAGE_EXTRA, activePage.equals(ActivePage.LEFT));
                startActivityForResult(startIntent, REQUEST_CODE);
            } else if (viewId == R.id.copy_btn) {
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
            } else if (viewId == R.id.history_btn_in_left) {
                String[] locations = mLeftHistoryLocationManager.getActualHistoryLocations();
                if (locations.length > 0) {
                    TerminalDialogUtil.showHistoryDialog(TerminalActivity.this,
                            locations,
                            ActivePage.LEFT);
                }
            } else if (viewId == R.id.history_btn_in_right) {
                String[] locations = mRightHistoryLocationManager.getActualHistoryLocations();
                if (locations.length > 0) {
                    TerminalDialogUtil.showHistoryDialog(TerminalActivity.this,
                            locations,
                            ActivePage.RIGHT);
                }
            }
        }
    };
    private ListViewAdapter mLeftAdapter, mRightAdapter;
    private SelectionVisualItems mSelectionVisualItems;
    private ActivePage activePage = ActivePage.LEFT;
    private ToggleButton mShiftBtn, mCtrlBtn;
    private ListView mLeftList, mRightList;
    private String mRightListSavedLocation;
    private String mLeftListSavedLocation;
    private TerminalPreferences mPreferences;
    private HistoryLocationsManager mLeftHistoryLocationManager;
    private HistoryLocationsManager mRightHistoryLocationManager;
    private boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_activity_layout);
        mSelectionVisualItems = new SelectionVisualItems(this);
        readPreferences();
        initView();
    }

    private void readPreferences() {
        mPreferences = new TerminalPreferences(this);
        // read last locations
        mLeftListSavedLocation = mPreferences.loadLastLeftLocations();
        mRightListSavedLocation = mPreferences.loadLastRightLocations();
        // read locations history
        mLeftHistoryLocationManager = new HistoryLocationsManager(mPreferences, ActivePage.LEFT);
        mRightHistoryLocationManager = new HistoryLocationsManager(mPreferences, ActivePage.RIGHT);
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
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        if (!isPaused) {
            new LoadLeftListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new LoadRightListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        isPaused = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data.hasExtra(CommanderActivity.WORK_PATH_EXTRA)) {
                String pathFromCommander = data.getStringExtra(CommanderActivity.WORK_PATH_EXTRA);
                boolean isLeftActive = data.getBooleanExtra(CommanderActivity.ACTIVE_PAGE_EXTRA, true);
                String destinationPath = data.getStringExtra(CommanderActivity.OTHER_PATH_EXTRA);
                String[] splitPath = pathFromCommander.
                        substring(1).split(StringUtil.PATH_SEPARATOR);
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
        // setup shift
        MenuItem shiftItem = menu.findItem(R.id.action_shift);
        if (shiftItem != null) {
            mShiftBtn = (ToggleButton) shiftItem.getActionView();
            mShiftBtn.setOnCheckedChangeListener(mOnToggleListener);
        }
        // setup ctrl
        MenuItem ctrlItem = menu.findItem(R.id.action_ctrl);
        if (ctrlItem != null) {
            mCtrlBtn = (ToggleButton) ctrlItem.getActionView();
            mCtrlBtn.setOnCheckedChangeListener(mOnToggleListener);
        }
        // setup commander
        MenuItem commItem = menu.findItem(R.id.action_commander);
        if (ctrlItem != null) {
            Button tabBtn = (Button) commItem.getActionView();
            tabBtn.setOnClickListener(mOnClickListener);
        }
        // setup tab
        MenuItem tabItem = menu.findItem(R.id.action_tab);
        tabItem.setVisible(false);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        saveDataBeforeDestroy();
        super.onDestroy();
    }

    private void saveDataBeforeDestroy() {
        mPreferences.saveLastLocations(mLeftAdapter.getPathLabel().getFullPath(),
                mRightAdapter.getPathLabel().getFullPath());
        mPreferences.saveLeftHistoryLocations(mLeftHistoryLocationManager.getActualHistoryLocations());
        mPreferences.saveRightHistoryLocations(mRightHistoryLocationManager.getActualHistoryLocations());
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
        return activePage == ActivePage.LEFT ? mLeftAdapter.getSelectedItems() : mRightAdapter.getSelectedItems();
    }

    public void showProgress() {
        startActivity(new Intent(this, TerminalProgressActivity.class));
    }

    public void hideProgress() {
        sendStickyBroadcast(new Intent(TerminalProgressActivity.PROGRESS_DISMISS_ACTION));
    }

    public ActivePage getActivePage() {
        return activePage;
    }

    public enum ActivePage {
        LEFT,
        RIGHT
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
                        String fileName = adapter.getItem(position).getFileName();
//                        if (FileOpeningUtil.checkOpening(fileName, getPackageManager())) {
                            TerminalDialogUtil.showAppDialog(TerminalActivity.this, fileName);
//                        }
//                        else {
//                            Toast.makeText(TerminalActivity.this, getString(R.string.not_app_for_openning_file),
//                                    Toast.LENGTH_SHORT).show();
//                        }
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
            ListViewFiller.fillingList(list, mLeftListSavedLocation);
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
            ListViewFiller.fillingList(list, mRightListSavedLocation);
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
}
