package org.udacity.android.arejas.popularmovies.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Class created with the help of http://www.jsonschema2pojo.org/ online application. It represents
 * a list of videos related with a movie. It also contains the annotations for defining entities and 
 * columns for allocating MovieVideoListRestApi.MovieReviewItem objects into databases through room persistence library.
 */
public class MovieVideoListRestApi extends MovieElementRestApi implements Parcelable
{

    private Integer movie_id;
    private List<Result> results = null;
    public final static Creator<MovieVideoListRestApi> CREATOR = new Creator<MovieVideoListRestApi>() {

        public MovieVideoListRestApi createFromParcel(Parcel in) {
            return new MovieVideoListRestApi(in);
        }

        public MovieVideoListRestApi[] newArray(int size) {
            return (new MovieVideoListRestApi[size]);
        }

    };

    private MovieVideoListRestApi(Parcel in) {
        this.movie_id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.results, (MovieVideoListRestApi.Result.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieVideoListRestApi() {
    }

    /**
     *
     * @param movie_id
     * @param results
     */
    public MovieVideoListRestApi(Integer movie_id, List<Result> results) {
        super();
        this.movie_id = movie_id;
        this.results = results;
    }

    public Integer getMovieId() {
        return movie_id;
    }

    public void setMovieId(Integer movie_id) {
        this.movie_id = movie_id;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(movie_id);
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }

    public static class Result implements Parcelable
    {
        private String id;
        private String language_code;
        private String country_code;
        private String key;
        private String name;
        private String site;
        private Integer size;
        private String type;
        public final static Creator<Result> CREATOR = new Creator<Result>() {

            public Result createFromParcel(Parcel in) {
                return new Result(in);
            }

            public Result[] newArray(int size) {
                return (new Result[size]);
            }

        };

        Result(Parcel in) {
            this.id = ((String) in.readValue((String.class.getClassLoader())));
            this.language_code = ((String) in.readValue((String.class.getClassLoader())));
            this.country_code = ((String) in.readValue((String.class.getClassLoader())));
            this.key = ((String) in.readValue((String.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.site = ((String) in.readValue((String.class.getClassLoader())));
            this.size = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.type = ((String) in.readValue((String.class.getClassLoader())));
        }

        /**
         * No args constructor for use in serialization
         *
         */
        Result() {
        }

        /**
         *
         * @param site
         * @param id
         * @param language_code
         * @param name
         * @param type
         * @param key
         * @param country_code
         * @param size
         */
        Result(String id, String language_code, String country_code, String key, String name, String site, Integer size, String type) {
            super();
            this.id = id;
            this.language_code = language_code;
            this.country_code = country_code;
            this.key = key;
            this.name = name;
            this.site = site;
            this.size = size;
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLanguageCode() {
            return language_code;
        }

        public void setLanguageCode(String language_code) {
            this.language_code = language_code;
        }

        public String getCountryCode() {
            return country_code;
        }

        public void setCountryCode(String country_code) {
            this.country_code = country_code;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(id);
            dest.writeValue(language_code);
            dest.writeValue(country_code);
            dest.writeValue(key);
            dest.writeValue(name);
            dest.writeValue(site);
            dest.writeValue(size);
            dest.writeValue(type);
        }

        public int describeContents() {
            return 0;
        }

    }

}
