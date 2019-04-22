package org.udacity.android.arejas.popularmovies.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Class created with the help of http://www.jsonschema2pojo.org/ online application. It represents
 * a list of reviews about a movie. It also contains the annotations for defining entities and 
 * columns for allocating MovieReviewListRestApi.MovieReviewItem objects into databases through room persistence library.
 */
public class MovieReviewListRestApi extends MovieElementRestApi implements Parcelable
{

    private Integer movie_id;
    private Integer page;
    private List<Result> results = null;
    private Integer total_pages;
    private Integer total_results;
    public final static Parcelable.Creator<MovieReviewListRestApi> CREATOR = new Parcelable.Creator<MovieReviewListRestApi>() {

        public MovieReviewListRestApi createFromParcel(Parcel in) {
            return new MovieReviewListRestApi(in);
        }

        public MovieReviewListRestApi[] newArray(int size) {
            return (new MovieReviewListRestApi[size]);
        }

    };

    private MovieReviewListRestApi(Parcel in) {
        this.movie_id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.results, (MovieReviewListRestApi.Result.class.getClassLoader()));
        this.total_pages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.total_results = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieReviewListRestApi() {
    }

    /**
     *
     * @param movie_id
     * @param results
     * @param page
     * @param total_pages
     * @param total_results
     */
    public MovieReviewListRestApi(Integer movie_id, Integer page, List<Result> results, Integer total_pages, Integer total_results) {
        super();
        this.movie_id = movie_id;
        this.page = page;
        this.results = results;
        this.total_pages = total_pages;
        this.total_results = total_results;
    }

    public Integer getMovieId() {
        return movie_id;
    }

    public void setMovieId(Integer movie_id) {
        this.movie_id = movie_id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Integer total_results) {
        this.total_results = total_results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(movie_id);
        dest.writeValue(page);
        dest.writeList(results);
        dest.writeValue(total_pages);
        dest.writeValue(total_results);
    }

    public static class Result implements Parcelable
    {

        private String author;
        private String content;
        private String url;
        public final static Creator<Result> CREATOR = new Creator<Result>() {

            public Result createFromParcel(Parcel in) {
                return new Result(in);
            }

            public Result[] newArray(int size) {
                return (new Result[size]);
            }

        };

        Result(Parcel in) {
            this.author = ((String) in.readValue((String.class.getClassLoader())));
            this.content = ((String) in.readValue((String.class.getClassLoader())));
            this.url = ((String) in.readValue((String.class.getClassLoader())));
        }

        /**
         * No args constructor for use in serialization
         *
         */
        Result() {
        }

        /**
         *
         * @param content
         * @param author
         * @param url
         */
        Result(String author, String content, String url) {
            super();
            this.author = author;
            this.content = content;
            this.url = url;
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
            dest.writeValue(author);
            dest.writeValue(content);
            dest.writeValue(url);
        }

        public int describeContents() {
            return 0;
        }

    }

}