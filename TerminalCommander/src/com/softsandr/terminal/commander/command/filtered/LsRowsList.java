package com.softsandr.terminal.commander.command.filtered;

import android.content.res.Resources;
import com.drk.terminal.util.utils.StringUtil;
import com.softsandr.terminal.R;

import java.util.LinkedList;

/**
 * Date: 12/2/13
 *
 * @author Drachuk O.V.
 */
public class LsRowsList extends LinkedList<LsRowRecord> {
    private int maxPermissionsLength;
    private int maxOwnerLength;
    private int maxGroupLength;
    private int maxSizeLength;
    private int maxDateLength;
    private int maxNameLength;

    @Override
    public boolean add(LsRowRecord object) {
        if (object.getPermissionsToken() != null && object.getPermissionsToken().length() > maxPermissionsLength) {
            maxPermissionsLength = object.getPermissionsToken().length();
        }
        if (object.getOwnerToken() != null && object.getOwnerToken().length() > maxOwnerLength) {
            maxOwnerLength = object.getOwnerToken().length();
        }
        if (object.getGroupToken() != null && object.getGroupToken().length() > maxGroupLength) {
            maxGroupLength = object.getGroupToken().length();
        }
        if (object.getSizeToken() != null && object.getSizeToken().length() > maxSizeLength) {
            maxSizeLength = object.getSizeToken().length();
        }
        if (object.getDateToken() != null && object.getDateToken().length() > maxDateLength) {
            maxDateLength = object.getDateToken().length();
        }
        if (object.getNameToken() != null && object.getNameToken().length() > maxNameLength) {
            maxNameLength = object.getNameToken().length();
        }
        return super.add(object);
    }

    public void alignTokens(Resources resources) {
        for (LsRowRecord rr : this) {
            String permissionsToken = rr.getPermissionsToken();
            String ownerToken = rr.getOwnerToken();
            String groupToken = rr.getGroupToken();
            String sizeToken = rr.getSizeToken();
            String dateToken = rr.getDateToken();
            String nameToken = rr.getNameToken();
            int permissionSub = permissionsToken.length() - maxPermissionsLength;
            int ownerSub = maxOwnerLength - ownerToken.length();
            int groupSub = maxGroupLength - groupToken.length();
            int sizeSub = maxSizeLength - sizeToken.length();
            int dateSub = maxDateLength - dateToken.length();
            int nameSub = maxNameLength - nameToken.length();

            if (permissionSub > 0) {
                StringBuilder sb = new StringBuilder(permissionsToken);
                for (int i = 0; i < permissionSub; i++) {
                    sb.append(resources.getString(R.string.whitespace));
                }
                rr.setPermissionsToken(sb.toString());
            }
            if (ownerSub > 0) {
                StringBuilder sb = new StringBuilder(ownerToken);
                for (int i = 0; i < ownerSub; i++) {
                    sb.append(resources.getString(R.string.whitespace));
                }
                rr.setOwnerToken(sb.toString());
            }
            if (groupSub > 0) {
                StringBuilder sb = new StringBuilder(groupToken);
                for (int i = 0; i < groupSub; i++) {
                    sb.append(resources.getString(R.string.whitespace));
                }
                rr.setGroupToken(sb.toString());
            }
            if (sizeSub > 0) {
                StringBuilder sb = new StringBuilder(sizeToken);
                for (int i = 0; i < sizeSub; i++) {
                    sb.append("0");
                }
                rr.setSizeToken(sb.toString());
            }
            if (dateSub > 0) {
                StringBuilder sb = new StringBuilder(dateToken);
                for (int i = 0; i < dateSub; i++) {
                    sb.append(resources.getString(R.string.whitespace));
                }
                rr.setDateToken(sb.toString());
            }
            if (nameSub > 0) {
                StringBuilder sb = new StringBuilder(nameToken);
                for (int i = 0; i < nameSub; i++) {
                    sb.append(" ");
                }
                rr.setNameToken(sb.toString());
            }
        }
    }

    public String toString(Resources resources) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            LsRowRecord rr = get(i);
            result.append(rr.getPermissionsToken()).append(resources.getString(R.string.tabulate))
                    .append(resources.getString(R.string.tabulate)).append(resources.getString(R.string.tabulate))
                    .append(resources.getString(R.string.tabulate)).append(resources.getString(R.string.tabulate))
                    .append(rr.getOwnerToken()).append(resources.getString(R.string.tabulate)).append(resources.getString(R.string.tabulate))
                    .append(rr.getGroupToken()).append(resources.getString(R.string.tabulate)).append(resources.getString(R.string.tabulate))
                    .append(rr.getSizeToken()).append(resources.getString(R.string.tabulate)).append(resources.getString(R.string.tabulate))
                    .append(rr.getDateToken()).append(resources.getString(R.string.tabulate)).append(resources.getString(R.string.tabulate))
                    .append(rr.getNameToken().trim())
                    .append(i == (this.size() - 1)? "" : StringUtil.LINE_SEPARATOR);
        }
        return result.toString();
    }
}
