package com.projectpinacolada.ucsd.projectpinacolada.ReadReviews;

/**
 * Created by Darren on 2/14/2016.
 * Getter/ setter class
 */
public class Reviews {
    private String review;
    private String reviewers;
    private double reviewRating;
    private String reviewTitle;

    public String getReviews(){
        return review;
    }

    public void setReviews(String review) {
        this.review = review;
    }

    public String getReviewers(){
        return reviewers;
    }

    public void setReviewers(String reviewers){
        this.reviewers = reviewers;
    }

    public void setReviewTitle(String reviewTitle){this.reviewTitle = reviewTitle;}

    public String getReviewTitle(){return reviewTitle;}

    // Get Set for reviewRating
    public double getReviewRating () {return reviewRating;}
    public void setReviewRating (double myReviewRating) {this.reviewRating = myReviewRating;}
}
