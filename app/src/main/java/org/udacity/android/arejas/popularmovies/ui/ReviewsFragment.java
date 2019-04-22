package org.udacity.android.arejas.popularmovies.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.data.entities.MovieReviewItem;
import org.udacity.android.arejas.popularmovies.databinding.FragmentCreditBinding;
import org.udacity.android.arejas.popularmovies.gateways.ui.MovieViewModel;
import org.udacity.android.arejas.popularmovies.gateways.ui.factories.ViewModelFactory;
import org.udacity.android.arejas.popularmovies.ui.adapters.MovieReviewsListAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class ReviewsFragment extends Fragment
        implements MovieReviewsListAdapter.OnMovieReviewItemClickListener{

    private FragmentCreditBinding uiBinding;

    private MovieReviewsListAdapter mAdapter;

    private MovieViewModel movieViewModel;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Get the movie activity view model and observe the changes in the reviews
        movieViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), viewModelFactory).get(MovieViewModel.class);
        movieViewModel.getMovieReviews().observe(this, movieReviewsResource -> {
            if (movieReviewsResource != null) {
                // According to the state of the data, show different layouts
                switch (movieReviewsResource.getStatus()) {
                    case ERROR:
                        showError(movieReviewsResource.getError());
                        break;
                    case LOADING:
                        showLoading();
                        break;
                    case SUCCESS:
                        updateList(movieReviewsResource.getData());
                        break;
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        uiBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_credit, container, false);

        /* Get number of items in a row*/
        int iElementsPerRow = getResources().getInteger(R.integer.movie_list_items_per_row);

        // Configure recycler view with a grid layout
        LinearLayoutManager llm_linear = new LinearLayoutManager(getContext());
        uiBinding.rvCreditsList.setLayoutManager(llm_linear);

        // Configure adapter for recycler view
        mAdapter = new MovieReviewsListAdapter(null, this);
        uiBinding.rvCreditsList.setAdapter(mAdapter);

        return uiBinding.getRoot();
    }

    /**
     * Update UI with new data received in the form of list
     * @param data
     */
    private void updateList(List<MovieReviewItem> data) {
        if (uiBinding != null) {
            if ((data != null) && (!data.isEmpty())) {
                uiBinding.rvCreditsList.setVisibility(View.VISIBLE);
                uiBinding.creditsLoadingLayout.loadingLayout.setVisibility(View.GONE);
                uiBinding.creditsErrorLayout.errorLayout.setVisibility(View.GONE);
                uiBinding.creditsNoElementsLayout.noElementsLayout.setVisibility(View.GONE);
                mAdapter.setDataList(data);
                mAdapter.notifyDataSetChanged();
            }
            else {
                uiBinding.rvCreditsList.setVisibility(View.GONE);
                uiBinding.creditsLoadingLayout.loadingLayout.setVisibility(View.GONE);
                uiBinding.creditsErrorLayout.errorLayout.setVisibility(View.GONE);
                uiBinding.creditsNoElementsLayout.noElementsLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Show an error on the activity. The idea has been to escalate errors to the UI classes by the
     * exception mechanisms and let these classes decide how to show the error.
     *
     * @param error Exception with the error appeared.
     */
    private void showError(Throwable error) {
        if (uiBinding != null) {
            uiBinding.rvCreditsList.setVisibility(View.GONE);
            uiBinding.creditsLoadingLayout.loadingLayout.setVisibility(View.GONE);
            uiBinding.creditsErrorLayout.errorLayout.setVisibility(View.VISIBLE);
            uiBinding.creditsNoElementsLayout.noElementsLayout.setVisibility(View.GONE);
            if (uiBinding.creditsErrorLayout.tvError != null) {
                if (error instanceof NullPointerException)
                    uiBinding.creditsErrorLayout.tvError.setText(getString(R.string.error_data));
                if (error instanceof IOException)
                    uiBinding.creditsErrorLayout.tvError.setText(getString(R.string.error_connection));
                else
                    uiBinding.creditsErrorLayout.tvError.setText(getString(R.string.error_ui));
            }
        }
    }

    /**
     * Show the fragment info is loading.
     */
    private void showLoading() {
        if (uiBinding != null) {
            uiBinding.rvCreditsList.setVisibility(View.GONE);
            uiBinding.creditsLoadingLayout.loadingLayout.setVisibility(View.VISIBLE);
            uiBinding.creditsErrorLayout.errorLayout.setVisibility(View.GONE);
            uiBinding.creditsNoElementsLayout.noElementsLayout.setVisibility(View.GONE);
        }
    }

    /**
     * If clicked a review, open a new activity showing the content of the review.
     *
     * @param item
     */
    @Override
    public void onClick(MovieReviewItem item) {
        if (item != null) {
            Intent reviewIntent = new Intent(getContext(),
                    ReviewActivity.class);
            if ((movieViewModel.getMovieDetails().getValue() != null) &&
                    (movieViewModel.getMovieDetails().getValue().getData() != null) &&
                    (movieViewModel.getMovieDetails().getValue().getData() != null)) {
                reviewIntent.putExtra(ReviewActivity.EXTRA_REVIEW_MOVIE,
                        movieViewModel.getMovieDetails().getValue().getData().getTitle());
            }
            reviewIntent.putExtra(ReviewActivity.EXTRA_REVIEW_AUTHOR, item.getAuthor());
            reviewIntent.putExtra(ReviewActivity.EXTRA_REVIEW_TEXT, item.getContent());
            startActivity(reviewIntent);
        }
    }
}
