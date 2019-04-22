package org.udacity.android.arejas.popularmovies.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.data.entities.MovieListItem;
import org.udacity.android.arejas.popularmovies.databinding.ActivityMainBinding;
import org.udacity.android.arejas.popularmovies.gateways.data.entities.Resource;
import org.udacity.android.arejas.popularmovies.gateways.ui.MainViewModel;
import org.udacity.android.arejas.popularmovies.gateways.ui.factories.ViewModelFactory;
import org.udacity.android.arejas.popularmovies.ui.adapters.MoviePagedListAdapter;
import org.udacity.android.arejas.popularmovies.utils.Utils;

import java.io.IOException;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * This is the main activity class, which is in charge of showing the list of movies in the
 * chosen sort type.
 */
public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private static final String SAVED_INSTANCE_MOVIESORTYPE = "MovieSortType";

    private static final Utils.MovieSortType initialSortType = Utils.MovieSortType.POPULAR;

    private Menu mOptionsMenu;
    private Utils.MovieSortType eSortBy = initialSortType;
    @SuppressWarnings("FieldCanBeLocal")
    private GridLayoutManager glm_grid;
    private MoviePagedListAdapter mAdapter;

    private ActivityMainBinding uiBinding;

    private MainViewModel mainViewModel;
    @Inject ViewModelFactory viewModelFactory;

    private LiveData<Resource<PagedList<MovieListItem>>> resourceBeingShown;

    /**
     * Function called when creating the activity. See comments
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Inflate main layout and get UI element references */
        uiBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        /* Inject dependencies*/
        AndroidInjection.inject(this);

        /* Get view model*/
        mainViewModel = ViewModelProviders.of(this, this.viewModelFactory).get(MainViewModel.class);

        // Configure adapter for recycler view
        configureListAdapter();

        // Set action of refreshing list when refreshing gesture detected
        uiBinding.srlRefreshLayout.setOnRefreshListener(() -> setList(eSortBy, false, true));

        // Get from the bundle the sorting type. If not provided, set initial one.
        if (savedInstanceState != null) {
            eSortBy = Utils.MovieSortType.valueOf(
                    savedInstanceState.getInt(SAVED_INSTANCE_MOVIESORTYPE, initialSortType.ordinal()));
        } else {
            eSortBy = initialSortType;
        }

        // Set the received movie list. If null, request a new noe with the configured sort type
        setList(eSortBy, true, false);
    }

    /**
     * Function called for saving activity data state before destroying it for restoring the data later.
     * Here we will save the data related with the movie sort type. The movie list is not saved here
     * due to the reasons explained earlier.
     *
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_MOVIESORTYPE, eSortBy.ordinal());
    }

    /**
     * Function called for inflating the defined menu, with the options for changing between popular
     * or most rated sorting ways.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mOptionsMenu = menu;
        mOptionsMenu.findItem(R.id.menu_sort_popular).setVisible(eSortBy != Utils.MovieSortType.POPULAR);
        mOptionsMenu.findItem(R.id.menu_sort_top_rated).setVisible(eSortBy != Utils.MovieSortType.TOP_RATED);
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
            case R.id.menu_sort_popular:
                setList(Utils.MovieSortType.POPULAR, true, false);
                return true;
            case R.id.menu_sort_top_rated:
                setList(Utils.MovieSortType.TOP_RATED, true, false);
                return true;
            case R.id.menu_sort_favorites:
                setList(Utils.MovieSortType.FAVORITES, true, false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Function called for setting a new movie list, requesting it to the REST API
     *
     * @param sortType The sort type to be applied
     * @param showLoading true if wanted to show the loading layout until info got (we don't want in
     *                case of swipe refresh, because it has it's own way to info about the loading process).
     * @param refreshData true if wanted to reload the data, false if it's enough with cached data.
     *
     */
    private void setList(Utils.MovieSortType sortType, boolean showLoading, boolean refreshData) {
        Utils.MovieSortType ePrevSortBy = eSortBy;
        eSortBy = sortType;
        /* If we change from a not favorite list to a favorite list or the contrary, we configure again
         * the adapter, because these lists are provided one by a contiguous data source and the other
          * by a tiled data source, and they cannot share the same adapter. */
        if (((ePrevSortBy.equals(Utils.MovieSortType.FAVORITES)) &&
                (!eSortBy.equals(Utils.MovieSortType.FAVORITES))) ||
                ((!ePrevSortBy.equals(Utils.MovieSortType.FAVORITES)) &&
                (eSortBy.equals(Utils.MovieSortType.FAVORITES)))) {
            configureListAdapter();
        }
        if (showLoading) {
            prepareUIForSortType(eSortBy);
            showLoading();
        }
        // Request the list to the REST API in a background thread, with apiCallback invoked
        // on main thread once finished the request.
        //MovieDbApi.requestNewMovieList(eSortBy, apiCallback, getApplicationContext());
        if (resourceBeingShown != null) {
            resourceBeingShown.removeObservers(this);
            resourceBeingShown = null;
        }
        switch (eSortBy) {
            case POPULAR:
                resourceBeingShown = mainViewModel.getPopularMovies(refreshData);
                break;
            case TOP_RATED:
                resourceBeingShown = mainViewModel.getTopRatedMovies(refreshData);
                break;
            case FAVORITES:
                resourceBeingShown = mainViewModel.getFavoritesListMovies();
                break;
        }
        if (resourceBeingShown != null) {
            resourceBeingShown.observe(this, new Observer<Resource<PagedList<MovieListItem>>>() {
                private boolean mustScrollToBeginning = true;
                @Override
                public void onChanged(@Nullable Resource<PagedList<MovieListItem>> pagedListResource) {
                    if (pagedListResource == null) {
                        showError(new NullPointerException("No data was loaded."));
                    } else {
                        if (pagedListResource.getStatus() == Resource.Status.ERROR) {
                            showError(pagedListResource.getError());
                            uiBinding.srlRefreshLayout.setRefreshing(false);
                        } else if (pagedListResource.getStatus() == Resource.Status.LOADING &&
                                (pagedListResource.getData() != null && pagedListResource.getData().isEmpty())) {
                            if (showLoading)
                                showLoading();
                        } else {
                            updateList(pagedListResource.getData(), mustScrollToBeginning);
                            mustScrollToBeginning = false;
                            uiBinding.srlRefreshLayout.setRefreshing(false);
                        }
                    }
                }
            });
        }
    }

    /**
     * Function in charge of changing the title and menu of the activity depending of the sorting type.
     *
     * @param  sortType Sort type to apply
     */
    private void prepareUIForSortType(Utils.MovieSortType sortType) {
        if (sortType == Utils.MovieSortType.POPULAR) {
            if (mOptionsMenu != null) {
                mOptionsMenu.findItem(R.id.menu_sort_popular).setVisible(false);
                mOptionsMenu.findItem(R.id.menu_sort_top_rated).setVisible(true);
                mOptionsMenu.findItem(R.id.menu_sort_favorites).setVisible(true);
            }
            setTitle(getString(R.string.popular_title));
        } else if (sortType == Utils.MovieSortType.TOP_RATED) {
            if (mOptionsMenu != null) {
                mOptionsMenu.findItem(R.id.menu_sort_popular).setVisible(true);
                mOptionsMenu.findItem(R.id.menu_sort_top_rated).setVisible(false);
                mOptionsMenu.findItem(R.id.menu_sort_favorites).setVisible(true);
            }
            setTitle(getString(R.string.top_rated_title));
        } else {
            if (mOptionsMenu != null) {
                mOptionsMenu.findItem(R.id.menu_sort_popular).setVisible(true);
                mOptionsMenu.findItem(R.id.menu_sort_top_rated).setVisible(true);
                mOptionsMenu.findItem(R.id.menu_sort_favorites).setVisible(false);
            }
            setTitle(getString(R.string.favorites_title));
        }
    }

    private void configureListAdapter() {

        /* Get number of items in a row*/
        int iElementsPerRow = getResources().getInteger(R.integer.movie_list_items_per_row);

        // Configure recycler view with a grid layout
        glm_grid = new GridLayoutManager(this, iElementsPerRow);
        uiBinding.rvMovieList.setLayoutManager(glm_grid);

        // Configure adapter for recycler view
        mAdapter = new MoviePagedListAdapter(mItemClickListener);
        uiBinding.rvMovieList.setAdapter(mAdapter);
    }

    /**
     * Updates the list used by the activity. We set the list in the list adapter and notify it.
     * We cancel both loading more and swipe to refresh refreshing methods.
     *
     * @param newList New list to set
     * @param mustScrollToBeginning true if list must be scrolled to the beginning
     */
    private void updateList(PagedList<MovieListItem> newList, boolean mustScrollToBeginning) {
        if ((newList != null) && (!newList.isEmpty())) {
            uiBinding.rvMovieList.setVisibility(View.VISIBLE);
            uiBinding.mainLoadingLayout.loadingLayout.setVisibility(View.GONE);
            uiBinding.mainErrorLayout.errorLayout.setVisibility(View.GONE);
            uiBinding.mainNoElementsLayout.noElementsLayout.setVisibility(View.GONE);
            if (mustScrollToBeginning) {
                new Handler().postDelayed(() -> uiBinding.rvMovieList.scrollToPosition(0), 200);
            }
            mAdapter.submitList(newList);
        } else {
            uiBinding.rvMovieList.setVisibility(View.GONE);
            uiBinding.mainLoadingLayout.loadingLayout.setVisibility(View.GONE);
            uiBinding.mainErrorLayout.errorLayout.setVisibility(View.GONE);
            uiBinding.mainNoElementsLayout.noElementsLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Show an error on the activity. The idea has been to escalate errors to the UI classes by the
     * exception mechanisms and let these classes decide how to show the error.
     *
     * @param error Exception with the error appeared.
     */
    private void showError(Throwable error) {
        uiBinding.rvMovieList.setVisibility(View.GONE);
        uiBinding.mainLoadingLayout.loadingLayout.setVisibility(View.GONE);
        uiBinding.mainErrorLayout.errorLayout.setVisibility(View.VISIBLE);
        uiBinding.mainNoElementsLayout.noElementsLayout.setVisibility(View.GONE);
        if (error instanceof NullPointerException)
            uiBinding.mainErrorLayout.tvError.setText(getString(R.string.error_data));
        if (error instanceof IOException)
            uiBinding.mainErrorLayout.tvError.setText(getString(R.string.error_connection));
        else
            uiBinding.mainErrorLayout.tvError.setText(getString(R.string.error_ui));
    }

    /**
     * Show loading data on the activity.
     *
     */
    private void showLoading() {
        uiBinding.rvMovieList.setVisibility(View.GONE);
        uiBinding.mainLoadingLayout.loadingLayout.setVisibility(View.VISIBLE);
        uiBinding.mainErrorLayout.errorLayout.setVisibility(View.GONE);
        uiBinding.mainNoElementsLayout.noElementsLayout.setVisibility(View.GONE);
    }

    /**
     * Listener called when movie list item is clicked. Now we open the details activity for the
     * movie by passing it's ID.
     */
    private final MoviePagedListAdapter.OnMovieListItemClickListener mItemClickListener =
            item -> {
                Intent movieIntent = new Intent(MainActivity.this,
                        MovieActivity.class);
                movieIntent.putExtra(MovieActivity.EXTRA_MOVIE_ID, item.getId());
                startActivity(movieIntent);
            };

}
