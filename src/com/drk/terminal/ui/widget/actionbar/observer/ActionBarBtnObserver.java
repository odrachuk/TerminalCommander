package com.drk.terminal.ui.widget.actionbar.observer;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/17/13
 * Time: 9:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ActionBarBtnObserver {
    void onShift();
    void offShift();
    void onCtrl();
    void offCtrl();
}
