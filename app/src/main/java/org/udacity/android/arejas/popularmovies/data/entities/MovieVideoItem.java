package org.udacity.android.arejas.popularmovies.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Objects;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/*
 * Class representing an element on a list of videos for a movie
 */
@Entity(tableName = "movie_videos",
        foreignKeys = @ForeignKey(entity = MovieDetails.class,
                parentColumns = "id",
                childColumns = "movieId",
                onDelete = CASCADE),
        indices = @Index(value = "movieId"))
public class MovieVideoItem extends EntityElement implements Parcelable
{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id = 0;
    @ColumnInfo(name = "movieId")
    private Integer movieId;
    private String languageCode;
    private String countryCode;
    private String key;
    private String name;
    private String site;
    private Integer size;
    @Ignore
    public final static Creator<MovieVideoItem> CREATOR = new Creator<MovieVideoItem>() {

        public MovieVideoItem createFromParcel(Parcel in) {
            return new MovieVideoItem(in);
        }

        public MovieVideoItem[] newArray(int size) {
            return (new MovieVideoItem[size]);
        }

    };

    @Ignore
    MovieVideoItem(Parcel in) {
        this.id = ((Integer) Objects.requireNonNull(in.readValue((Integer.class.getClassLoader()))));
        this.movieId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.languageCode = ((String) in.readValue((String.class.getClassLoader())));
        this.countryCode = ((String) in.readValue((String.class.getClassLoader())));
        this.key = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.site = ((String) in.readValue((String.class.getClassLoader())));
        this.size = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.dataLanguage = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieVideoItem() {
    }

    /**
     *
     * @param id
     * @param movieId
     * @param site
     * @param languageCode
     * @param name
     * @param key
     * @param countryCode
     * @param size
     * @param dataLanguage
     */
    @Ignore
    MovieVideoItem(@NonNull Integer id, Integer movieId, String languageCode, String countryCode, String key, String name, String site, Integer size, String dataLanguage) {
        super();
        this.id = id;
        this.movieId = movieId;
        this.languageCode = languageCode;
        this.countryCode = countryCode;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.dataLanguage = dataLanguage;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movie_id) {
        this.movieId = movie_id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String language_code) {
        this.languageCode = language_code;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String country_code) {
        this.countryCode = country_code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(movieId);
        dest.writeValue(languageCode);
        dest.writeValue(countryCode);
        dest.writeValue(key);
        dest.writeValue(name);
        dest.writeValue(site);
        dest.writeValue(size);
        dest.writeValue(dataLanguage);
    }

    public int describeContents() {
        return 0;
    }

}
