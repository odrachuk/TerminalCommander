package com.drk.terminal.ui.widget.listview.observer;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/17/13
 * Time: 9:08 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ListViewObservable {
    void registerObserver(ListViewObserver observer);
    void removeObserver(ListViewObserver observer);
    void notifyObservers(int selectedPosition);
}
