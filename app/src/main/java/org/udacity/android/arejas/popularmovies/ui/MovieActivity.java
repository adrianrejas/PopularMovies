package org.udacity.android.arejas.popularmovies.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.databinding.ActivityMovieBinding;
import org.udacity.android.arejas.popularmovies.gateways.ui.MovieViewModel;
import org.udacity.android.arejas.popularmovies.gateways.ui.factories.ViewModelFactory;
import org.udacity.android.arejas.popularmovies.gateways.data.entities.Resource;
import org.udacity.android.arejas.popularmovies.utils.Utils;

import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Class representing the activity showing the details of a movie
 */
public class MovieActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "org.udacity.android.arejas.popularmovies.MOVIE_ID";

    private MovieViewModel movieViewModel;
    @Inject
    ViewModelFactory viewModelFactory;

    private Menu menu;

    private boolean bSavedAsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Inflate main layout and get UI element references */
        ActivityMovieBinding uiBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie);

        /* Inject dependencies*/
        AndroidInjection.inject(this);

        /* Get ID of the movie to load */
        if (getIntent().hasExtra(EXTRA_MOVIE_ID)) {
            int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);
            this.viewModelFactory.setMovieIdToLoad(movieId);

        }

        /* Get view model*/
        movieViewModel = ViewModelProviders.of(this, this.viewModelFactory).get(MovieViewModel.class);

        // Observer details in order to update title and favorites menu in a suitable way
        movieViewModel.getMovieDetails().observe(this, movieDetailsResource -> {
            if (Objects.requireNonNull(movieDetailsResource).getStatus().equals(Resource.Status.SUCCESS)) {
                if (movieDetailsResource.getData() != null) {
                    setTitle(movieDetailsResource.getData().getTitle());
                    setFavoriteMenuItems(movieDetailsResource.getData().getSavedAsFavorite());
                }
            }
        });

        // Init the view pager with a fragment adapter for showing the fragments with different info
        // of the movie in a tab system
        MovieFragmentAdapter fragmentAdapter = new MovieFragmentAdapter(getSupportFragmentManager(), this);
        uiBinding.vpTabViewer.setAdapter(fragmentAdapter);
        uiBinding.tlTabs.setupWithViewPager(uiBinding.vpTabViewer);
    }

    /**
     * Set Favorite menu items depending of the movie is saved as favorite or not.
     *
     * @param savedAsFavorite
     */
    private void setFavoriteMenuItems(Boolean savedAsFavorite) {
        if ((savedAsFavorite == null)) {
            savedAsFavorite = false;
        }
        bSavedAsFavorite = savedAsFavorite;
        if (this.menu != null) {
            this.menu.getItem(0).setVisible(savedAsFavorite);
            this.menu.getItem(1).setVisible(!savedAsFavorite);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie, menu);
        this.menu = menu;
        // We update here the menu in order to solve problems when rotating device
        setFavoriteMenuItems(bSavedAsFavorite);
        return true;
    }

    /**
     * Function called when a menu item is selected.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_is_favorite:
                unsetMovieAsFavorite();
                return true;
            case R.id.menu_is_not_favorite:
                setMovieAsFavorite();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Call viewmodel for setting movie as favorite and update UI depending of the result
     */
    private void setMovieAsFavorite() {
        Utils.showToast( getString(R.string.setting_favorite));
        LiveData<Resource> favoriteUpdater = movieViewModel.setMovieAsFavorite();
        favoriteUpdater.observe(this, resource -> {
            if (Objects.requireNonNull(resource).getStatus().equals(Resource.Status.SUCCESS)) {
                favoriteUpdater.removeObservers(MovieActivity.this);
                setFavoriteMenuItems(true);
                Utils.showToast(getString(R.string.set_favorite));
            } else if (resource.getStatus().equals(Resource.Status.ERROR)) {
                favoriteUpdater.removeObservers(MovieActivity.this);
                showFavoriteSettingError(resource.getError());
            }
        });
    }

    /**
     * Call viewmodel for unsetting movie as favorite and update UI depending of the result
     */
    private void unsetMovieAsFavorite() {
        Utils.showToast( getString(R.string.unsetting_favorite));
        LiveData<Resource> favoriteUpdater = movieViewModel.unsetMovieAsFavorite();
        favoriteUpdater.observe(this, resource -> {
            if (Objects.requireNonNull(resource).getStatus().equals(Resource.Status.SUCCESS)) {
                favoriteUpdater.removeObservers(MovieActivity.this);
                setFavoriteMenuItems(false);
                Utils.showToast( getString(R.string.unset_favorite));
            } else if (resource.getStatus().equals(Resource.Status.ERROR)) {
                favoriteUpdater.removeObservers(MovieActivity.this);
                showFavoriteSettingError(resource.getError());
            }
        });
    }

    /**
     * Show error when setting a movie as favorite
     *
     * @param error
     */
    private void showFavoriteSettingError(Throwable error) {
        if (error instanceof NullPointerException)
            Utils.showToast( getString(R.string.error_data));
        if (error instanceof IOException)
            Utils.showToast(getString(R.string.error_connection));
        else
            Utils.showToast(getString(R.string.error_ui));
    }

    /**
     * This adapter is used for defining the tab system of the movie activity, providing the
     * fragments it will used, so as the tab configuration.
     */
    static class MovieFragmentAdapter extends FragmentPagerAdapter {

        private static final int NUM_ITEMS = 5;

        private final Context mContext;

        MovieFragmentAdapter(FragmentManager fragmentManager, Context context) {
            super(fragmentManager);
            this.mContext = context;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Details
                    return (new DetailsFragment());
                case 1: // Cast
                    return (new CastFragment());
                case 2: // Crew
                    return (new CrewFragment());
                case 3: // Reviews
                    return (new ReviewsFragment());
                case 4: // Videos
                    return (new VideosFragment());
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if (mContext != null) {
                switch (position) {
                    case 0: // Details
                        return mContext.getString(R.string.movie_tab_details);
                    case 1: // Cast
                        return mContext.getString(R.string.movie_tab_cast);
                    case 2: // Crew
                        return mContext.getString(R.string.movie_tab_crew);
                    case 3: // Reviews
                        return mContext.getString(R.string.movie_tab_reviews);
                    case 4: // Videos
                        return mContext.getString(R.string.movie_tab_videos);
                    default:
                        return null;
                }
            } else {
                return "";
            }
        }

    }
}
