package org.udacity.android.arejas.popularmovies.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.data.entities.MovieCreditsItem;
import org.udacity.android.arejas.popularmovies.databinding.FragmentCreditBinding;
import org.udacity.android.arejas.popularmovies.gateways.ui.MovieViewModel;
import org.udacity.android.arejas.popularmovies.gateways.ui.factories.ViewModelFactory;
import org.udacity.android.arejas.popularmovies.ui.adapters.MovieCreditsListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Fragment dealing with showing crew of a movie
 */
public class CrewFragment extends Fragment {

    private FragmentCreditBinding uiBinding;

    private MovieCreditsListAdapter mAdapter;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Get the movie activity view model and observe the changes in the credits
        MovieViewModel movieViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), viewModelFactory).get(MovieViewModel.class);
        movieViewModel.getMovieCredits().observe(this, movieCreditsResource -> {
            if (movieCreditsResource != null) {
                switch (movieCreditsResource.getStatus()) {
                    // According to the state of the data, show different layouts
                    case ERROR:
                        showError(movieCreditsResource.getError());
                        break;
                    case LOADING:
                        showLoading();
                        break;
                    case SUCCESS:
                        updateList(movieCreditsResource.getData());
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
        GridLayoutManager glm_grid = new GridLayoutManager(getContext(), iElementsPerRow);
        uiBinding.rvCreditsList.setLayoutManager(glm_grid);

        // Configure adapter for recycler view
        mAdapter = new MovieCreditsListAdapter(null);
        uiBinding.rvCreditsList.setAdapter(mAdapter);

        return uiBinding.getRoot();
    }

    /**
     * Update UI with new data received in the form of list
     * @param data
     */
    private void updateList(List<MovieCreditsItem> data) {
        if (uiBinding != null) {
            data = prepareList(data);
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
     * Prepare list by filtering credit items not related to performance
     * @param data
     * @return
     */
    private List<MovieCreditsItem> prepareList(List<MovieCreditsItem> data) {
        List<MovieCreditsItem> newData = new ArrayList<>();
        for (MovieCreditsItem item: data) {
            if (!item.getJob().equals(MovieCreditsItem.ACTOR_JOB)) {
                newData.add(item);
            }
        }
        return newData;
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
}
