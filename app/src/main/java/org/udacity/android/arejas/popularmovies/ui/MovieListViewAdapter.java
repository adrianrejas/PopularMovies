package org.udacity.android.arejas.popularmovies.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.data.MovieList;
import org.udacity.android.arejas.popularmovies.network.MovieDbApi;

/**
 * Class with the adapter used for managing movie lists on a RecyclerView.
 */
public class MovieListViewAdapter extends RecyclerView.Adapter<MovieListViewAdapter.MovieViewHolder> {

    private MovieList mMovieList;
    private final OnMovieListItemClickListener mListener;
    private final OnMovieListLoadMoreListener onLoadMoreListener;
    private int lastVisibleItem;
    private int totalItemCount;
    private final int elementsPerRow;
    private boolean loading;

    /**
     * Constructor
     *
     * @param movieList First movie list to use
     * @param recyclerView RecyclerVIew to manage
     * @param clickListener Callback invoked when item clicked
     * @param loadMoreListener Callback invoked when possibility of loading more elements detected.
     * @param elementsRow Elements by row (if gGridLayoutManager used)
     */
    public MovieListViewAdapter(
            MovieList movieList, RecyclerView recyclerView,
            OnMovieListItemClickListener clickListener,
            OnMovieListLoadMoreListener loadMoreListener,
            int elementsRow) {
        this.mMovieList = movieList;
        this.mListener = clickListener;
        this.onLoadMoreListener = loadMoreListener;
        this.elementsPerRow = elementsRow;
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // Redefine spansizelookup in order to set the last element (the one indicating load
            // more elements) to occupy the whole line if GridLayoutManager used.
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position >= mMovieList.getResults().size()) {
                        return elementsPerRow;
                    } else {
                        return 1;
                    }
                }
            });
        }
        /* We define a scroll listener over the recycler view. If, after scrolling, it's has been
         * detected the last element (the one indicating load more) is completely visible (or
          * partially if a small screen) we have to call the load more callback. */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager instanceof GridLayoutManager) {
                    lastVisibleItem = ((GridLayoutManager) layoutManager)
                            .findLastVisibleItemPosition();
                    if (lastVisibleItem == -1) {
                        lastVisibleItem = ((GridLayoutManager) layoutManager)
                                .findLastVisibleItemPosition();
                    }
                } else if (layoutManager instanceof LinearLayoutManager) {
                    lastVisibleItem = ((LinearLayoutManager) layoutManager)
                            .findLastVisibleItemPosition();
                    if (lastVisibleItem == -1) {
                        lastVisibleItem = ((LinearLayoutManager) layoutManager)
                                .findLastVisibleItemPosition();
                    }
                } else {
                    return;
                }
                totalItemCount = layoutManager.getItemCount();
                synchronized (this) {
                    if (!loading
                            && totalItemCount <= (lastVisibleItem + 1)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadNext();
                        }
                        loading = true;
                    }
                }
            }
        });
    }

    /**
     * getMovieList function
     *
     * @return the movie List
     */
    public MovieList getMovieList() {
        return mMovieList;
    }

    /**
     * Set a new movie list
     *
     * @param mMovieList Movie list to set
     */
    public void setMovieList(MovieList mMovieList) {
        this.mMovieList = mMovieList;
    }

    /**
     * Function called for creating a new view holder, with it's associated view inflated.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card_view,
                parent, false);
        return new MovieViewHolder(view);
    }

    /**
     * Function for binding a view holder to a element of the list. If the position provided is in
     * the list, we'll show the info of the element of the list. If it's outside the list, we'll show
     * the load more layout.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        try {
            if (position < mMovieList.getResults().size())
                holder.bind(mMovieList.getResults().get(position));
            else
                holder.bind(null);
        } catch (Exception e) {
            holder.bind(null);
        }
    }

    /**
     * notifyDataSetChanged() customized function in order to set the loading more elements state
     * to false. Recommended to use this.
     */
    public void notifyDataSetChangedCustomized() {
        super.notifyDataSetChanged();
        loading = false;
    }

    /**
     * Get the number of items in the list
     *
     * @return Number of items in the list. it provides one more (representing the load more item)
     */
    @Override
    public int getItemCount() {
        try {
            return mMovieList.getResults().size() + 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * View holder for the adapter.
     */
    class MovieViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        MovieList.Result item;
        final CardView cv_movie_card;
        final FrameLayout fl_loading_cards;
        final TextView tv_title;
        final ImageView iv_star_rate;
        final TextView tv_rate;
        final ImageView iv_poster;

        /**
         * Constructor
         *
         * @param v
         */
        MovieViewHolder(View v) {
            super(v);
            // Click listener set for calling user defined listener
            v.setOnClickListener(this);
            cv_movie_card = v.findViewById(R.id.cv_movie_card);
            fl_loading_cards = v.findViewById(R.id.fl_loading_cards);
            tv_title = v.findViewById(R.id.tv_title_card);
            iv_star_rate = v.findViewById(R.id.iv_star_rating_card);
            tv_rate = v.findViewById(R.id.tv_rating_card);
            iv_poster = v.findViewById(R.id.iv_poster_card);
        }

        /**
         * Function called when a item list has to be visible, to bind it to the current holder.
         *
         * @param item Movie list item to bind. If null, show the load more layout.
         */
        @SuppressLint("DefaultLocale")
        void bind(MovieList.Result item) {
            this.item = item;
            // If not null
            if (this.item != null) {
                // Set movie info layout
                cv_movie_card.setVisibility(View.VISIBLE);
                fl_loading_cards.setVisibility(View.GONE);
                // If not null, set title
                tv_title.setText((item.getTitle() != null) ? item.getTitle() :
                        this.itemView.getContext().getString(R.string.no_title));
                // If not null, load poster image with picasso library
                if (item.getPoster_path() != null) {
                    Picasso.with(iv_poster.getContext())
                            .load(MovieDbApi.getImageUri(item.getPoster_path(), MovieDbApi.ImageSize.W342))
                            .error(R.drawable.not_found)
                            .into(iv_poster);
                } else {
                    Picasso.with(iv_poster.getContext())
                            .load(R.drawable.not_found)
                            .into(iv_poster);
                }
                // If not null, set vote average and colorize both text and associated star image
                if (item.getVote_average() != null) {
                    int red = (int) ((10 - item.getVote_average()) * 255 / 10);
                    int green = (int) (item.getVote_average() * 255 / 10);
                    int ratingColor = Color.rgb(red, green, 0);
                    iv_star_rate.setColorFilter(ratingColor);
                    tv_rate.setTextColor(ratingColor);
                    tv_rate.setText(String.format("%.2f", item.getVote_average()));
                } else {
                    tv_rate.setText(this.itemView.getContext().getString(R.string.no_rating));
                }
            } else {
                // If null, set load more items layout
                cv_movie_card.setVisibility(View.GONE);
                fl_loading_cards.setVisibility(View.VISIBLE);
            }
        }

        /**
         * Function called when item clicked. User defined callback will be invoked
         * @param view
         */
        @Override
        public void onClick(View view) {
            mListener.onClick(this.item);
        }
    }

    /**
     * Callback to implement for defining user action when item clicked.
     */
    public interface OnMovieListItemClickListener {
        void onClick(MovieList.Result item);
    }

    /**
     * Callback to implement for defining user action when possible to load more.
     */
    public interface OnMovieListLoadMoreListener {
        void onLoadNext();
    }
}
