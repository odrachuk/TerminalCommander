package com.drk.terminal.ui.activity.terminal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;
import com.drk.terminal.R;
import com.drk.terminal.model.filesystem.ProcessDirectory;
import com.drk.terminal.model.listview.ListViewItem;
import com.drk.terminal.ui.activity.commander.CommanderActivity;
import com.drk.terminal.ui.adapter.ListViewAdapter;
import com.drk.terminal.utils.DirectoryUtil;
import com.drk.terminal.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    private boolean isShiftToggle, isCtrlToggle;
    private ToggleButton mShiftBtn, mCtrlBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_activity_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadInfoTask().execute();
    }

    private void initViews() {
        prepareLeftList();
        prepareRightList();
    }

    private void prepareLeftList() {
        final ListView listView = (ListView) findViewById(R.id.left_directory_list);
        final List<ListViewItem> filesList = new ArrayList<ListViewItem>();
        filesList.add(new ListViewItem(true, true, null, StringUtil.PARENT_DOTS,
                -1, 0l));
        new ProcessDirectory(new ProcessDirectory.ProcessDirectoryStrategy() {
            @Override
            public void processDirectory(File file) {
                try {
                    filesList.add(new ListViewItem(true,
                            file.canRead(),
                            file.getParent(),
                            DirectoryUtil.isSymlink(file) ?
                            StringUtil.DIRECTORY_LINK_PREFIX +
                                    file.getName():
                            StringUtil.PATH_SEPARATOR +
                                    file.getName(),
                            file.getUsableSpace(),
                            file.lastModified()));
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepareLeftList", e);
                }
            }

            @Override
            public void processFile(File file) {
                try {
                    filesList.add(new ListViewItem(false,
                            file.canRead(),
                            file.getParent(),
                            DirectoryUtil.isSymlink(file) ?
                                    StringUtil.FILE_LINK_PREFIX +
                                            file.getName():
                                            file.getName(),
                            file.getUsableSpace(),
                            file.lastModified()));
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepareLeftList", e);
                }
            }
        }, "").start("/");
        Collections.sort(filesList);
        final ListViewAdapter adapter = new ListViewAdapter(this, filesList);
        listView.setAdapter(adapter);
    }

    private void prepareRightList() {
        final ListView listView = (ListView) findViewById(R.id.right_directory_list);
        final List<ListViewItem> filesList = new ArrayList<ListViewItem>();
        filesList.add(new ListViewItem(true, true, null, StringUtil.PARENT_DOTS,
                -1, 0l));
        new ProcessDirectory(new ProcessDirectory.ProcessDirectoryStrategy() {
            @Override
            public void processDirectory(File file) {
                try {
                    filesList.add(new ListViewItem(true,
                            file.canRead(),
                            file.getParent(),
                            DirectoryUtil.isSymlink(file) ?
                                    StringUtil.DIRECTORY_LINK_PREFIX +
                                            file.getName():
                                    StringUtil.PATH_SEPARATOR +
                                            file.getName(),
                            file.getUsableSpace(),
                            file.lastModified()));
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepareRightList", e);
                }
            }

            @Override
            public void processFile(File file) {
                try {
                    filesList.add(new ListViewItem(false,
                            file.canRead(),
                            file.getParent(),
                            DirectoryUtil.isSymlink(file) ?
                                    StringUtil.FILE_LINK_PREFIX +
                                        file.getName():
                                        file.getName(),
                            file.getUsableSpace(),
                            file.lastModified()));
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepareRightList", e);
                }
            }
        }, "").start("/");
        Collections.sort(filesList);
        final ListViewAdapter adapter = new ListViewAdapter(this, filesList);
        listView.setAdapter(adapter);
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
        ProgressDialog progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressDialog(TerminalActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Data downloading ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.show();
        }

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
            progressBar.dismiss();
        }
    }

    private final CompoundButton.OnCheckedChangeListener mOnToggleListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.action_shift) {
                isShiftToggle = isChecked;
                if (isChecked && mCtrlBtn.isChecked()) {
                    mCtrlBtn.setChecked(false);
                    isCtrlToggle = false;
                }
            } else if (buttonView.getId() == R.id.action_ctrl) {
                isCtrlToggle = isChecked;
                if (isChecked && mShiftBtn.isChecked()) {
                    mShiftBtn.setChecked(false);
                    isShiftToggle = false;
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
                startActivity(startIntent);
            }
        }
    };
}
