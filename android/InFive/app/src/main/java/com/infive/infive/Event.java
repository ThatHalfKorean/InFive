package com.infive.infive;

import org.json.JSONObject;

/**
 * Created by geoffkim on 4/26/15.
 */
public class Event {
    public JSONObject msg;

    public Event() {
        super();
    }

    public Event(JSONObject msg) {
        super();
        this.msg = msg;
    }
}
