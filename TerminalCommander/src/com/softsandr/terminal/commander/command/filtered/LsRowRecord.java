package com.softsandr.terminal.commander.command.filtered;

/**
 * Date: 12/2/13
 *
 * @author Drachuk O.V.
 */
public class LsRowRecord {
    private String permissionsToken = "";
    private String ownerToken = "";
    private String groupToken = "";
    private String sizeToken = "";
    private String dateToken = "";
    private String nameToken = "";

    public String getPermissionsToken() {
        return permissionsToken;
    }

    public void setPermissionsToken(String permissionsToken) {
        this.permissionsToken = permissionsToken;
    }

    public String getOwnerToken() {
        return ownerToken;
    }

    public void setOwnerToken(String ownerToken) {
        this.ownerToken = ownerToken;
    }

    public String getGroupToken() {
        return groupToken;
    }

    public void setGroupToken(String groupToken) {
        this.groupToken = groupToken;
    }

    public String getSizeToken() {
        return sizeToken;
    }

    public void setSizeToken(String sizeToken) {
        this.sizeToken = sizeToken;
    }

    public String getDateToken() {
        return dateToken;
    }

    public void setDateToken(String dateToken) {
        this.dateToken = dateToken;
    }

    public String getNameToken() {
        return nameToken;
    }

    public void setNameToken(String nameToken) {
        this.nameToken = nameToken;
    }
}
