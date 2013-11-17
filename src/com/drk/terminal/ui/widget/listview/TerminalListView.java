package com.drk.terminal.ui.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.drk.terminal.ui.widget.listview.observer.ListViewObservable;
import com.drk.terminal.ui.widget.listview.observer.ListViewObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/11/13
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerminalListView extends ListView implements ListViewObservable {
    private List<ListViewObserver> observers;

    public TerminalListView(Context context) {
        this(context, null);
    }

    public TerminalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initList();
    }

    private void initList() {
        observers = new ArrayList<ListViewObserver>();
        setOnItemClickListener(makeItemClickListener());
    }

    private OnItemClickListener makeItemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                notifyObservers(position);
            }
        };
    }

    @Override
    public void registerObserver(ListViewObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ListViewObserver observer) {
        int i = observers.indexOf(observer);
        if (i >= 0) {
            observers.remove(i);
        }
    }

    @Override
    public void notifyObservers(int selectedPosition) {
        for (ListViewObserver o : observers) {
            o.onItemSelected(this, selectedPosition);
        }
    }
}
