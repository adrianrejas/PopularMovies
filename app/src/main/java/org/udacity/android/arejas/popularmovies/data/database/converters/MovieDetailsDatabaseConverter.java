package org.udacity.android.arejas.popularmovies.data.database.converters;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This class will deal with the conversion of objects and list of objects for saving it in a
 * movie details database. The conversion will be done between list of objects and JSON strings.
 * For JSON parsing gson library is used. Apart from these facts, the rest is self-explanatory.
 */
public class MovieDetailsDatabaseConverter  implements Serializable {

    @TypeConverter
    public String fromListString(List<String> genres) {
        return TextUtils.join("::::", genres);
    }

    @TypeConverter
    public List<String> toListString(String genresListString) {
        return Arrays.asList(TextUtils.split(genresListString, "::::"));
    }

    @TypeConverter
    public Long fromDate(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public Date toDate(Long dateLong) {
        return new Date(dateLong);
    }
}
