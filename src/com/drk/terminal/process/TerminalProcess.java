package com.drk.terminal.process;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

public interface TerminalProcess {
    void startProcess(ExecutorService processExecutor, String processDirectory) throws IOException;
    void execCommand(String command);
    void stopProcess();
}