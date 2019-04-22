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
 * Class representing an element on a list of reviews for a movie
 */
@Entity(tableName = "movie_reviews",
        foreignKeys = @ForeignKey(entity = MovieDetails.class,
                parentColumns = "id",
                childColumns = "movieId",
                onDelete = CASCADE),
        indices = @Index(value = "movieId"))
public class MovieReviewItem extends EntityElement implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id = 0;
    @ColumnInfo(name = "movieId")
    private Integer movieId;
    private String author;
    private String content;
    private String url;
    @Ignore
    public final static Creator<MovieReviewItem> CREATOR = new Creator<MovieReviewItem>() {

        public MovieReviewItem createFromParcel(Parcel in) {
            return new MovieReviewItem(in);
        }

        public MovieReviewItem[] newArray(int size) {
            return (new MovieReviewItem[size]);
        }

    };

    @Ignore
    MovieReviewItem(Parcel in) {
        this.id = ((Integer) Objects.requireNonNull(in.readValue((Integer.class.getClassLoader()))));
        this.movieId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.author = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.dataLanguage = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieReviewItem() {
    }

    /**
     *
     * @param movieId
     * @param id
     * @param content
     * @param author
     * @param url
     */
    @Ignore
    MovieReviewItem(Integer movieId, String author, String content, @NonNull Integer id, String url) {
        super();
        this.id = id;
        this.movieId = movieId;
        this.author = author;
        this.content = content;
        this.url = url;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(movieId);
        dest.writeValue(author);
        dest.writeValue(content);
        dest.writeValue(url);
        dest.writeValue(dataLanguage);
    }

    public int describeContents() {
        return 0;
    }

}
