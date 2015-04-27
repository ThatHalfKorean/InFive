package com.infive.infive;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by geoffkim on 4/20/15.
 */
public class Notification {

        public JSONObject msg;
        public Notification(){
            super();
        }

        public Notification(JSONObject msg) {
            super();
            this.msg = msg;
        }
}
