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
* Class representing an element on a list of credits for a movie
*/
@Entity(tableName = "movie_credits",
        foreignKeys = @ForeignKey(entity = MovieDetails.class,
                parentColumns = "id",
                childColumns = "movieId",
                onDelete = CASCADE),
        indices = @Index(value = "movieId"))
public  class MovieCreditsItem extends EntityElement implements Parcelable
{
    @Ignore
    public static final String ACTOR_JOB = "Actor";
    @Ignore
    public static final String PERFORMANCE_DEPARTMENT = "Performance";

    @PrimaryKey(autoGenerate = true)
    private Integer id = 0;
    @ColumnInfo(name = "movieId")
    private Integer movieId;
    private String character;
    private String name;
    private Integer order;
    private String department;
    private String job;
    private String profilePath;

    @Ignore
    public final static Creator<MovieCreditsItem> CREATOR = new Creator<MovieCreditsItem>() {

        public MovieCreditsItem createFromParcel(Parcel in) {
            return new MovieCreditsItem(in);
        }

        public MovieCreditsItem[] newArray(int size) {
            return (new MovieCreditsItem[size]);
        }

    };

    @Ignore
    MovieCreditsItem(Parcel in) {
        this.id = ((Integer) Objects.requireNonNull(in.readValue((Integer.class.getClassLoader()))));
        this.movieId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.character = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.order = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.department = ((String) in.readValue((String.class.getClassLoader())));
        this.job = ((String) in.readValue((String.class.getClassLoader())));
        this.profilePath = ((String) in.readValue((String.class.getClassLoader())));
        this.dataLanguage = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieCreditsItem() {
    }

    /**
     *
     * @param id
     * @param movieId
     * @param order
     * @param name
     * @param department
     * @param job
     * @param profilePath
     * @param character
     * @param dataLanguage
     */
    @Ignore
    MovieCreditsItem(@NonNull Integer id, Integer movieId, String character, String name, Integer order, String department, String job, String profilePath, String dataLanguage) {
        super();
        this.id = id;
        this.movieId = movieId;
        this.character = character;
        this.name = name;
        this.order = order;
        this.department = department;
        this.job = job;
        this.profilePath = profilePath;
        this.dataLanguage = dataLanguage;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movie_id) {
        this.movieId = movie_id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profile_path) {
        this.profilePath = profile_path;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(movieId);
        dest.writeValue(character);
        dest.writeValue(name);
        dest.writeValue(order);
        dest.writeValue(department);
        dest.writeValue(job);
        dest.writeValue(profilePath);
        dest.writeValue(dataLanguage);
    }

    public int describeContents() {
        return 0;
    }

}
