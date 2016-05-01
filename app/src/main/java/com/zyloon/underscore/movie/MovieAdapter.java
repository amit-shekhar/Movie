package com.zyloon.underscore.movie;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class MovieAdapter extends ArrayAdapter<Movie> {
    Activity activity;
    List<Movie> data;
    int layoutResourceId;
    List<Bitmap> imageBuffer;

    public MovieAdapter(Activity activity, int resource ,List<Movie> data) {
        super(activity, resource , data);
        this.activity = activity;
        this.data = data;
        this.layoutResourceId = resource;
        imageBuffer = new ArrayList<Bitmap>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieHolder holder = null;
        View row = convertView;

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(row == null){
            row = inflater.inflate(R.layout.movie_row,null ,false);
            holder = new MovieHolder();

            holder.posterImage = (ImageView)row.findViewById(R.id.imgIcon);
            holder.movieName = (TextView)row.findViewById(R.id.txtTitle);
            holder.releaseDate = (TextView)row.findViewById(R.id.release);
            holder.Rating = (TextView)row.findViewById(R.id.rating);
            holder.VoteCount = (TextView)row.findViewById(R.id.voteCount);

            row.setTag(holder);

        }else{
            holder = (MovieHolder)row.getTag();
        }

        Movie movie = (Movie)data.get(position);
        holder.movieName.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getReleaseDate());
        holder.Rating.setText(String.valueOf(movie.getVoteAverage()));
        holder.VoteCount.setText("(" + String.valueOf(movie.getVoteCount()) + "votes)");

        try {
           getBitmap(position,movie.getPosterPath(), holder.posterImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return row;
    }

    private void getBitmap(int position,final String addr, final ImageView imageView) throws IOException {

        if (imageBuffer.size() > position) {
            imageView.setImageBitmap(imageBuffer.get(position));
        } else {

            final Bitmap[] bmp = new Bitmap[1];
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        InputStream in = new URL(addr).openStream();
                        bmp[0] = BitmapFactory.decodeStream(in);
                        imageBuffer.add(bmp[0]);
                    } catch (Exception e) {
                        // log error
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    if (bmp[0] != null)
                        imageView.setImageBitmap(bmp[0]);
                }

            }.execute();
        }
    }

}
