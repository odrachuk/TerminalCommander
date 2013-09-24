package com.drk.terminal.process;

import java.io.IOException;

public interface TerminalProcess {
    void startProcess() throws IOException;
    void execCommand(String command);
    void stopProcess();
}