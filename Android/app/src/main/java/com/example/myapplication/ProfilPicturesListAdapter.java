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

import java.util.ArrayList;

public class ProfilPicturesListAdapter extends ArrayAdapter<Pictures>
    {
        private static final String TAG = "PicturesListAdapter";
        private static String filelien2;
        private Context mContext;
        private int mResource;
        private int lastPosition = -1;
        private ArrayList<Pictures> li;

        public ProfilPicturesListAdapter (Context context, int resource, ArrayList<Pictures> objects)
        {

            super(context, resource, objects);
            this.mContext = context;
            this.mResource = resource;
            this.li = objects;

        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            String image = getItem(position).getImage();
            String description = getItem(position).getDescription();



            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);

                convertView = inflater.inflate(mResource, parent, false);
            }

            Pictures pictures = new Pictures();
            pictures.setImage(image);
            pictures.setDescription(description);

            final View result;
            ViewHolder holder;
            PicturesListAdapter.viewHolder viewholder = (PicturesListAdapter.viewHolder) convertView.getTag();

            //com.example.myapplication.PicturesListAdapter.viewHolder viewholder = (com.example.myapplication.PicturesListAdapter.viewHolder) convertView.getTag();
            if(viewholder == null) {
                viewholder = new com.example.myapplication.PicturesListAdapter.viewHolder();
                viewholder.description = (TextView) convertView.findViewById(R.id.textView_description);
                viewholder.like = (TextView) convertView.findViewById(R.id.TextView_image);
                viewholder.com = (TextView) convertView.findViewById(R.id.TextView_comments);
                viewholder.localisation = (TextView) convertView.findViewById(R.id.TextView_localisation);
                viewholder.username = (TextView) convertView.findViewById(R.id.textView_username);
                viewholder.image = (ImageView) convertView.findViewById(R.id.ImagePictures);

                convertView.setTag(viewholder);
            }


            String strNew = this.li.get(position).getImage().toString().replace("\"", "");

            com.example.myapplication.PicturesListAdapter.filelien2 = "http://35.180.120.219:3000/uploads/" + strNew;


            viewholder.description.setText(this.li.get(position).getDescription() + "");
            viewholder.localisation.setText(this.li.get(position).getLocalisation() + "");

            if(this.li.get(position).getLike() == null)
                viewholder.username.setText("");
            else
                viewholder.username.setText("");

            if(this.li.get(position).getLike() == null)
                viewholder.like.setText("0");
            else
                viewholder.like.setText(this.li.get(position).getLike() + "");

            if(this.li.get(position).getComments() == null)
                viewholder.com.setText("0");
            else
                viewholder.com.setText(this.li.get(position).getComments() + "");


            Picasso.with(getContext()).load(com.example.myapplication.PicturesListAdapter.filelien2).into(viewholder.image);





            return convertView;
        }

        private class ViewHolder {
                TextView description;
                ImageView image;
                TextView like;
                TextView localisation;
                TextView username;
        }
    }
