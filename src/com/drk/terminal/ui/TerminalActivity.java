package com.drk.terminal.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;
import com.drk.terminal.R;

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
        final ListView listview = (ListView) findViewById(R.id.left_directory_list);
        final List<DirectoryContentInfo> valuesList = new ArrayList<DirectoryContentInfo>();
        valuesList.add(new DirectoryContentInfo("Android", "iPhone", "WindowsMobile"));
        valuesList.add(new DirectoryContentInfo("Blackberry", "WebOS", "Ubuntu"));
        valuesList.add(new DirectoryContentInfo("Windows7", "Max OS X", "Linux"));
        valuesList.add(new DirectoryContentInfo("OS/2", "Ubuntu", "Windows7"));
        valuesList.add(new DirectoryContentInfo("Max OS X", "Linux", "OS/2"));
        valuesList.add(new DirectoryContentInfo("Ubuntu", "Windows7", "Max OS X"));
        valuesList.add(new DirectoryContentInfo("Linux", "OS/2", "Android"));
        valuesList.add(new DirectoryContentInfo("Android", "iPhone", "WindowsMobile"));
        valuesList.add(new DirectoryContentInfo("Blackberry", "WebOS", "Ubuntu"));
        valuesList.add(new DirectoryContentInfo("OS/2", "Ubuntu", "Windows7"));
        valuesList.add(new DirectoryContentInfo("Max OS X", "Linux", "OS/2"));
        valuesList.add(new DirectoryContentInfo("Ubuntu", "Windows7", "Max OS X"));
        valuesList.add(new DirectoryContentInfo("Linux", "OS/2", "Android"));
        valuesList.add(new DirectoryContentInfo("Android", "iPhone", "WindowsMobile"));
        valuesList.add(new DirectoryContentInfo("Blackberry", "WebOS", "Ubuntu"));
        valuesList.add(new DirectoryContentInfo("OS/2", "Ubuntu", "Windows7"));
        valuesList.add(new DirectoryContentInfo("Max OS X", "Linux", "OS/2"));
        valuesList.add(new DirectoryContentInfo("Ubuntu", "Windows7", "Max OS X"));
        valuesList.add(new DirectoryContentInfo("Linux", "OS/2", "Android"));
        valuesList.add(new DirectoryContentInfo("Android", "iPhone", "WindowsMobile"));
        valuesList.add(new DirectoryContentInfo("Blackberry", "WebOS", "Ubuntu"));
        valuesList.add(new DirectoryContentInfo("OS/2", "Ubuntu", "Windows7"));
        valuesList.add(new DirectoryContentInfo("Max OS X", "Linux", "OS/2"));
        valuesList.add(new DirectoryContentInfo("Ubuntu", "Windows7", "Max OS X"));
        valuesList.add(new DirectoryContentInfo("Linux", "OS/2", "Android"));
        valuesList.add(new DirectoryContentInfo("Android", "iPhone", "WindowsMobile"));
        valuesList.add(new DirectoryContentInfo("Blackberry", "WebOS", "Ubuntu"));
        final DirectoryContentAdapter adapter = new DirectoryContentAdapter(this, valuesList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final DirectoryContentInfo item = (DirectoryContentInfo) parent.getItemAtPosition(position);
            }

        });
    }

    private void prepareRightList() {
        final ListView listview = (ListView) findViewById(R.id.right_directory_list);
        final List<DirectoryContentInfo> valuesList = new ArrayList<DirectoryContentInfo>();
        valuesList.add(new DirectoryContentInfo("Android", "iPhone", "WindowsMobile"));
        valuesList.add(new DirectoryContentInfo("Blackberry", "WebOS", "Ubuntu"));
        valuesList.add(new DirectoryContentInfo("Windows7", "Max OS X", "Linux"));
        valuesList.add(new DirectoryContentInfo("OS/2", "Ubuntu", "Windows7"));
        valuesList.add(new DirectoryContentInfo("Linux", "OS/2", "Android"));
        valuesList.add(new DirectoryContentInfo("Android", "iPhone", "WindowsMobile"));
        valuesList.add(new DirectoryContentInfo("Blackberry", "WebOS", "Ubuntu"));
        valuesList.add(new DirectoryContentInfo("Windows7", "Max OS X", "Linux"));
        valuesList.add(new DirectoryContentInfo("OS/2", "Ubuntu", "Windows7"));
        final DirectoryContentAdapter adapter = new DirectoryContentAdapter(this, valuesList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final DirectoryContentInfo item = (DirectoryContentInfo) parent.getItemAtPosition(position);
            }

        });
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

    private final CompoundButton.OnCheckedChangeListener mOnToggleListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == mShiftBtn.getId()) {
                isShiftToggle = isChecked;
                if (isChecked && mCtrlBtn.isChecked()) {
                    mCtrlBtn.setChecked(false);
                    isCtrlToggle = false;
                }
            } else if (buttonView.getId() == mCtrlBtn.getId()) {
                isCtrlToggle = isChecked;
                if (isChecked && mShiftBtn.isChecked()) {
                    mShiftBtn.setChecked(false);
                    isShiftToggle = false;
                }
            }
        }
    };
}
