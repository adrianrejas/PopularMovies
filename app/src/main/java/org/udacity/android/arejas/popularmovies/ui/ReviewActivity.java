package org.udacity.android.arejas.popularmovies.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.databinding.ActivityReviewBinding;

import java.util.Objects;

/**
 * Class representing an activity for showing a movie review
 */
public class ReviewActivity extends AppCompatActivity {

    public static final String EXTRA_REVIEW_MOVIE = "org.udacity.android.arejas.popularmovies.REVIEW_MOVIE";
    public static final String EXTRA_REVIEW_AUTHOR = "org.udacity.android.arejas.popularmovies.REVIEW_AUTHOR";
    public static final String EXTRA_REVIEW_TEXT = "org.udacity.android.arejas.popularmovies.REVIEW_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityReviewBinding uiBinding = DataBindingUtil.setContentView(this, R.layout.activity_review);

        // Get title of the movie reviewed and set activity title if existing
        String movieTitle = Objects.requireNonNull(getIntent().getExtras()).getString(EXTRA_REVIEW_MOVIE);
        if (movieTitle != null)
            setTitle(movieTitle);

        // Get author of the review and pass it to UI if existing
        String reviewAuthor = getIntent().getExtras().getString(EXTRA_REVIEW_AUTHOR);
        if (reviewAuthor != null) {
            uiBinding.setAuthor(reviewAuthor);
        }

        // Get content of the review and pass it to UI if existing
        String reviewText = getIntent().getExtras().getString(EXTRA_REVIEW_TEXT);
        if (reviewText != null) {
            uiBinding.setText(reviewText);
        }
    }
}
