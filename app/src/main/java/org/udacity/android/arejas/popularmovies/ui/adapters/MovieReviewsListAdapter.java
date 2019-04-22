package org.udacity.android.arejas.popularmovies.ui.adapters;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.data.entities.MovieReviewItem;
import org.udacity.android.arejas.popularmovies.databinding.ItemViewReviewBinding;

import java.util.List;

/*
 * Class used for the management of lists of reviews of a movie.
 */
public class MovieReviewsListAdapter extends RecyclerView.Adapter <MovieReviewsListAdapter.MovieReviewsItemViewHolder> {

    private List<MovieReviewItem> dataList;
    private final OnMovieReviewItemClickListener mListener;

    public MovieReviewsListAdapter(List<MovieReviewItem> dataList,
                                   OnMovieReviewItemClickListener listener) {
        super();
        this.dataList = dataList;
        this.mListener = listener;
    }

    public List<MovieReviewItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<MovieReviewItem> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MovieReviewsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemViewReviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_view_review,
                parent, false);
        return new MovieReviewsItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewsItemViewHolder holder, int position) {
        if (dataList != null) holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        if (dataList != null)
            return dataList.size();
        else
            return 0;
    }

    /**
     * View holder for the adapter.
     */
    class MovieReviewsItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        MovieReviewItem item;
        final ItemViewReviewBinding uiBinding;

        /**
         * Constructor
         *
         * @param binding
         */
        MovieReviewsItemViewHolder(ItemViewReviewBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setOnClickListener(this);
            this.uiBinding = binding;
            binding.executePendingBindings();
        }

        /**
         * Function called when a item list has to be visible, to bind it to the current holder.
         *
         * @param item Movie list item to bind. If null, show the load more layout.
         */
        @SuppressLint("DefaultLocale")
        void bind(MovieReviewItem item) {
            this.item = item;
            // If not null
            if (this.item != null) {
                uiBinding.setMovieReviewItem(item);
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
    public interface OnMovieReviewItemClickListener {
        void onClick(MovieReviewItem item);
    }

}
