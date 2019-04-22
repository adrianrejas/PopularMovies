package org.udacity.android.arejas.popularmovies.ui.adapters;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.data.entities.MovieCreditsItem;
import org.udacity.android.arejas.popularmovies.databinding.ItemViewCreditBinding;

import java.util.List;

/*
* Class used for the management of lists of credits of a movie.
 */
public class MovieCreditsListAdapter extends RecyclerView.Adapter <MovieCreditsListAdapter.MovieCreditsItemViewHolder> {

    private List<MovieCreditsItem> dataList;

    public MovieCreditsListAdapter(List<MovieCreditsItem> dataList) {
        super();
        this.dataList = dataList;
    }

    public List<MovieCreditsItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<MovieCreditsItem> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MovieCreditsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemViewCreditBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_view_credit,
                parent, false);
        return new MovieCreditsItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCreditsItemViewHolder holder, int position) {
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
    class MovieCreditsItemViewHolder extends RecyclerView.ViewHolder {

        MovieCreditsItem item;
        final ItemViewCreditBinding uiBinding;

        /**
         * Constructor
         *
         * @param binding
         */
        MovieCreditsItemViewHolder(ItemViewCreditBinding binding) {
            super(binding.getRoot());
            this.uiBinding = binding;
            binding.executePendingBindings();
        }

        /**
         * Function called when a item list has to be visible, to bind it to the current holder.
         *
         * @param item Movie list item to bind. If null, show the load more layout.
         */
        @SuppressLint("DefaultLocale")
        void bind(MovieCreditsItem item) {
            this.item = item;
            // If not null
            if (item != null) {
                uiBinding.setMovieCreditItem(item);
            }
        }

    }

}
