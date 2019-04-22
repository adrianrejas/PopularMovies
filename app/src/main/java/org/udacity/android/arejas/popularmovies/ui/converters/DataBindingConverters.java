package org.udacity.android.arejas.popularmovies.ui.converters;

import android.annotation.SuppressLint;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.udacity.android.arejas.popularmovies.MoviesApplication;
import org.udacity.android.arejas.popularmovies.R;
import org.udacity.android.arejas.popularmovies.data.network.MovieDbApi;
import org.udacity.android.arejas.popularmovies.utils.Utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
* Class with static functions used by DataBinding library for setting UI according to parameters passed.
 */
public class DataBindingConverters {

    @BindingAdapter({"imageUrl", "errorResource", "loadingResource"})
    public static void loadImage(ImageView view, String url, Drawable errorResource, Drawable loadingResource) {
        // If not null, get poster image URI and load it with Picasso library
        if (url != null) {
            Picasso.with(MoviesApplication.getContext())
                    .load(MovieDbApi.getImageUri(url, MovieDbApi.ImageSize.W500))
                    .error(errorResource)
                    .placeholder(loadingResource)
                    .into(view);
        } else {
            Picasso.with(MoviesApplication.getContext())
                    .load(R.drawable.error)
                    .placeholder(errorResource)
                    .into(view);
        }
    }

    @BindingAdapter("voteAverage")
    public static void loadStarVoteColor(ImageView view, Float voteAverage) {
        // If not null, set vote average, changing the color of both the text and the star image
        // with a color between red (0.0) and green (10.0).
        if (voteAverage != null) {
            int red = (int) ((10 - voteAverage) * 255 / 10);
            int green = (int) (voteAverage * 255 / 10);
            int ratingColor = Color.rgb(red, green, 0);
            view.setColorFilter(ratingColor);
        }
    }

    @SuppressLint("DefaultLocale")
    @BindingAdapter("voteAverage")
    public static void loadTextVoteColored(TextView view, Float voteAverage) {
        // If not null, set vote average, changing the color of both the text and the star image
        // with a color between red (0.0) and green (10.0).
        if (voteAverage != null) {
            int red = (int) ((10 - voteAverage) * 255 / 10);
            int green = (int) (voteAverage * 255 / 10);
            int ratingColor = Color.rgb(red, green, 0);
            view.setTextColor(ratingColor);
            view.setText(String.format("%.2f", voteAverage));
        } else {
            view.setText(MoviesApplication.getContext().getString(R.string.no_rating));
        }
    }

    @BindingAdapter({"date", "dateFormat", "textIntro", "noData"})
    public static void loadDateText(TextView view, Date date, String dateFormat,
                                    String textIntro, String noData) {
        // If not null, set release date, with the format specified at the strings XML.
        if (date != null) {
            @SuppressLint("SimpleDateFormat") String dateString =
                    (new SimpleDateFormat(dateFormat)).format(date);
            view.setText(Utils.fromHtml(String.format(textIntro,
                    (dateString != null) ? dateString : noData)));
        } else {
            view.setText(Utils.fromHtml(String.format(textIntro, noData)));
        }
    }

    @BindingAdapter({"dataText", "textIntro", "noData"})
    public static void loadFancyText(TextView view, String dataText, String textIntro, String noData) {
        if (dataText != null) {
            view.setText(Utils.fromHtml(String.format(textIntro, dataText)));
        } else {
            view.setText(Utils.fromHtml(String.format(textIntro, noData)));
        }
    }

    @BindingAdapter({"dataInt", "textIntro", "noData"})
    public static void loadFancyInteger(TextView view, Integer dataInt, String textIntro, String noData) {
        if (dataInt != null) {
            String dataString = NumberFormat.getNumberInstance().format(dataInt);
            view.setText(Utils.fromHtml(String.format(textIntro,
                    (dataString != null) ? dataString : noData)));
        } else {
            view.setText(Utils.fromHtml(String.format(textIntro, noData)));
        }
    }

    @BindingAdapter({"dataList", "textIntro", "noData"})
    public static void loadFancyDataList(TextView view, List<String> dataList, String textIntro, String noData) {
        // If not null, compose the list and set it.
        if ((dataList != null) && (dataList.size() != 0)) {
            String dataString = TextUtils.join(", ", dataList);
            view.setText(Utils.fromHtml(String.format(textIntro, dataString)));
        } else {
            view.setText(Utils.fromHtml(String.format(textIntro, noData)));
        }
    }

}
