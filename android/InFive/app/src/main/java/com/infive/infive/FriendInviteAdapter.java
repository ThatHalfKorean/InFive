package com.infive.infive;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by geoffkim on 5/1/15.
 */
public class FriendInviteAdapter extends ArrayAdapter<FriendInvite> {
    Context context;
    int layoutResourceId;
    FriendInvite[] data = null;

    public FriendInviteAdapter(Context context, int layoutResourceId, FriendInvite[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FriendInviteHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new FriendInviteHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
            holder.check = (CheckBox) row.findViewById(R.id.friendCheckBox);

            row.setTag(holder);

            holder.check.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    FriendInvite friend = data[position];
                    friend.setSelected(cb.isChecked());
                    friend.setSelected(cb.isChecked());
                }

            });
        } else {
            holder = (FriendInviteHolder) row.getTag();
        }

        FriendInvite circleForMessage = data[position];
        try {
            holder.txtTitle.setText(circleForMessage.friend);
        } catch (Exception e) {

        }


        return row;
    }

    static class FriendInviteHolder {
        TextView txtTitle;
        CheckBox check;
    }

}
