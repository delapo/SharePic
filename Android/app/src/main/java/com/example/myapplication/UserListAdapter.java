package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<User>
{
private static final String TAG="PicturesListAdapter";
private Context mContext;
private int mResource;
private int lastPosition=-1;

    public UserListAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    static class ViewHolder {
    TextView username;
}

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        UserListAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new UserListAdapter.ViewHolder();
            holder.username = (TextView) convertView.findViewById(R.id.search_profil_username);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = getItem(position);
        holder.username.setText(user.getUsername());

        return convertView;
    }
}