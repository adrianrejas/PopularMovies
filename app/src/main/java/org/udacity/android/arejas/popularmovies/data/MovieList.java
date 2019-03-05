package org.udacity.android.arejas.popularmovies.data;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class created with the help of http://www.jsonschema2pojo.org/ online application. It represents
 * a list of movies and it's elements.
 */
public class MovieList implements Parcelable
{
    private static final int ELEMENTS_TO_CHECK_WHEN_MIXING = 40;

    private Integer page;
    private Integer total_results;
    private Integer total_pages;
    private List<Result> results = null;
    public final static Parcelable.Creator<MovieList> CREATOR = new Creator<MovieList>() {

        public MovieList createFromParcel(Parcel in) {
            return new MovieList(in);
        }

        public MovieList[] newArray(int size) {
            return (new MovieList[size]);
        }

    };

    private MovieList(Parcel in) {
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.total_results = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.total_pages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.results, (MovieList.Result.class.getClassLoader()));
    }

    public MovieList() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public MovieList withPage(Integer page) {
        this.page = page;
        return this;
    }

    public Integer getTotalResults() {
        return total_results;
    }

    public void setTotalResults(Integer total_results) {
        this.total_results = total_results;
    }

    public MovieList withTotalResults(Integer total_results) {
        this.total_results = total_results;
        return this;
    }

    public Integer getTotalPages() {
        return total_pages;
    }

    public void setTotalPages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public MovieList withTotalPages(Integer total_pages) {
        this.total_pages = total_pages;
        return this;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public MovieList withResults(List<Result> results) {
        this.results = results;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page);
        dest.writeValue(total_results);
        dest.writeValue(total_pages);
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }

    public void mixWith(MovieList newMovieList) {
        /* If new movie list is empty, we don't need to do anything */
        if (newMovieList.results.isEmpty())
            return;
        /* Set the biggest current page number between the two */
        if (newMovieList.page > this.page)
            this.page = newMovieList.page;
        /* Set the number of pages to the new one */
        this.total_pages = newMovieList.total_pages;
        /* We check the first elements of the array of the new movie list are not yet in the list
         * (this could happen if it passes much time between updates and in the middle new elements
         * were added). If a new movie is yet on the list, remove it. */
        /* NOTE: we check jsut the last elements of the current list, in order to avoid the problem
         * of check a lot of elements every time a mix is done after having mixed the same list
          * many times*/
        List<Result> lastElementsOfCurrentList = this.results.subList(
                Math.max((this.results.size() - ELEMENTS_TO_CHECK_WHEN_MIXING), 0),
                this.results.size());
        boolean needToKeepCleaningNewList = true;
        while (needToKeepCleaningNewList && (!newMovieList.results.isEmpty())) {
            needToKeepCleaningNewList = false;
            Result itemToTest = newMovieList.results.get(0);
            for (Result item : lastElementsOfCurrentList) {
                if (item.getId().equals(itemToTest.getId())) {
                    newMovieList.results.remove(0);
                    needToKeepCleaningNewList = true;
                }
            }
        }
        /* Now we add the remaining movies into the list. */
        this.results.addAll(newMovieList.results);
        /* And finally we set the number of results to the size of the list*/
        this.total_results = this.results.size();
    }

    public static class Result implements Parcelable {

        private Integer vote_count;
        private Integer id;
        private Boolean video;
        private Float vote_average;
        private String title;
        private Float popularity;
        private String poster_path;
        private String original_language;
        private String original_title;
        private List<Integer> genre_ids = null;
        private String backdrop_path;
        private Boolean adult;
        private String overview;
        private String release_date;
        public final static Parcelable.Creator<Result> CREATOR = new Creator<Result>() {

            public Result createFromParcel(Parcel in) {
                return new Result(in);
            }

            public Result[] newArray(int size) {
                return (new Result[size]);
            }

        };

        Result(Parcel in) {
            this.vote_count = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.video = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            this.vote_average = ((Float) in.readValue((Float.class.getClassLoader())));
            this.title = ((String) in.readValue((String.class.getClassLoader())));
            this.popularity = ((Float) in.readValue((Float.class.getClassLoader())));
            this.poster_path = ((String) in.readValue((String.class.getClassLoader())));
            this.original_language = ((String) in.readValue((String.class.getClassLoader())));
            this.original_title = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(this.genre_ids, (java.lang.Integer.class.getClassLoader()));
            this.backdrop_path = ((String) in.readValue((String.class.getClassLoader())));
            this.adult = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            this.overview = ((String) in.readValue((String.class.getClassLoader())));
            this.release_date = ((String) in.readValue((String.class.getClassLoader())));
        }

        Result() {
        }

        public Integer getVote_count() {
            return vote_count;
        }

        public void setVote_count(Integer vote_count) {
            this.vote_count = vote_count;
        }

        public Result withVote_count(Integer vote_count) {
            this.vote_count = vote_count;
            return this;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Result withId(Integer id) {
            this.id = id;
            return this;
        }

        public Boolean getVideo() {
            return video;
        }

        public void setVideo(Boolean video) {
            this.video = video;
        }

        public Result withVideo(Boolean video) {
            this.video = video;
            return this;
        }

        public Float getVote_average() {
            return vote_average;
        }

        public void setVote_average(Float vote_average) {
            this.vote_average = vote_average;
        }

        public Result withVote_average(Float vote_average) {
            this.vote_average = vote_average;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Result withTitle(String title) {
            this.title = title;
            return this;
        }

        public Float getPopularity() {
            return popularity;
        }

        public void setPopularity(Float popularity) {
            this.popularity = popularity;
        }

        public Result withPopularity(Float popularity) {
            this.popularity = popularity;
            return this;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public Result withPoster_path(String poster_path) {
            this.poster_path = poster_path;
            return this;
        }

        public String getOriginal_language() {
            return original_language;
        }

        public void setOriginal_language(String original_language) {
            this.original_language = original_language;
        }

        public Result withOriginal_language(String original_language) {
            this.original_language = original_language;
            return this;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public Result withOriginal_title(String original_title) {
            this.original_title = original_title;
            return this;
        }

        public List<Integer> getGenre_ids() {
            return genre_ids;
        }

        public void setGenre_ids(List<Integer> genre_ids) {
            this.genre_ids = genre_ids;
        }

        public Result withGenre_ids(List<Integer> genre_ids) {
            this.genre_ids = genre_ids;
            return this;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }

        public Result withBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
            return this;
        }

        public Boolean getAdult() {
            return adult;
        }

        public void setAdult(Boolean adult) {
            this.adult = adult;
        }

        public Result withAdult(Boolean adult) {
            this.adult = adult;
            return this;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public Result withOverview(String overview) {
            this.overview = overview;
            return this;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public Result withRelease_date(String release_date) {
            this.release_date = release_date;
            return this;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(vote_count);
            dest.writeValue(id);
            dest.writeValue(video);
            dest.writeValue(vote_average);
            dest.writeValue(title);
            dest.writeValue(popularity);
            dest.writeValue(poster_path);
            dest.writeValue(original_language);
            dest.writeValue(original_title);
            dest.writeList(genre_ids);
            dest.writeValue(backdrop_path);
            dest.writeValue(adult);
            dest.writeValue(overview);
            dest.writeValue(release_date);
        }

        public int describeContents() {
            return 0;
        }

    }

}