package com.projectpinacolada.ucsd.projectpinacolada.ReadReviews;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import java.lang.String;

import com.projectpinacolada.ucsd.projectpinacolada.R;

import org.w3c.dom.Text;

/**
 * Created by Darren on 2/14/2016.
 */
public class ListViewAdapter extends BaseAdapter{
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    private List<Reviews> reviewsList = null;
    private ArrayList<Reviews> arraylist;

    public ListViewAdapter(Context context,
                           List<Reviews> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<Reviews>();
        this.arraylist.addAll(reviewsList);
        // imageLoader = new ImageLoader(context);
    }

    public class ViewHolder {
        TextView reviewers;
        TextView reviews;
        TextView reviewTitle;
        RatingBar ratingBar;
    }

    @Override
    public int getCount() {
        return reviewsList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.reviewers = (TextView) view.findViewById(R.id.reviewer);
            holder.reviews = (TextView) view.findViewById(R.id.review);
            holder.ratingBar = (RatingBar) view.findViewById((R.id.reviewRatingBar));
            holder.reviewTitle = (TextView) view.findViewById(R.id.reviewTitle);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.reviewers.setText(reviewsList.get(position).getReviewers());
        holder.reviews.setText(reviewsList.get(position).getReviews());
        holder.ratingBar.setRating((float) reviewsList.get(position).getReviewRating());
        holder.reviewTitle.setText(reviewsList.get(position).getReviewTitle());
        /*
        holder.population.setText(worldpopulationlist.get(position)
                .getPopulation());

        imageLoader.DisplayImage(worldpopulationlist.get(position).getFlag(),
            holder.flag); */
        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(context, SingleItemView.class);
                // Pass all data reviewers
                intent.putExtra("reviewers",
                        (reviewsList.get(position).getReviewers()));
                // Pass all data reviews
                intent.putExtra("reviews",
                        (reviewsList.get(position).getReviews()));
                intent.putExtra("reviewRating", reviewsList.get(position).getReviewRating());

                // Start SingleItemView Class
                context.startActivity(intent);
            }
        });
        return view;
    }
}
