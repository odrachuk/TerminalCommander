package com.drk.terminal.process;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.drk.terminal.console.TerminalInput;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TerminalInputProcess implements TerminalProcess {
    private static final String LOG_TAG = TerminalInputProcess.class.getSimpleName();
    public static final String SYSTEM_EXECUTOR_PATH = "sh";
    private Process mProcess;
    private OutputStream mStdIn;
    private InputStream mStdErr;
    private InputStream mStdOut;
    private TerminalInput mInput;
    private ExecutorService mExecutorService;

    /**
     * Create new input runtime process
     * @throws IOException if an I/O error happens when create new process {@link ProcessBuilder#start()}
     */
    public TerminalInputProcess(TerminalInput input) {
        mInput = input;
    }

    @Override
    public void startProcess() throws IOException {
        Log.d(LOG_TAG, "startProcess");
        ProcessBuilder builder = new ProcessBuilder(SYSTEM_EXECUTOR_PATH);
        builder.redirectErrorStream(true);
        try {
            mProcess = builder.start();
        } catch (IOException ex) {
            Log.d(LOG_TAG, "Exception when create console main process: " + ex.getMessage());
            throw ex;
        }
        mStdIn = mProcess.getOutputStream();
        mStdErr = mProcess.getErrorStream();
        mStdOut = mProcess.getInputStream();
        mExecutorService = Executors.newSingleThreadExecutor();
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(mStdErr);
                while (scanner.hasNext()) {
                    Log.e(LOG_TAG, scanner.next());
                }
            }
        });
    }

    @Override
    public void execCommand() {
        String input = "";
        for (String line : mInput.readLine()) {
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
                mInput.writeLine(s);
            }
            mInput.newLine();
        }
    }
}