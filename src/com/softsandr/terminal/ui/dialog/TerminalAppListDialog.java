package com.softsandr.terminal.ui.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.softsandr.terminal.R;

import java.util.List;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class TerminalAppListDialog extends DialogFragment {
    private static final String LOG_TAG = TerminalAppListDialog.class.getSimpleName();
    private static final String FILE_NAME = LOG_TAG + ".ACTIVE_PAGE";
    private final AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            getDialog().cancel();
        }
    };
    private String mFileName;

    /**
     * Create a new instance of TerminalHistoryDialog, providing {@link com.softsandr.terminal.ui.activity.terminal.TerminalActivity.ActivePage}
     * as argument.
     */
    static TerminalAppListDialog newInstance(String fileName) {
        TerminalAppListDialog f = new TerminalAppListDialog();
        // Supply arguments
        Bundle args = new Bundle();
        args.putString(FILE_NAME, fileName);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileName = getArguments().getString(FILE_NAME);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.terminal_app_dialog_layout, container, false);
        ListView appListView = (ListView) v.findViewById(R.id.app_dialog_list);
        String fileExtension = parseFileExtension();
        PackageManager packageManager = getActivity().getPackageManager();
        Intent searchIntent = new Intent(Intent.ACTION_VIEW);
        searchIntent.setType("application/" + fileExtension);
        List<ResolveInfo> knownAppList = packageManager.queryIntentActivities(searchIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (!knownAppList.isEmpty()) {
            ArrayAdapter<ResolveInfo> listAdapter = new ResolveInfoAdapter(getActivity(), knownAppList);
            appListView.setAdapter(listAdapter);
            appListView.setOnItemClickListener(mItemClickListener);
        }
        return v;
    }

    private String parseFileExtension() {
        int dotIndex = mFileName.lastIndexOf(".");
        return mFileName.substring(dotIndex + 1, mFileName.length());
    }

    private final class ResolveInfoAdapter extends ArrayAdapter<ResolveInfo> {
        private final List<ResolveInfo> mAppList;
        private final Activity mActivity;

        public ResolveInfoAdapter(Activity activity, List<ResolveInfo> appList) {
            super(activity, R.layout.terminal_app_list_row_layout, appList);
            mActivity = activity;
            mAppList = appList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = mActivity.getLayoutInflater();
                convertView = inflater.inflate(R.layout.terminal_app_list_row_layout, parent, false);
            }
            ResolveInfo item = mAppList.get(position);
            TextView textView = (TextView) convertView.findViewById(R.id.terminal_app_list_text);
            textView.setText(item.loadLabel(mActivity.getPackageManager()));
            return convertView;
        }
    }
}
