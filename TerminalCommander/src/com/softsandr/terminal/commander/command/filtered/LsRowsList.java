package com.softsandr.terminal.commander.command.filtered;

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
        if (object.getPermissionsToken().length() > maxPermissionsLength) {
            maxPermissionsLength = object.getPermissionsToken().length();
        }
        if (object.getOwnerToken().length() > maxOwnerLength) {
            maxOwnerLength = object.getOwnerToken().length();
        }
        if (object.getGroupToken().length() > maxGroupLength) {
            maxGroupLength = object.getGroupToken().length();
        }
        if (object.getSizeToken().length() > maxSizeLength) {
            maxSizeLength = object.getSizeToken().length();
        }
        if (object.getDateToken().length() > maxDateLength) {
            maxDateLength = object.getDateToken().length();
        }
        if (object.getNameToken().length() > maxNameLength) {
            maxNameLength = object.getNameToken().length();
        }
        return super.add(object);
    }
}
