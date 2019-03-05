package org.udacity.android.arejas.popularmovies;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.udacity.android.arejas.popularmovies.data.MovieDetails;
import org.udacity.android.arejas.popularmovies.network.MovieDbApi;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is the details activity class, which is in charge of showing the details of a movie which ID
 * has been passed.
 */
public class DetailsActivity extends AppCompatActivity {

    private static String TAG = "DetailsActivity";

    private static final String SAVED_INSTANCE_MOVIEDETAILS = "MovieDetails";

    public static final String EXTRA_MOVIE_ID = "org.udacity.android.arejas.popularmovies.MOVIE_ID";

    private ViewGroup llDetails, llProgress, llError;
    private TextView tvTitle, tvRate, tvReleaseDate, tvSynopsys, tvCountries, tvGenres, tvError;
    private ImageView ivPoster, ivStartRate;

    private int movieId;
    private MovieDetails movieDetails;

    /**
     * Function called when creating the activity. See comments
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        /* Get reference of UI elements to be used */
        llDetails = findViewById(R.id.details_layout);
        llProgress = findViewById(R.id.details_loading_layout);
        llError = findViewById(R.id.details_error_layout);
        tvTitle = findViewById(R.id.tv_title_details);
        tvRate = findViewById(R.id.tv_rating_details);
        tvReleaseDate = findViewById(R.id.tv_release_details);
        tvSynopsys = findViewById(R.id.tv_overview_details);
        tvCountries = findViewById(R.id.tv_countries_details);
        tvGenres = findViewById(R.id.tv_genres_details);
        tvError = findViewById(R.id.tv_error_details);
        ivPoster = findViewById(R.id.iv_poster_details);
        ivStartRate = findViewById(R.id.iv_star_rating_details);

