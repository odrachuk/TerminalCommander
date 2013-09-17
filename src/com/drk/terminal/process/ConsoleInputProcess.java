package com.drk.terminal.process;

import android.os.AsyncTask;
import android.util.Log;
import com.drk.terminal.console.ConsoleInput;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class ConsoleInputProcess implements ConsoleProcess {
    public static final String LOG_TAG = ConsoleInputProcess.class.getSimpleName();
    public static final String RUNTIME_EXECUTOR = "/bin/bash";
    private Process mProcess;
    private OutputStream mStdIn;
    private InputStream mStdErr;
    private InputStream mStdOut;
    private ConsoleInput mConsole;

    /**
     * Create new input runtime process
     * @throws IOException if an I/O error happens when create new process {@link ProcessBuilder#start()}
     */
    public ConsoleInputProcess(ConsoleInput input) throws IOException {
        mConsole = input;
        createProcess();
    }

    private void createProcess() throws IOException {
        ProcessBuilder builder = new ProcessBuilder("sh");
        builder.redirectErrorStream(true);
        try {
            mProcess = builder.start();
        } catch (IOException ex) {
            Log.d(LOG_TAG, "Exception when create console main process: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void startProcess() {
        Log.d(LOG_TAG, "startProcess");
        mStdIn = mProcess.getOutputStream();
        mStdErr = mProcess.getErrorStream();
        mStdOut = mProcess.getInputStream();
    }

    @Override
    public void execCommand() {
        String input = "";
        for (String line : mConsole.readLine()) {
            input += line;
        }
        new CommandAsynchronouslyExecutor().execute(input);
    }

    @Override
    public void stopProcess() {
        Log.d(LOG_TAG, "stopProcess");
        mProcess.destroy();
    }

    private class CommandAsynchronouslyExecutor extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
            List<String> resultStrings = new LinkedList<String>();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(mStdOut));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mStdIn));
                String input = params[0];
                Log.d(LOG_TAG, "execCommand -> " + input);
                if (!input.isEmpty()) {
                    if (input.trim().equals("exit")) {
                        writer.write("exit\n");
                    } else {
                        // write command to process
                        writer.write("((" + input + ") && echo --EOF--) || echo --EOF--\n");
                    }
                    writer.flush();
                    // read result of command from process
                    String out = reader.readLine();
                    while (out != null && !out.trim().equals("--EOF--")) {
                        // write output to list
                        resultStrings.add(out);
                        out = reader.readLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultStrings;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            // write result to console
            for (String s : strings) {
                mConsole.writeLine(s);
            }
            mConsole.newLine();
        }
    }
}