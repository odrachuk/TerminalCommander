/*******************************************************************************
 * Created by o.drachuk on 10/01/2014.
 *
 * Copyright Oleksandr Drachuk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.softsandr.terminal.commander.command.filtered;

import android.content.res.Resources;
import android.widget.TextView;
import com.softsandr.utils.string.StringUtil;
import com.softsandr.terminal.R;

import java.util.LinkedList;

/**
 * The {@link java.util.LinkedList} extends for managing alignments of ls record data
 */
public class LsRowsList extends LinkedList<LsRowRecord> {
    private static final String SOME_SYMBOL_FOR_PROBE = "a";
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
            int permissionSub = permissionsToken.length() - maxPermissionsLength;
            int ownerSub = maxOwnerLength - ownerToken.length();
            int groupSub = maxGroupLength - groupToken.length();
            int sizeSub = maxSizeLength - sizeToken.length();
            int dateSub = maxDateLength - dateToken.length();
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
                    sb.append(resources.getString(R.string.whitespace));
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
        }
    }

    public String toString(TextView outTextView, int screenWidth, Resources resources) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            LsRowRecord rr = get(i);
            StringBuilder rowRecordText = new StringBuilder();
            rowRecordText.append(rr.getPermissionsToken())
                    .append(resources.getString(R.string.whitespace))
                    .append(rr.getOwnerToken())
                    .append(resources.getString(R.string.whitespace))
                    .append(rr.getGroupToken())
                    .append(resources.getString(R.string.whitespace))
                    .append(rr.getSizeToken())
                    .append(resources.getString(R.string.whitespace))
                    .append(rr.getDateToken())
                    .append(rr.getNameToken().trim());
            String rowRecordAfterCut = cutStringIfNeeds(outTextView, screenWidth, rowRecordText.toString());
            result.append(rowRecordAfterCut).append(i == (this.size() - 1)? "" : StringUtil.LINE_SEPARATOR);
        }
        return result.toString();
    }

    private String cutStringIfNeeds(TextView outTextView, int screenWidth, String text) {
        float textWidth = outTextView.getPaint().measureText(text);
        float someSymbolWidth = outTextView.getPaint().measureText(SOME_SYMBOL_FOR_PROBE);
        int countOfSymbolsPerScreen = (int) (screenWidth / someSymbolWidth);
        int countOfSymbolsInText = (int) (textWidth / someSymbolWidth);
        if (countOfSymbolsInText >= countOfSymbolsPerScreen) {
            StringBuilder resultText = new StringBuilder();
            resultText.append(text.substring(0, countOfSymbolsPerScreen - 3)).append("..");
            return resultText.toString();
        } else {
            return text;
        }
    }
}