        /* Get ID of the movie to load */
        if (getIntent().hasExtra(EXTRA_MOVIE_ID)) {
            movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);
        }

        /* Get from bundle, if existing, current movie details */
        if (savedInstanceState != null) {
            movieDetails = savedInstanceState.getParcelable(SAVED_INSTANCE_MOVIEDETAILS);
        } else {
            movieDetails = null;
        }

        /* If existing movie details, load them. If not, request info about the movie */
        if (movieDetails != null) {
            populateUI(movieDetails);
        }
        else {
            if (movieId > 0) {
                // Request the details to the REST API in a background thread, with apiCallback invoked
                // on main thread once finished the request.
                MovieDbApi.requestMovieDetails(movieId, apiCallback, getApplicationContext());
            } else {
                showError(null);
            }
        }
    }

    /**
     * Function called when destroying the activity.
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Function called for saving activity data state before destroying it for restoring the data later.
     * Here we will save the movie details loaded in order not to be requested again when restored.
     *
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_INSTANCE_MOVIEDETAILS, movieDetails);
    }

    /**
     * Callback invoked when background request to REST API has finished. We'll populate the UI with
     * the movie details if success or we'll show an error if failure.
     */
    private final MovieDbApi.MovieDbApiCallback<MovieDetails> apiCallback = new MovieDbApi.MovieDbApiCallback<MovieDetails>() {
        @Override
        public void onSuccess(MovieDetails response) {
            movieDetails = response;
            populateUI(response);
        }

        @Override
        public void onFailure(Throwable exception) {
            showError(exception);
        }
    };

    /**
     *
     * Function in charge of loading the details on the UI. See coments.
     * @param details Details of the movie
     */
    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    private void populateUI (MovieDetails details) {
        /* Check nulls */
        try {
            if ((details == null) || (llDetails == null) || (llProgress == null) ||
                    (llError == null) || (tvError == null) || (tvTitle == null) ||
                    (tvRate == null) || (tvReleaseDate == null) || (tvSynopsys == null) ||
                    (tvCountries == null) || (tvGenres == null) || (ivPoster == null) ||
                    (ivStartRate == null)) {
                showError(null);
                return;
            }
            // Set details layout visible
            llDetails.setVisibility(View.VISIBLE);
            llProgress.setVisibility(View.GONE);
            llError.setVisibility(View.GONE);
            // If not null, get poster image URI and load it with Picasso library
            if (details.getPoster_path() != null) {
                Picasso.with(ivPoster.getContext())
                        .load(MovieDbApi.getImageUri(details.getPoster_path(), MovieDbApi.ImageSize.W500))
                        .error(R.drawable.not_found)
                        .into(ivPoster);
            } else {
                Picasso.with(ivPoster.getContext())
                        .load(R.drawable.not_found)
                        .into(ivPoster);
            }
            // If not null, set Title
            tvTitle.setText(((details.getTitle() != null) && (!details.getTitle().isEmpty())) ?
                    details.getTitle() : getString(R.string.no_title));
            // If not null, set vote average, changing the color of both the text and the star image
            // with a color between red (0.0) and green (10.0).
            if (details.getVote_average() != null) {
                int red = (int) ((10 - details.getVote_average()) * 255 / 10);
                int green = (int) (details.getVote_average() * 255 / 10);
                int ratingColor = Color.rgb(red, green, 0);
                ivStartRate.setColorFilter(ratingColor);
                tvRate.setTextColor(ratingColor);
                tvRate.setText(String.format("%.2f", details.getVote_average()));
            } else {
                tvRate.setText(getString(R.string.no_rating));
            }
            // If not null, set release date, with the format specified at the strings XML.
            String releaseDateString;
            try {
                Date releaseDataObject = details.getRelease_dateDateObj();
                releaseDateString =
                        (new SimpleDateFormat(getString(R.string.date_format))).format(releaseDataObject);
            } catch (ParseException e) {
                releaseDateString = getString(R.string.no_release_date);
            }
            tvReleaseDate.setText(Html.fromHtml(String.format(getString(R.string.details_release_date),
                    (releaseDateString != null) ? releaseDateString : getString(R.string.no_release_date))));
            // If not null, compose the list of genres and set it.
            List<MovieDetails.Genre> genres = details.getGenres();
            if ((genres != null) && (genres.size() != 0)) {
                List<String> genrenames = new ArrayList<>();
                for (MovieDetails.Genre genre : genres) {
                    genrenames.add(genre.getName());
                }
                String genreString = TextUtils.join(", ", genrenames);
                tvGenres.setText(Html.fromHtml(
                        getResources().getQuantityString(R.plurals.details_genres,
                                genres.size(), genreString)));
            } else {
                tvGenres.setText(Html.fromHtml(
                        getResources().getQuantityString(R.plurals.details_genres,
                                1, getString(R.string.no_genre))));
            }
            // If not null, compose the list of countries and set it.
            List<MovieDetails.Production_country> countries = details.getProduction_countries();
            if ((countries != null) && (countries.size() != 0)) {
                List<String> countriesNames = new ArrayList<>();
                for (MovieDetails.Production_country country : countries) {
                    countriesNames.add(country.getName());
                }
                String countriesString = TextUtils.join(", ", countriesNames);
                tvCountries.setText(Html.fromHtml(
                        getResources().getQuantityString(R.plurals.details_countries,
                                countries.size(), countriesString)));
            } else {
                tvCountries.setText(Html.fromHtml(
                        getResources().getQuantityString(R.plurals.details_countries,
                                1, getString(R.string.no_country))));
            }
            // If not null, set synopsis
            tvSynopsys.setText(Html.fromHtml(String.format(getString(R.string.details_synopsis),
                    ((details.getOverview() != null) && (!details.getOverview().isEmpty())) ?
                            details.getOverview() : getString(R.string.no_synopsis))));
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
        if (llDetails != null) llDetails.setVisibility(View.GONE);
        if (llProgress != null) llProgress.setVisibility(View.GONE);
        if (llError != null) llError.setVisibility(View.VISIBLE);
        if (tvError != null) {
            if (error instanceof IOException)
                tvError.setText(getString(R.string.error_connection));
            else
                tvError.setText(getString(R.string.error_ui));
        }
    }

}
