package com.drk.terminal.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.drk.terminal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/16/13
 * Time: 10:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_activity_layout);
        initViews();
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
        valuesList.add(new DirectoryContentInfo("Windows7", "Max OS X", "Linux"));
        valuesList.add(new DirectoryContentInfo("OS/2", "Ubuntu", "Windows7"));
        valuesList.add(new DirectoryContentInfo("Max OS X", "Linux", "OS/2"));
        valuesList.add(new DirectoryContentInfo("Ubuntu", "Windows7", "Max OS X"));
        valuesList.add(new DirectoryContentInfo("Linux", "OS/2", "Android"));
        valuesList.add(new DirectoryContentInfo("Android", "iPhone", "WindowsMobile"));
        valuesList.add(new DirectoryContentInfo("Blackberry", "WebOS", "Ubuntu"));
        valuesList.add(new DirectoryContentInfo("Windows7", "Max OS X", "Linux"));
        valuesList.add(new DirectoryContentInfo("OS/2", "Ubuntu", "Windows7"));
        valuesList.add(new DirectoryContentInfo("Max OS X", "Linux", "OS/2"));
        valuesList.add(new DirectoryContentInfo("Ubuntu", "Windows7", "Max OS X"));
        valuesList.add(new DirectoryContentInfo("Linux", "OS/2", "Android"));
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
        valuesList.add(new DirectoryContentInfo("Max OS X", "Linux", "OS/2"));
        valuesList.add(new DirectoryContentInfo("Ubuntu", "Windows7", "Max OS X"));
        valuesList.add(new DirectoryContentInfo("Linux", "OS/2", "Android"));
        valuesList.add(new DirectoryContentInfo("Android", "iPhone", "WindowsMobile"));
        valuesList.add(new DirectoryContentInfo("Blackberry", "WebOS", "Ubuntu"));
        valuesList.add(new DirectoryContentInfo("Windows7", "Max OS X", "Linux"));
        valuesList.add(new DirectoryContentInfo("OS/2", "Ubuntu", "Windows7"));
        valuesList.add(new DirectoryContentInfo("Max OS X", "Linux", "OS/2"));
        valuesList.add(new DirectoryContentInfo("Ubuntu", "Windows7", "Max OS X"));
        valuesList.add(new DirectoryContentInfo("Linux", "OS/2", "Android"));
        valuesList.add(new DirectoryContentInfo("Android", "iPhone", "WindowsMobile"));
        valuesList.add(new DirectoryContentInfo("Blackberry", "WebOS", "Ubuntu"));
        valuesList.add(new DirectoryContentInfo("Windows7", "Max OS X", "Linux"));
        valuesList.add(new DirectoryContentInfo("OS/2", "Ubuntu", "Windows7"));
        valuesList.add(new DirectoryContentInfo("Max OS X", "Linux", "OS/2"));
        valuesList.add(new DirectoryContentInfo("Ubuntu", "Windows7", "Max OS X"));
        valuesList.add(new DirectoryContentInfo("Linux", "OS/2", "Android"));
        valuesList.add(new DirectoryContentInfo("Android", "iPhone", "WindowsMobile"));
        valuesList.add(new DirectoryContentInfo("Blackberry", "WebOS", "Ubuntu"));
        valuesList.add(new DirectoryContentInfo("Windows7", "Max OS X", "Linux"));
        valuesList.add(new DirectoryContentInfo("OS/2", "Ubuntu", "Windows7"));
        valuesList.add(new DirectoryContentInfo("Max OS X", "Linux", "OS/2"));
        valuesList.add(new DirectoryContentInfo("Ubuntu", "Windows7", "Max OS X"));
        valuesList.add(new DirectoryContentInfo("Linux", "OS/2", "Android"));
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
}
