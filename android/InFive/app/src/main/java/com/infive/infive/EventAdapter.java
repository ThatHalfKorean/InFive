package com.infive.infive;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by geoffkim on 4/26/15.
 */
public class EventAdapter extends ArrayAdapter<Event>{
    Context context;
    int layoutResourceId;
    Event[] data = null;

    public EventAdapter(Context context, int layoutResourceId, Event[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
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

            row.setTag(holder);
        } else {
            holder = (FeedHolder) row.getTag();
        }

        Event feedItem = data[position];
        holder.txtTitle.setText(feedItem.msg.toString());

        return row;
    }

    static class FeedHolder {
        TextView txtTitle;
    }
}
