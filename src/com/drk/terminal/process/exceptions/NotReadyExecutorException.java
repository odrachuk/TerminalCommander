package com.drk.terminal.process.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/28/13
 * Time: 8:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class NotReadyExecutorException extends Exception {

    public NotReadyExecutorException() {
    }

    public NotReadyExecutorException(String detailMessage) {
        super(detailMessage);
    }

    public NotReadyExecutorException(Throwable throwable) {
        super(throwable);
    }
}
