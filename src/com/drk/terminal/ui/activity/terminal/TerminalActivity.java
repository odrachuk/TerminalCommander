package com.drk.terminal.ui.activity.terminal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.drk.terminal.R;
import com.drk.terminal.model.listview.ListViewFiller;
import com.drk.terminal.model.listview.ListViewItem;
import com.drk.terminal.ui.activity.commander.CommanderActivity;
import com.drk.terminal.ui.activity.progress.TerminalProgressActivity;
import com.drk.terminal.ui.adapter.ListViewAdapter;
import com.drk.terminal.ui.dialog.TerminalDialogUtil;
import com.drk.terminal.utils.FileUtil;
import com.drk.terminal.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class TerminalActivity extends android.app.Activity {
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
                startIntent.putExtra(CommanderActivity.WORK_PATH_EXTRA,
                        activePage == ActivePage.LEFT ? mLeftAdapter.getPathLabel().getCurrentLabel() :
                                mRightAdapter.getPathLabel().getCurrentLabel());
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
            }
        }
    };

    private static final String LOG_TAG = TerminalActivity.class.getSimpleName();
    private static final String FILE_LIST_BUNDLE = LOG_TAG + ".FILE_LIST";
    private ListViewAdapter mLeftAdapter, mRightAdapter;
    private SelectionVisualItems mSelectionVisualItems;
    private ActivePage activePage = ActivePage.LEFT;
    public static final int REQUEST_CODE = 0;
    private ToggleButton mShiftBtn, mCtrlBtn;
    private ListView mLeftList, mRightList;
    private boolean isPaused;
    private ArrayList<ListViewItem> mFilesList = new ArrayList<ListViewItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mFilesList = savedInstanceState.getParcelableArrayList(FILE_LIST_BUNDLE);
        }
        setContentView(R.layout.terminal_activity_layout);
        mSelectionVisualItems = new SelectionVisualItems(this);
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(FILE_LIST_BUNDLE, mFilesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        if (!isPaused) {
            isPaused = false;
            showProgress();
            new LoadInfoTask().execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data.hasExtra(CommanderActivity.WORK_PATH_EXTRA)) {
                String pathFromCommander = data.getStringExtra(CommanderActivity.WORK_PATH_EXTRA);
                String[] splitPath = pathFromCommander.
                        substring(1).split(StringUtil.PATH_SEPARATOR);
                switch (activePage) {
                    case LEFT:
                        // todo save instance state on this activity
                        mLeftAdapter.clearBackPath(splitPath);
                        mLeftAdapter.changeDirectory(splitPath[splitPath.length - 1]);
                        break;
                    case RIGHT:
                        mRightAdapter.clearBackPath(splitPath);
                        mRightAdapter.changeDirectory(splitPath[splitPath.length - 1]);
                        break;
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
            case R.id.action_serrings:
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

    private void initView() {
        findViewById(R.id.copy_btn).setOnClickListener(mOnClickListener);
        findViewById(R.id.rename_btn).setOnClickListener(mOnClickListener);
        findViewById(R.id.mkdir_btn).setOnClickListener(mOnClickListener);
        findViewById(R.id.delete_btn).setOnClickListener(mOnClickListener);
        mLeftList = (ListView) findViewById(R.id.left_directory_list);
        mRightList = (ListView) findViewById(R.id.right_directory_list);
    }

    private void fillLists() {
        TextView leftPathLabel = (TextView) findViewById(R.id.path_location_in_left);
        TextView rightPathLabel = (TextView) findViewById(R.id.path_location_in_right);
        if (mFilesList.isEmpty()) { // filling not necessary when we restoring from saved state
            ListViewFiller.fillingList(mFilesList, StringUtil.PATH_SEPARATOR, null);
        }
        mLeftAdapter = new ListViewAdapter(this, mFilesList,
                new CurrentPathLabel(leftPathLabel));
        mRightAdapter = new ListViewAdapter(this, new ArrayList<ListViewItem>(mFilesList),
                new CurrentPathLabel(rightPathLabel));
        mLeftList.setAdapter(mLeftAdapter);
        mRightList.setAdapter(mRightAdapter);
        mLeftList.setOnItemClickListener(new ListViewItemClickListener(mLeftAdapter, mLeftList));
        mLeftList.setOnItemLongClickListener(new ListViewItemLongClickListener(mLeftAdapter));
        mLeftList.setOnTouchListener(mListTouchListener);
        mRightList.setOnItemClickListener(new ListViewItemClickListener(mRightAdapter, mRightList));
        mRightList.setOnItemLongClickListener(new ListViewItemLongClickListener(mRightAdapter));
        mRightList.setOnTouchListener(mListTouchListener);
        hideProgress();
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

    private final class LoadInfoTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "LoadInfoTask", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            fillLists();
        }
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
                        Toast.makeText(TerminalActivity.this, "Selected file " +
                                adapter.getItem(position).getFileName() + ".",
                                Toast.LENGTH_SHORT).show();
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
}
