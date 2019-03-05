package org.udacity.android.arejas.popularmovies.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Class with elements which can be useful in the rest of classes.
 */
public class Utils {

    /* Enumerator for distinguish between a sorting style by most popular movies or a sorting style
    by top rated movies. */
    public enum MovieSortType {
        POPULAR,
        TOP_RATED;

        public static MovieSortType valueOf(int ordinal) {
            switch (ordinal) {
                case 0:
                    return POPULAR;
                default:
                    return TOP_RATED;
            }
        }
    }

    private static Toast mToast;

    /**
     * This method shows an error on the screen. use this function instead of calling directly
     * Toast functions in order to cancel app toast still on screen.
     *
     * @param context The application context to use.
     * @param text The text string to be shown.
     *
     */
    public static void showToast(Context context, String text) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
