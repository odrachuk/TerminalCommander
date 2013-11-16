package com.drk.terminal.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.drk.terminal.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/16/13
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryContentAdapter extends ArrayAdapter<DirectoryContentInfo> {
    private final List<DirectoryContentInfo> filesInfo;
    private Map<Integer, View> cache;
    private final Activity context;
    private boolean inFirst = true;

    static class ViewHolder {
        public TextView fileNameView;
        public TextView fileSizeView;
        public TextView fileModifyTimeView;
    }

    public DirectoryContentAdapter(Activity context, List<DirectoryContentInfo> filesInfo) {
        super(context, R.layout.terminal_list_row_layout, filesInfo);
        this.context = context;
        this.filesInfo = filesInfo;
    }

    private void initCache(ViewGroup parent) {
        cache = new HashMap<Integer, View>(getCount());
        for (int i = 0; i < getCount(); i++) {
            View rowView = context.getLayoutInflater().inflate(R.layout.terminal_list_row_layout, parent, false);
            TextView fileNameView = (TextView) rowView.findViewById(R.id.file_name);
            TextView fileSizeView = (TextView) rowView.findViewById(R.id.file_size);
            TextView fileModifyTimeView = (TextView) rowView.findViewById(R.id.file_modify_time);
            DirectoryContentInfo info = filesInfo.get(i);
            fileNameView.setText(info.getFileName());
            fileSizeView.setText(info.getFileSize());
            fileModifyTimeView.setText(info.getFileModifyTime());
            if (!info.isDirectory()) {
                fileNameView.setTextColor(getContext().getResources().getColor(R.color.COLOR_b2b2b2));
                fileSizeView.setTextColor(getContext().getResources().getColor(R.color.COLOR_b2b2b2));
                fileModifyTimeView.setTextColor(getContext().getResources().getColor(R.color.COLOR_b2b2b2));
            }
            cache.put(i, rowView);
        }
        inFirst = false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inFirst) {
            initCache(parent);
        }
//        View rowView = convertView;
//        if (rowView == null) {
//            LayoutInflater inflater = context.getLayoutInflater();
//            rowView = inflater.inflate(R.layout.terminal_list_row_layout, parent, false);
//            ViewHolder viewHolder = new ViewHolder();
//            viewHolder.fileNameView = (TextView) rowView.findViewById(R.id.file_name);
//            viewHolder.fileSizeView = (TextView) rowView.findViewById(R.id.file_size);
//            viewHolder.fileModifyTimeView = (TextView) rowView.findViewById(R.id.file_modify_time);
//            DirectoryContentInfo info = filesInfo.get(position);
//            viewHolder.fileNameView.setText(info.getFileName());
//            viewHolder.fileSizeView.setText(info.getFileSize());
//            viewHolder.fileModifyTimeView.setText(info.getFileModifyTime());
//            rowView.setTag(viewHolder);
//        }
////        ViewHolder holder = (ViewHolder) rowView.getTag();
////        DirectoryContentInfo info = filesInfo.get(position);
////        holder.fileNameView.setText(info.getFileName());
////        holder.fileSizeView.setText(info.getFileSize());
////        holder.fileModifyTimeView.setText(info.getFileModifyTime());
        return cache.get(position);
    }
}
