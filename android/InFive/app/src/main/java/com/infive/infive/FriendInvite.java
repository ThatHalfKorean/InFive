package com.infive.infive;

import org.json.JSONObject;

/**
 * Created by geoffkim on 5/1/15.
 */
public class FriendInvite {
    public String friend;
    public boolean selected;

    public FriendInvite() {
        super();
    }

    public FriendInvite(String friend, boolean selected) {
        super();
        this.friend = friend;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
