package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentsListAdapter extends ArrayAdapter<Comment> {
    private static final String TAG = "PicturesListAdapter";
    private Context mContext;
    private int mResource;

    public CommentsListAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    static class ViewHolder {
        TextView comment;
        TextView username;
        ImageView image;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CommentsListAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new CommentsListAdapter.ViewHolder();
            holder.comment = (TextView) convertView.findViewById(R.id.comcom);
            holder.username = (TextView) convertView.findViewById(R.id.username_comments);
            holder.image = (ImageView) convertView.findViewById(R.id.comments_profil_picture);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Comment comment = getItem(position);


        if(comment.getComments() != null && holder.comment != null)
            holder.comment.setText(comment.getComments() + "");

        if(comment.getUsername() != null && holder.username != null)
        holder.username.setText(comment.getUsername() + "");

        if(comment.getImage() != null && holder.image != null)
            Picasso.with(getContext()).load(comment.getImage()).into(holder.image);



        //holder.comment.setText(comment.getUsername());

        return convertView;
    }
}
