package com.drk.terminal.ui.activity.terminal;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.drk.terminal.R;
import com.drk.terminal.model.listview.ListViewFiller;
import com.drk.terminal.model.listview.ListViewItem;
import com.drk.terminal.ui.activity.TerminalProgressActivity;
import com.drk.terminal.ui.activity.commander.CommanderActivity;
import com.drk.terminal.ui.adapter.ListViewAdapter;
import com.drk.terminal.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/16/13
 * Time: 10:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalActivity extends Activity {
    private static final String LOG_TAG = TerminalActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 0;
    private ToggleButton mShiftBtn, mCtrlBtn;
    private ListView mLeftList, mRightList;
    private ListViewAdapter mLeftAdapter, mRightAdapter;
    private boolean isLeftActive = true;
    private boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_activity_layout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
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
                if (isLeftActive) {
                    // todo save instance state on this activity
                    mLeftAdapter.clearBackPath(splitPath);
                    mLeftAdapter.changeDirectory(splitPath[splitPath.length - 1]);
                } else {
                    mRightAdapter.clearBackPath(splitPath);
                    mRightAdapter.changeDirectory(splitPath[splitPath.length - 1]);
                }
            }
        }
    }

    private void initViews() {
        mLeftList = (ListView) findViewById(R.id.left_directory_list);
        mRightList = (ListView) findViewById(R.id.right_directory_list);
        TextView leftPathLabel = (TextView) findViewById(R.id.path_location_in_left);
        TextView rightPathLabel = (TextView) findViewById(R.id.path_location_in_right);
        List<ListViewItem> listInfo = new ArrayList<ListViewItem>();
        ListViewFiller.fillingList(listInfo, StringUtil.PATH_SEPARATOR, null);
        mLeftAdapter = new ListViewAdapter(this, listInfo,
                new CurrentPathLabel(getResources(), leftPathLabel, rightPathLabel));
        mRightAdapter = new ListViewAdapter(this, new ArrayList<ListViewItem>(listInfo),
                new CurrentPathLabel(getResources(), rightPathLabel, leftPathLabel));
        mLeftList.setAdapter(mLeftAdapter);
        mRightList.setAdapter(mRightAdapter);
        mLeftList.setOnItemClickListener(new ListViewItemClickListener(mLeftAdapter, mLeftList));
        mRightList.setOnItemClickListener(new ListViewItemClickListener(mRightAdapter, mRightList));
        hideProgress();
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

    private final class LoadInfoTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            initViews();
        }
    }

    private final class ListViewItemClickListener implements AdapterView.OnItemClickListener {
        private final SelectionStrategy selectionStrategy;
        private final ListViewAdapter adapter;
        private final CurrentPathLabel pathLabel;
        private final ListView listView;

        private ListViewItemClickListener(ListViewAdapter adapter, ListView listView) {
            this.adapter = adapter;
            this.listView = listView;
            this.selectionStrategy = adapter.getSelectionStrategy();
            this.pathLabel = adapter.getPathLabel();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (pathLabel.getOwnLabel().getId() == R.id.path_location_in_left) {
                isLeftActive = true;
            } else {
                isLeftActive = false;
            }
            pathLabel.setPath(null);
            if (selectionStrategy.isCtrlToggle() ||
                    selectionStrategy.isShiftToggle()) {
                selectionStrategy.addSelection(position);
            } else {
                selectionStrategy.clear();
                ListViewItem selectedItem = adapter.getItem(position);
                if (selectedItem.isParentDots()) {
                    String backPath = adapter.getBackPath();
                    if (backPath != null) {
                        adapter.changeDirectory(backPath);
                        listView.smoothScrollToPosition(0);
                    }
                } else if (selectedItem.isDirectory()) {
                    if (selectedItem.canRead()) {
                        if (selectedItem.isLink()) {
                            Toast.makeText(TerminalActivity.this,
                                    "Link: " + selectedItem.getAbsPath(), Toast.LENGTH_SHORT).show();
                            String[] splitPath = selectedItem.getAbsPath().
                                    substring(1).split(StringUtil.PATH_SEPARATOR);
                            adapter.clearBackPath(splitPath);
                            adapter.changeDirectory(splitPath[splitPath.length - 1]);
                        } else {
                            adapter.changeDirectory(selectedItem.getFileName());
                        }
                        listView.smoothScrollToPosition(0);
                    } else {
                        Toast.makeText(TerminalActivity.this, "Selected directory: " +
                                adapter.getItem(position).getFileName(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TerminalActivity.this, "Selected file: " +
                            adapter.getItem(position).getFileName(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

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

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.action_commander) {
                Intent startIntent = new Intent(TerminalActivity.this, CommanderActivity.class);
                startIntent.putExtra(CommanderActivity.WORK_PATH_EXTRA,
                        isLeftActive? mLeftAdapter.getPathLabel().getCurrentLabel() :
                                        mRightAdapter.getPathLabel().getCurrentLabel());
                startActivityForResult(startIntent, REQUEST_CODE);
            }
        }
    };

    public void showProgress() {
        startActivity(new Intent(this, TerminalProgressActivity.class));
    }

    public void hideProgress() {
        sendStickyBroadcast(new Intent(TerminalProgressActivity.PROGRESS_DISMISS_ACTION));
    }
}
