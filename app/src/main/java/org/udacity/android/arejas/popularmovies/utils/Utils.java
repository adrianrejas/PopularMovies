package org.udacity.android.arejas.popularmovies.utils;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import org.udacity.android.arejas.popularmovies.MoviesApplication;
import org.udacity.android.arejas.popularmovies.R;

/**
 * Class with elements which can be useful in the rest of classes.
 */
public class Utils {

    private static final String YOUTUBE_WEB_INTENT_BASE = "http://www.youtube.com/";
    private static final String YOUTUBE_WEB_INTENT_WATCH = "watch";
    private static final String YOUTUBE_WEB_INTENT_VEW_QUERY_PARAM = "v";

    /* Enumerator for distinguish between a sorting style by most popular movies or a sorting style
    by top rated movies. */
    public enum MovieSortType {
        POPULAR,
        TOP_RATED,
        FAVORITES;

        public static MovieSortType valueOf(int ordinal) {
            switch (ordinal) {
                case 0:
                    return POPULAR;
                case 1:
                    return TOP_RATED;
                default:
                    return FAVORITES;
            }
        }
    }

    private static Toast mToast;

    /**
     * This method shows an error on the screen. use this function instead of calling directly
     * Toast functions in order to cancel app toast still on screen.
     *
     * @param text The text string to be shown.
     *
     */
    public static void showToast(String text) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(MoviesApplication.getContext(), text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * Function for launching YouTube application or web with a video to be played
     * @param id YouTube ID of the video to be played
     * @throws ActivityNotFoundException If no activity found to launch the video
     */
    public static void launchYoutubeVideo(String id) throws ActivityNotFoundException{
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(YOUTUBE_WEB_INTENT_BASE).buildUpon().
                        appendEncodedPath(YOUTUBE_WEB_INTENT_WATCH).
                        appendQueryParameter(YOUTUBE_WEB_INTENT_VEW_QUERY_PARAM, id).build());
        webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (webIntent.resolveActivity(MoviesApplication.getContext().getPackageManager()) != null) {
            MoviesApplication.getContext().startActivity(webIntent);
        } else {
            throw new ActivityNotFoundException();
        }
    }

    /**
     * Function for launching a web page (used for reviews)
     * @param web Web to open
     * @throws ActivityNotFoundException If no activity found to launch the video
     */
    public static void launchWebPage(String web) throws ActivityNotFoundException{
        if (web != null) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(web).buildUpon().build());
            webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (webIntent.resolveActivity(MoviesApplication.getContext().getPackageManager()) != null) {
                MoviesApplication.getContext().startActivity(webIntent);
            } else {
                throw new ActivityNotFoundException();
            }
        } else {
            showToast(MoviesApplication.getContext().getString(R.string.error_no_web));
        }
    }


    /**
     * Function for calling Html.fromHtml function correctly depending on the Android version
     *
     * @param htmlText text to convert
     * @return Html.fromHtml returned object
     */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String htmlText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(htmlText);
        }
    }

}
