package com.smakhorin.doodoo.reviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smakhorin.doodoo.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private List<Review> reviews = new ArrayList<>();

    public ReviewsAdapter() {}


    public void addReview(Review review) {
        reviews.add(review);
    }

    public void clearReviews() {
        reviews.clear();
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ReviewsViewHolder viewHolder = new ReviewsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int i) {
        if(reviews.size() > 0) {
            holder.bind(reviews.get(i).getRating(), reviews.get(i).getText(), reviews.get(i).getAuthor());
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        TextView mRating;
        TextView mText;
        TextView mAuthor;

        public ReviewsViewHolder(View itemView) {
            super(itemView);

            mRating = itemView.findViewById(R.id.tv_review_rating);
            mText = itemView.findViewById(R.id.tv_review_content);
            mAuthor = itemView.findViewById(R.id.tv_review_author);
        }

        void bind(String rating, String text, String author) {
            String ratingTemp = rating + "/5";
            mRating.setText(ratingTemp);
            mText.setText(text);
            mAuthor.setText(author);
        }

    }
}
