package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.util.ArrayList;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PicturesListAdapter extends ArrayAdapter<Pictures>
{
    private static final String TAG = "PicturesListAdapter";
    public static String filelien2;
    private Context mContext;
    private int mResource;
    private ArrayList<Pictures> li;
    private static UserApi userApi;




    public PicturesListAdapter(Context context, int resource, ArrayList<Pictures> objects)
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
        RecyclerView.ViewHolder holder;
        viewHolder viewholder = (viewHolder) convertView.getTag();
        if(viewholder == null) {
            viewholder = new PicturesListAdapter.viewHolder();
            viewholder.description = (TextView) convertView.findViewById(R.id.textView_description);
            viewholder.like = (TextView) convertView.findViewById(R.id.TextView_image);
            viewholder.com = (TextView) convertView.findViewById(R.id.TextView_comments);
            viewholder.localisation = (TextView) convertView.findViewById(R.id.TextView_localisation);
            viewholder.username = (TextView) convertView.findViewById(R.id.textView_username);
            viewholder.image = (ImageView) convertView.findViewById(R.id.ImagePictures);

            convertView.setTag(viewholder);
        }


        String strNew = this.li.get(position).getImage().toString().replace("\"", "");

        PicturesListAdapter.filelien2 = "http://35.180.120.219:3000/uploads/" + strNew;


        viewholder.description.setText(this.li.get(position).getDescription() + "");
        viewholder.localisation.setText(this.li.get(position).getLocalisation() + "");

        if(this.li.get(position).getLike() == null)
            viewholder.username.setText("User");
        else
        viewholder.username.setText(this.li.get(position).getUsername() + "");

        if(this.li.get(position).getLike() == null)
            viewholder.like.setText("0");
        else
            viewholder.like.setText(this.li.get(position).getLike() + "");

        if(this.li.get(position).getComments() == null)
            viewholder.com.setText("0");
        else
            viewholder.com.setText(this.li.get(position).getComments() + "");


        Picasso.with(getContext()).load(PicturesListAdapter.filelien2).into(viewholder.image);





        return convertView;
    }

    static class viewHolder{
        TextView description;
        ImageView image;
        TextView like;
        TextView localisation;
        TextView username;
        TextView com;

    }

}