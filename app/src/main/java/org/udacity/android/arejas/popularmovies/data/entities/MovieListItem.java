package org.udacity.android.arejas.popularmovies.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * Class representing an element on a list of movies
 */
public class MovieListItem extends EntityElement implements Parcelable {

    private Integer id;
    private Float voteAverage;
    private String title;
    private String posterPath;

    public final static Parcelable.Creator<MovieListItem> CREATOR = new Parcelable.Creator<MovieListItem>() {

        public MovieListItem createFromParcel(Parcel in) {
            return new MovieListItem(in);
        }

        public MovieListItem[] newArray(int size) {
            return (new MovieListItem[size]);
        }

    };

    private MovieListItem(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.voteAverage = ((Float) in.readValue((Float.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
        this.dataLanguage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MovieListItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MovieListItem withId(Integer id) {
        this.id = id;
        return this;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Float vote_average) {
        this.voteAverage = vote_average;
    }

    public MovieListItem withVote_average(Float vote_average) {
        this.voteAverage = vote_average;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieListItem withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String poster_path) {
        this.posterPath = poster_path;
    }

    public MovieListItem withPosterPath(String poster_path) {
        this.posterPath = poster_path;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(voteAverage);
        dest.writeValue(title);
        dest.writeValue(posterPath);
        dest.writeValue(dataLanguage);
    }

    public int describeContents() {
        return 0;
    }
}
