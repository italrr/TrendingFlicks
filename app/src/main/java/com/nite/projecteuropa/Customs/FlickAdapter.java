package com.nite.projecteuropa.Customs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nite.projecteuropa.DTO.Flick;
import com.nite.projecteuropa.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FlickAdapter extends ArrayAdapter<Flick> implements View.OnClickListener{

    private ArrayList<Flick> dataSet;
    private Context mContext;
    private Activity activity;

    // View lookup cache
    private static class ViewHolder {
        TextView txtTitle;
        TextView txtReleased;
        TextView txtSecondLine;
        TextView txtDescription;
        TextView txtActors;
        TextView txtRatings;
        TextView txtType;
        ImageView thumbnail;
    }

    public FlickAdapter(ArrayList<Flick> data, Context context, Activity activity) {
        super(context, R.layout.flick_list, data);
        this.dataSet = data;
        this.mContext=context;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Flick dataModel=(Flick)object;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Flick dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.flick_list, parent, false);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.txtReleased = (TextView) convertView.findViewById(R.id.released);
            viewHolder.txtSecondLine = (TextView) convertView.findViewById(R.id.secondline);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.description);
            viewHolder.txtActors = (TextView) convertView.findViewById(R.id.actors);
            viewHolder.txtRatings = (TextView) convertView.findViewById(R.id.ratings);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.flick_img);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtTitle.setText(dataModel.getTitle());
        viewHolder.txtReleased.setText(""+dataModel.getReleased());
        viewHolder.txtSecondLine.setText(dataModel.getRuntime()+" | "+dataModel.getGenre());
        viewHolder.txtDescription.setText(dataModel.getPlot());
        viewHolder.txtType.setText(dataModel.getType());
        viewHolder.txtActors.setText("Actor(s): "+dataModel.getActors());
        viewHolder.txtRatings.setText("IMDb "+dataModel.getImdbRating()+"/10  |  Rotten Tomatoes "+dataModel.getRottenTomatoes()+" | Metascore "+dataModel.getMetascore()+"/100");
        if(dataModel.getPoster() != "") {
            Picasso.with(mContext).load(dataModel.getPoster()).into(viewHolder.thumbnail);
        }

        return convertView;
    }
}
