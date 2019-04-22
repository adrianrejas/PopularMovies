package org.udacity.android.arejas.popularmovies.ui.adapters;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.data.entities.MovieVideoItem;
import org.udacity.android.arejas.popularmovies.databinding.ItemViewVideoBinding;

import java.util.List;

/*
 * Class used for the management of lists of videos of a movie.
 */
public class MovieVideosListAdapter extends RecyclerView.Adapter <MovieVideosListAdapter.MovieVideosItemViewHolder> {

    private List<MovieVideoItem> dataList;
    private final OnMovieVideoItemClickListener mListener;

    public MovieVideosListAdapter(List<MovieVideoItem> dataList,
                                  OnMovieVideoItemClickListener listener) {
        super();
        this.dataList = dataList;
        this.mListener = listener;
    }

    public List<MovieVideoItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<MovieVideoItem> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MovieVideosItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemViewVideoBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_view_video,
                parent, false);
        return new MovieVideosItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVideosItemViewHolder holder, int position) {
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
    class MovieVideosItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        MovieVideoItem item;
        final ItemViewVideoBinding uiBinding;

        /**
         * Constructor
         *
         * @param binding
         */
        MovieVideosItemViewHolder(ItemViewVideoBinding binding) {
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
        void bind(MovieVideoItem item) {
            this.item = item;
            // If not null
            if (this.item != null) {
                uiBinding.setMovieVideoItem(item);
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
    public interface OnMovieVideoItemClickListener {
        void onClick(MovieVideoItem item);
    }

}
