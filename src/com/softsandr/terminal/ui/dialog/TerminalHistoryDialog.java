package com.softsandr.terminal.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.softsandr.terminal.R;
import com.softsandr.terminal.ui.activity.terminal.TerminalActivity;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class TerminalHistoryDialog extends DialogFragment {
    private static final String LOG_TAG = TerminalHistoryDialog.class.getSimpleName();
    private static final String ACTIVE_PAGE = LOG_TAG + ".ACTIVE_PAGE";
    private static final String HISTORY_LOCATIONS = LOG_TAG + ".HISTORY_LOCATIONS";
    private TerminalActivity.ActivePage mActivePage;
    private String[] mHistoryLocations;
    private final AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String selectedPath = mHistoryLocations[position];
            if (mActivePage.equals(TerminalActivity.ActivePage.LEFT)) {
                ((TerminalActivity) getActivity()).getLeftListAdapter().changeDirectory(selectedPath);
            } else {
                ((TerminalActivity) getActivity()).getRightListAdapter().changeDirectory(selectedPath);
            }
            getDialog().cancel();
        }
    };

    /**
     * Create a new instance of TerminalHistoryDialog, providing {@link TerminalActivity.ActivePage}
     * as argument.
     */
    static TerminalHistoryDialog newInstance(TerminalActivity.ActivePage activePage, String[] historyLocations) {
        TerminalHistoryDialog f = new TerminalHistoryDialog();
        // Supply arguments
        Bundle args = new Bundle();
        args.putSerializable(ACTIVE_PAGE, activePage);
        args.putStringArray(HISTORY_LOCATIONS, historyLocations);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivePage = (TerminalActivity.ActivePage) getArguments().getSerializable(ACTIVE_PAGE);
        mHistoryLocations = getArguments().getStringArray(HISTORY_LOCATIONS);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.terminal_history_dialog_layout, container, false);
        TextView title = (TextView) v.findViewById(R.id.terminal_history_dialog_title);
        title.setText(mActivePage.equals(TerminalActivity.ActivePage.LEFT)?
            "Left panel history" : "Right panel history");
        ListView list = (ListView) v.findViewById(R.id.history_dialog_list);
        ArrayAdapter listAdapter = new ArrayAdapter(getActivity(), R.layout.terminal_history_list_row_layout,
                mHistoryLocations);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(mItemClickListener);
        return v;
    }
}
