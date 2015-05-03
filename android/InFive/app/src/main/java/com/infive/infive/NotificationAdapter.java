package com.infive.infive;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by geoffkim on 4/20/15.
 */
public class NotificationAdapter extends ArrayAdapter<Notification> {
    Context context;
    int layoutResourceId;
    Notification[] data = null;

    public NotificationAdapter(Context context, int layoutResourceId, Notification[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public String processDate(String date) {
        return date.substring(date.lastIndexOf("T"));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FeedHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new FeedHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
            holder.txtDate = (TextView) row.findViewById(R.id.txtDate);

            row.setTag(holder);
        } else {
            holder = (FeedHolder) row.getTag();
        }

        Notification feedItem = data[position];
        try {
            holder.txtTitle.setText(feedItem.msg.getString("author") + " is " + feedItem.msg.getString("content") + "!");
            holder.txtDate.setText(feedItem.msg.getString("creationDate"));
        } catch (Exception e) {

        }

        return row;
    }

    static class FeedHolder {
        TextView txtTitle;
        TextView txtDate;
    }
}

