package org.udacity.android.arejas.popularmovies.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.udacity.android.arejas.popularmovies.data.database.converters.MovieDetailsDatabaseConverter;

import java.util.Date;
import java.util.List;

/*
 * Class representing the details of a movie
 */
@Entity(tableName = "movie_details")
public class MovieDetails extends EntityElement implements Parcelable
{

    private Boolean adult;
    private Integer budget;
    @TypeConverters({MovieDetailsDatabaseConverter.class})
    private List<String> genres = null;
    private String homepage;
    @PrimaryKey
    private Integer id;
    private String imdbId;
    private String originalTitle;
    private String synopsis;
    private String posterPath;
    @TypeConverters({MovieDetailsDatabaseConverter.class})
    private List<String> productionCompanies = null;
    @TypeConverters({MovieDetailsDatabaseConverter.class})
    private List<String> productionCountries = null;
    @TypeConverters({MovieDetailsDatabaseConverter.class})
    private Date releaseDate;
    private Integer revenue;
    @TypeConverters({MovieDetailsDatabaseConverter.class})
    private List<String> spokenLanguages = null;
    private String status;
    private String tagline;
    private String title;
    private Float voteAverage;
    private Integer voteCount;
    @Ignore
    private Boolean savedAsFavorite;

    @Ignore
    public final static Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {

        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        public MovieDetails[] newArray(int size) {
            return (new MovieDetails[size]);
        }

    };

    @Ignore
    private MovieDetails(Parcel in) {
        this.adult = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.budget = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.genres, (String.class.getClassLoader()));
        this.homepage = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.imdbId = ((String) in.readValue((String.class.getClassLoader())));
        this.originalTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.synopsis = ((String) in.readValue((String.class.getClassLoader())));
        this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.productionCompanies, (String.class.getClassLoader()));
        in.readList(this.productionCountries, (String.class.getClassLoader()));
        this.releaseDate = ((Date) in.readValue((String.class.getClassLoader())));
        this.revenue = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.spokenLanguages, (String.class.getClassLoader()));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.tagline = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.voteAverage = ((Float) in.readValue((Float.class.getClassLoader())));
        this.voteCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.savedAsFavorite = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.dataLanguage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MovieDetails() {
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public MovieDetails withAdult(Boolean adult) {
        this.adult = adult;
        return this;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public MovieDetails withBudget(Integer budget) {
        this.budget = budget;
        return this;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public MovieDetails withGenres(List<String> genres) {
        this.genres = genres;
        return this;
    }

        public String getGenresString() {
        return TextUtils.join(", ", this.genres);
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public MovieDetails withHomepage(String homepage) {
        this.homepage = homepage;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MovieDetails withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdb_id) {
        this.imdbId = imdb_id;
    }

    public MovieDetails withImdbId(String imdb_id) {
        this.imdbId = imdb_id;
        return this;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String original_title) {
        this.originalTitle = original_title;
    }

    public MovieDetails withOriginalTitle(String original_title) {
        this.originalTitle = original_title;
        return this;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public MovieDetails withSynopsis(String synopsis) {
        this.synopsis = synopsis;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String poster_path) {
        this.posterPath = poster_path;
    }

    public MovieDetails withPosterPath(String poster_path) {
        this.posterPath = poster_path;
        return this;
    }

    public List<String> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<String> production_companies) {
        this.productionCompanies = production_companies;
    }

    public MovieDetails withProductionCompanies(List<String> production_companies) {
        this.productionCompanies = production_companies;
        return this;
    }

    public String getProductionCompaniesString() {
        return TextUtils.join(", ", this.productionCompanies);
    }

    public List<String> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<String> production_countries) {
        this.productionCountries = production_countries;
    }

    public MovieDetails withProductionCountries(List<String> production_countries) {
        this.productionCountries = production_countries;
        return this;
    }

    public String getProductionCountriesString() {
        return TextUtils.join(", ", this.productionCountries);
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public MovieDetails withRevenue(Integer revenue) {
        this.revenue = revenue;
        return this;
    }

    public List<String> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(List<String> spoken_languages) {
        this.spokenLanguages = spoken_languages;
    }

    public MovieDetails withSpokenLanguages(List<String> spoken_languages) {
        this.spokenLanguages = spoken_languages;
        return this;
    }

    public String getSpokenLanguagesString() {
        return TextUtils.join(", ", this.spokenLanguages);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MovieDetails withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public MovieDetails withTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieDetails withTitle(String title) {
        this.title = title;
        return this;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Float vote_average) {
        this.voteAverage = vote_average;
    }

    public MovieDetails withVoteAverage(Float vote_average) {
        this.voteAverage = vote_average;
        return this;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer vote_count) {
        this.voteCount = vote_count;
    }

    public MovieDetails withVoteCount(Integer vote_count) {
        this.voteCount = vote_count;
        return this;
    }

    public Boolean getSavedAsFavorite() {
        return savedAsFavorite;
    }

    public void setSavedAsFavorite(Boolean savedAsFavorite) {
        this.savedAsFavorite = savedAsFavorite;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(adult);
        dest.writeValue(budget);
        dest.writeList(genres);
        dest.writeValue(homepage);
        dest.writeValue(id);
        dest.writeValue(imdbId);
        dest.writeValue(originalTitle);
        dest.writeValue(synopsis);
        dest.writeValue(posterPath);
        dest.writeList(productionCompanies);
        dest.writeList(productionCountries);
        dest.writeValue(releaseDate);
        dest.writeValue(revenue);
        dest.writeList(spokenLanguages);
        dest.writeValue(status);
        dest.writeValue(tagline);
        dest.writeValue(title);
        dest.writeValue(voteAverage);
        dest.writeValue(voteCount);
        dest.writeValue(savedAsFavorite);
        dest.writeValue(dataLanguage);
    }

    public int describeContents() {
        return 0;
    }

}