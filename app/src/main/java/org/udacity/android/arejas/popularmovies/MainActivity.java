package org.udacity.android.arejas.popularmovies;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.udacity.android.arejas.popularmovies.data.MovieList;
import org.udacity.android.arejas.popularmovies.network.MovieDbApi;
import org.udacity.android.arejas.popularmovies.ui.MovieListViewAdapter;
import org.udacity.android.arejas.popularmovies.utils.Utils;

import java.io.IOException;

/**
 * This is the main activity class, which is in charge of showing the list of movies in the
 * chosen sort type.
 */
public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private static final String SAVED_INSTANCE_MOVIESORTYPE = "MovieSortType";

    /* The movie list object has been implemented as an static parameter in order to save info when
     * switching from portrait to landscape and the contrary.
     * The most obvious solution would have been to save it at onSaveInstanceState() function and
     * getting it again at onCreate() function, and it was done in that way at the beginning. But
     * this caused TransactionTooLarge exceptions when loaded too many list elements when calling
     * onSaveInstanceState() function.
     * I check the working with the profiler tool and there was no problems with this.
     * I believe the ideal solution will come with the use of the ViewModel pattern.
     *
     * */
    private static MovieList movieList;

    private static final Utils.MovieSortType initialSortType = Utils.MovieSortType.POPULAR;

    private Menu mOptionsMenu;
    private Utils.MovieSortType eSortBy = initialSortType;
    private RecyclerView rv_movie_list;
    @SuppressWarnings("FieldCanBeLocal")
    private GridLayoutManager glm_grid;
    private MovieListViewAdapter mAdapter;
    private SwipeRefreshLayout llMain;
    private ViewGroup llProgress, llError;
    private TextView tvError;

    /**
     * Function called when creating the activity. See comments
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Inflate main layout */
        setContentView(R.layout.activity_main);

        /* Get UI element references */
        rv_movie_list = findViewById(R.id.rv_movie_list);
        llMain = findViewById(R.id.main_layout);
        llProgress = findViewById(R.id.main_loading_layout);
        llError = findViewById(R.id.main_error_layout);
        tvError = findViewById(R.id.tv_error_main);

        /* Get number of items in a row*/
        int iElementsPerRow = getResources().getInteger(R.integer.movie_list_items_per_row);

        // Configure recycler view with a grid layout
        glm_grid = new GridLayoutManager(this, iElementsPerRow);
        rv_movie_list.setLayoutManager(glm_grid);

        // Configure adapter for recycler view
        mAdapter = new MovieListViewAdapter(
                movieList,rv_movie_list, mItemClickListener, mLoadMoreListener, iElementsPerRow);
        rv_movie_list.setAdapter(mAdapter);

        // Set action of refreshing list when refreshing gesture detected
        llMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setList(eSortBy, true);
            }
        });

        // Get from the bundle the sorting type. If not provided, set initial one.
        if (savedInstanceState != null) {
            eSortBy = Utils.MovieSortType.valueOf(
                    savedInstanceState.getInt(SAVED_INSTANCE_MOVIESORTYPE, initialSortType.ordinal()));
        } else {
            eSortBy = initialSortType;
            movieList = null;
        }

        // Set the received movie list. If null, request a new noe with the configured sort type
        if (movieList != null) {
            prepareUIForSortType(eSortBy);
            updateList(movieList);
        } else {
            setList(eSortBy, false);
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
                setList(Utils.MovieSortType.POPULAR, false);
                return true;
            case R.id.menu_sort_top_rated:
                setList(Utils.MovieSortType.TOP_RATED, false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Function called for setting a new movie list, requesting it to the REST API
     *
     * @param sortType The sort type to be applied
     * @param refresh true if wanted to show the loading layout until info got (we don't want in
     *                case of swipe refresh, because it has it's own way to info about the loading process).
     */
    private void setList(Utils.MovieSortType sortType, boolean refresh) {
        eSortBy = sortType;
        if (!refresh) {
            prepareUIForSortType(eSortBy);
            if (llMain != null) llMain.setVisibility(View.GONE);
            if (llProgress != null) llProgress.setVisibility(View.VISIBLE);
            if (llError != null) llError.setVisibility(View.GONE);
            if (rv_movie_list != null) rv_movie_list.scrollToPosition(0);
        }
        // Request the list to the REST API in a background thread, with apiCallback invoked
        // on main thread once finished the request.
        MovieDbApi.requestNewMovieList(eSortBy, apiCallback, getApplicationContext());
    }

    /**
     * Function called for loading more items to the current movie list, requesting it to the REST API
     */
    private void loadMoreList() {
        // Request the the REST API to load more items in a background thread, with apiCallback invoked
        // on main thread once finished the request.
        MovieDbApi.requestLoadMorePages(eSortBy, movieList, apiCallback, getApplicationContext());
    }

    /**
     * API callback when background API REST requests finished. It updates the list in case of
     * success and shows an error in case of failure.
     */
    private final MovieDbApi.MovieDbApiCallback<MovieList> apiCallback = new MovieDbApi.MovieDbApiCallback<MovieList>() {
        @Override
        public void onSuccess(MovieList response) {
            updateList(response);
        }

        @Override
        public void onFailure(Throwable exception) {
            showError(exception);
        }
    };

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
            }
            setTitle(getString(R.string.popular_title));
        } else {
            if (mOptionsMenu != null) {
                mOptionsMenu.findItem(R.id.menu_sort_popular).setVisible(true);
                mOptionsMenu.findItem(R.id.menu_sort_top_rated).setVisible(false);
            }
            setTitle(getString(R.string.top_rated_title));
        }
    }

    /**
     * Updates the list used by the activity. We set the list in the list adapter and notify it.
     * We cancel both loading more and swipe to refresh refreshing methods.
     *
     * @param newList New list to set
     */
    private void updateList(MovieList newList) {
        if (newList != null) {
            if (llMain != null) llMain.setVisibility(View.VISIBLE);
            if (llProgress != null) llProgress.setVisibility(View.GONE);
            if (llError != null) llError.setVisibility(View.GONE);
            movieList = newList;
            mAdapter.setMovieList(movieList);
            llMain.setRefreshing(false);
            mAdapter.notifyDataSetChangedCustomized();
        }
    }

    /**
     * Show an error on the activity. The idea has been to escalate errors to the UI classes by the
     * exception mechanisms and let these classes decide how to show the error.
     *
     * @param error Exception with the error appeared.
     */
    private void showError(Throwable error) {
        if (llMain != null) llMain.setVisibility(View.GONE);
        if (llProgress != null) llProgress.setVisibility(View.GONE);
        if (llError != null) llError.setVisibility(View.VISIBLE);
        if (tvError != null) {
            if (error instanceof IOException)
                tvError.setText(getString(R.string.error_connection));
            else
                tvError.setText(getString(R.string.error_ui));
        }
    }

    /**
     * Listener called when movie list item is clicked. Now we open the details activity for the
     * movie by passing it's ID.
     */
    private final MovieListViewAdapter.OnMovieListItemClickListener mItemClickListener =
            new MovieListViewAdapter.OnMovieListItemClickListener() {
                @Override
                public void onClick(MovieList.Result item) {
                    Intent detailsIntent = new Intent(MainActivity.this,
                            DetailsActivity.class);
                    detailsIntent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, item.getId());
                    startActivity(detailsIntent);
                }
            };

    /**
     * Listener called when adapter informs more items can be loaded. Now we call to REST API in
     * order to load more items.
     */
    private final MovieListViewAdapter.OnMovieListLoadMoreListener mLoadMoreListener =
            new MovieListViewAdapter.OnMovieListLoadMoreListener() {
                @Override
                public void onLoadNext() {
                    loadMoreList();
                }
            };

}
