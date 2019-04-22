package org.udacity.android.arejas.popularmovies.ui.adapters;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.data.entities.MovieListItem;
import org.udacity.android.arejas.popularmovies.databinding.CardViewMovieBinding;

/*
 * Class used for the management of paged lists of movies.
 */
public class MoviePagedListAdapter extends android.arch.paging.PagedListAdapter<MovieListItem,
        MoviePagedListAdapter.MovieListItemViewHolder> {

    private final OnMovieListItemClickListener mListener;

    public MoviePagedListAdapter(
            OnMovieListItemClickListener clickListener) {
        super(diffCallback);
        this.mListener = clickListener;
    }

    @NonNull
    @Override
    public MovieListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardViewMovieBinding binding = DataBindingUtil.inflate(inflater, R.layout.card_view_movie,
                parent, false);
        return new MovieListItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    private static final DiffUtil.ItemCallback<MovieListItem> diffCallback =
            new DiffUtil.ItemCallback<MovieListItem>() {

        @Override
        public boolean areItemsTheSame(@NonNull MovieListItem oldItem, @NonNull MovieListItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MovieListItem oldItem, @NonNull MovieListItem newItem) {
            return (oldItem.getId().equals(newItem.getId())) &&
                    oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getPosterPath().equals(newItem.getPosterPath()) &&
                    oldItem.getVoteAverage().equals(newItem.getVoteAverage());
        }

    };

    /**
     * View holder for the adapter.
     */
    class MovieListItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        MovieListItem item;
        final CardViewMovieBinding uiBinding;

        /**
         * Constructor
         *
         * @param binding
         */
        MovieListItemViewHolder(CardViewMovieBinding binding) {
            super(binding.getRoot());
            binding.executePendingBindings();
            uiBinding = binding;
            // Click listener set for calling user defined listener
            binding.getRoot().setOnClickListener(this);
        }

        /**
         * Function called when a item list has to be visible, to bind it to the current holder.
         *
         * @param item Movie list item to bind. If null, show the load more layout.
         */
        @SuppressLint("DefaultLocale")
        void bind(MovieListItem item) {
            this.item = item;
            // If not null
            if (item != null) {
                // If not null, set movie item
                uiBinding.setMovieItem(item);
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
        void onClick(MovieListItem item);
    }

}
