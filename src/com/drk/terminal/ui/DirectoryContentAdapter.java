package com.drk.terminal.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.drk.terminal.R;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/16/13
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryContentAdapter extends ArrayAdapter<DirectoryContentInfo> {
    private final Activity context;
    private final List<DirectoryContentInfo> filesInfo;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.terminal_list_row_layout, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.fileNameView = (TextView) rowView.findViewById(R.id.file_name);
            viewHolder.fileSizeView = (TextView) rowView.findViewById(R.id.file_size);
            viewHolder.fileModifyTimeView = (TextView) rowView.findViewById(R.id.file_modify_time);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        DirectoryContentInfo info = filesInfo.get(position);
        holder.fileNameView.setText(info.getFileName());
        holder.fileSizeView.setText(info.getFileSize());
        holder.fileModifyTimeView.setText(info.getFileModifyTime());

        return rowView;
    }
}
