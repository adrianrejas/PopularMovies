package org.udacity.android.arejas.popularmovies.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.data.entities.MovieDetails;
import org.udacity.android.arejas.popularmovies.databinding.FragmentDetailsBinding;
import org.udacity.android.arejas.popularmovies.gateways.ui.MovieViewModel;
import org.udacity.android.arejas.popularmovies.gateways.ui.factories.ViewModelFactory;

import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Fragment dealing with representing details of a movie
 */
public class DetailsFragment extends Fragment {

    private FragmentDetailsBinding uiBinding;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Get the movie activity view model and observe the changes in the details
        MovieViewModel movieViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), viewModelFactory).get(MovieViewModel.class);
        movieViewModel.getMovieDetails().observe(this, movieDetailsResource -> {
            if (movieDetailsResource != null) {
                // According to the state of the data, show different layouts
                switch (movieDetailsResource.getStatus()) {
                    case ERROR:
                        showError(movieDetailsResource.getError());
                        break;
                    case LOADING:
                        showLoading();
                        break;
                    case SUCCESS:
                        populateUIDetails(movieDetailsResource.getData());
                        break;
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        uiBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        return uiBinding.getRoot();
    }

    /**
     *
     * Function in charge of loading the details on the UI. See coments.
     * @param details Details of the movie
     */
    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    private void populateUIDetails(MovieDetails details) {
        /* Check nulls */
        try {
            if ((details == null) || (uiBinding == null)) {
                showError(null);
                return;
            }
            // Set details layout visible
            uiBinding.detailsLayout.setVisibility(View.VISIBLE);
            uiBinding.detailsLoadingLayout.loadingLayout.setVisibility(View.GONE);
            uiBinding.detailsErrorLayout.errorLayout.setVisibility(View.GONE);
            // Set data (Databinding library will populate UI fields properly)
            uiBinding.setMovieDetails(details);
        } catch (Exception e) {
            // Any problem will show an error on screen.
            showError(null);
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
            uiBinding.detailsLayout.setVisibility(View.GONE);
            uiBinding.detailsLoadingLayout.loadingLayout.setVisibility(View.GONE);
            uiBinding.detailsErrorLayout.errorLayout.setVisibility(View.VISIBLE);
            if (uiBinding.detailsErrorLayout.tvError != null) {
                if (error instanceof NullPointerException)
                    uiBinding.detailsErrorLayout.tvError.setText(getString(R.string.error_data));
                if (error instanceof IOException)
                    uiBinding.detailsErrorLayout.tvError.setText(getString(R.string.error_connection));
                else
                    uiBinding.detailsErrorLayout.tvError.setText(getString(R.string.error_ui));
            }
        }
    }

    /**
     * Show the fragment info is loading.
     */
    private void showLoading() {
        if (uiBinding != null) {
            uiBinding.detailsLayout.setVisibility(View.GONE);
            uiBinding.detailsLoadingLayout.loadingLayout.setVisibility(View.VISIBLE);
            uiBinding.detailsErrorLayout.errorLayout.setVisibility(View.GONE);
        }
    }

}
