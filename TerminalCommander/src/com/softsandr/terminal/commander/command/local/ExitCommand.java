package com.softsandr.terminal.commander.command.local;

import com.softsandr.terminal.commander.Commander;
import com.softsandr.terminal.commander.TerminalCommander;

import static com.drk.terminal.util.utils.StringUtil.EMPTY;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class ExitCommand implements LocalCommand {
    @Override
    public String isExecutable(TerminalCommander terminalCommander) {
        return EMPTY;
    }

    @Override
    public String onExecute(TerminalCommander terminalCommander) {
        if (terminalCommander.getUiController().getActivity() instanceof Commander) {
            ((Commander) terminalCommander.getUiController().getActivity()).exitActivity();
        }
        return LocalCommands.EXIT.getText();
    }
}
