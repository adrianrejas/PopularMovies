package org.udacity.android.arejas.popularmovies.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class created with the help of http://www.jsonschema2pojo.org/ online application. It represents
 * the details of a movie. It also contains the annotations for defining entities and columns for
 * allocating MovieDetailsRestApi objects into databases through room persistence library.
 */
public class MovieDetailsRestApi extends MovieElementRestApi implements Parcelable
{

    private Boolean adult;
    private String backdrop_path;
    private Object belongs_to_collection;
    private Integer budget;
    private List<Genre> genres = null;
    private String homepage;
    private Integer id;
    private String imdb_id;
    private String original_language;
    private String original_title;
    private String overview;
    private Float popularity;
    private String poster_path;
    private List<Production_company> production_companies = null;
    private List<Production_country> production_countries = null;
    private String release_date;
    private Integer revenue;
    private Integer runtime;
    private List<Spoken_language> spoken_languages = null;
    private String status;
    private String tagline;
    private String title;
    private Boolean video;
    private Float vote_average;
    private Integer vote_count;
    public final static Creator<MovieDetailsRestApi> CREATOR = new Creator<MovieDetailsRestApi>() {

        public MovieDetailsRestApi createFromParcel(Parcel in) {
            return new MovieDetailsRestApi(in);
        }

        public MovieDetailsRestApi[] newArray(int size) {
            return (new MovieDetailsRestApi[size]);
        }

    }
            ;

    private MovieDetailsRestApi(Parcel in) {
        this.adult = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.backdrop_path = ((String) in.readValue((String.class.getClassLoader())));
        this.belongs_to_collection = in.readValue((Object.class.getClassLoader()));
        this.budget = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.genres, (MovieDetailsRestApi.Genre.class.getClassLoader()));
        this.homepage = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.imdb_id = ((String) in.readValue((String.class.getClassLoader())));
        this.original_language = ((String) in.readValue((String.class.getClassLoader())));
        this.original_title = ((String) in.readValue((String.class.getClassLoader())));
        this.overview = ((String) in.readValue((String.class.getClassLoader())));
        this.popularity = ((Float) in.readValue((Float.class.getClassLoader())));
        this.poster_path = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.production_companies, (MovieDetailsRestApi.Production_company.class.getClassLoader()));
        in.readList(this.production_countries, (MovieDetailsRestApi.Production_country.class.getClassLoader()));
        this.release_date = ((String) in.readValue((String.class.getClassLoader())));
        this.revenue = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.runtime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.spoken_languages, (MovieDetailsRestApi.Spoken_language.class.getClassLoader()));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.tagline = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.video = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.vote_average = ((Float) in.readValue((Float.class.getClassLoader())));
        this.vote_count = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public MovieDetailsRestApi() {
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public MovieDetailsRestApi withAdult(Boolean adult) {
        this.adult = adult;
        return this;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public MovieDetailsRestApi withBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
        return this;
    }

    public Object getBelongs_to_collection() {
        return belongs_to_collection;
    }

    public void setBelongs_to_collection(Object belongs_to_collection) {
        this.belongs_to_collection = belongs_to_collection;
    }

    public MovieDetailsRestApi withBelongs_to_collection(Object belongs_to_collection) {
        this.belongs_to_collection = belongs_to_collection;
        return this;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public MovieDetailsRestApi withBudget(Integer budget) {
        this.budget = budget;
        return this;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public MovieDetailsRestApi withGenres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public String getGenresString() {
        List<String> genreNames = new ArrayList<>();
        for (Genre genre : this.genres) {
            genreNames.add(genre.name);
        }
        return TextUtils.join(", ", genreNames);
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public MovieDetailsRestApi withHomepage(String homepage) {
        this.homepage = homepage;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MovieDetailsRestApi withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public MovieDetailsRestApi withImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
        return this;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public MovieDetailsRestApi withOriginal_language(String original_language) {
        this.original_language = original_language;
        return this;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public MovieDetailsRestApi withOriginal_title(String original_title) {
        this.original_title = original_title;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public MovieDetailsRestApi withOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public Float getPopularity() {
        return popularity;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public MovieDetailsRestApi withPopularity(Float popularity) {
        this.popularity = popularity;
        return this;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public MovieDetailsRestApi withPoster_path(String poster_path) {
        this.poster_path = poster_path;
        return this;
    }

    public List<Production_company> getProduction_companies() {
        return production_companies;
    }

    public void setProduction_companies(List<Production_company> production_companies) {
        this.production_companies = production_companies;
    }

    public MovieDetailsRestApi withProduction_companies(List<Production_company> production_companies) {
        this.production_companies = production_companies;
        return this;
    }

    public List<Production_country> getProduction_countries() {
        return production_countries;
    }

    public void setProduction_countries(List<Production_country> production_countries) {
        this.production_countries = production_countries;
    }

    public MovieDetailsRestApi withProduction_countries(List<Production_country> production_countries) {
        this.production_countries = production_countries;
        return this;
    }

    public String getProduction_countriesString() {
        List<String> countriesNames = new ArrayList<>();
        for (Production_country country : this.production_countries) {
            countriesNames.add(country.name);
        }
        return TextUtils.join(", ", countriesNames);
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public MovieDetailsRestApi withRelease_date(String release_date) {
        this.release_date = release_date;
        return this;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public MovieDetailsRestApi withRevenue(Integer revenue) {
        this.revenue = revenue;
        return this;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public MovieDetailsRestApi withRuntime(Integer runtime) {
        this.runtime = runtime;
        return this;
    }

    public List<Spoken_language> getSpoken_languages() {
        return spoken_languages;
    }

    public void setSpoken_languages(List<Spoken_language> spoken_languages) {
        this.spoken_languages = spoken_languages;
    }

    public MovieDetailsRestApi withSpoken_languages(List<Spoken_language> spoken_languages) {
        this.spoken_languages = spoken_languages;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MovieDetailsRestApi withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public MovieDetailsRestApi withTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieDetailsRestApi withTitle(String title) {
        this.title = title;
        return this;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public MovieDetailsRestApi withVideo(Boolean video) {
        this.video = video;
        return this;
    }

    public Float getVote_average() {
        return vote_average;
    }

    public void setVote_average(Float vote_average) {
        this.vote_average = vote_average;
    }

    public MovieDetailsRestApi withVote_average(Float vote_average) {
        this.vote_average = vote_average;
        return this;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    public MovieDetailsRestApi withVote_count(Integer vote_count) {
        this.vote_count = vote_count;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(adult);
        dest.writeValue(backdrop_path);
        dest.writeValue(belongs_to_collection);
        dest.writeValue(budget);
        dest.writeList(genres);
        dest.writeValue(homepage);
        dest.writeValue(id);
        dest.writeValue(imdb_id);
        dest.writeValue(original_language);
        dest.writeValue(original_title);
        dest.writeValue(overview);
        dest.writeValue(popularity);
        dest.writeValue(poster_path);
        dest.writeList(production_companies);
        dest.writeList(production_countries);
        dest.writeValue(release_date);
        dest.writeValue(revenue);
        dest.writeValue(runtime);
        dest.writeList(spoken_languages);
        dest.writeValue(status);
        dest.writeValue(tagline);
        dest.writeValue(title);
        dest.writeValue(video);
        dest.writeValue(vote_average);
        dest.writeValue(vote_count);
    }

    public int describeContents() {
        return 0;
    }

    public static class Genre implements Parcelable
    {

        private Integer id;
        private String name;
        public final static Creator<Genre> CREATOR = new Creator<Genre>() {

            public Genre createFromParcel(Parcel in) {
                return new Genre(in);
            }

            public Genre[] newArray(int size) {
                return (new Genre[size]);
            }

        }
                ;

        Genre(Parcel in) {
            this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
        }

        Genre() {
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Genre withId(Integer id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Genre withName(String name) {
            this.name = name;
            return this;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(id);
            dest.writeValue(name);
        }

        public int describeContents() {
            return 0;
        }

    }

    public static class Production_company implements Parcelable
    {

        private Integer id;
        private String logo_path;
        private String name;
        private String origin_country;
        public final static Creator<Production_company> CREATOR = new Creator<Production_company>() {

            public Production_company createFromParcel(Parcel in) {
                return new Production_company(in);
            }

            public Production_company[] newArray(int size) {
                return (new Production_company[size]);
            }

        }
                ;

        Production_company(Parcel in) {
            this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.logo_path = ((String) in.readValue((String.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.origin_country = ((String) in.readValue((String.class.getClassLoader())));
        }

        Production_company() {
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Production_company withId(Integer id) {
            this.id = id;
            return this;
        }

        public String getLogo_path() {
            return logo_path;
        }

        public void setLogo_path(String logo_path) {
            this.logo_path = logo_path;
        }

        public Production_company withLogo_path(String logo_path) {
            this.logo_path = logo_path;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Production_company withName(String name) {
            this.name = name;
            return this;
        }

        public String getOrigin_country() {
            return origin_country;
        }

        public void setOrigin_country(String origin_country) {
            this.origin_country = origin_country;
        }

        public Production_company withOrigin_country(String origin_country) {
            this.origin_country = origin_country;
            return this;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(id);
            dest.writeValue(logo_path);
            dest.writeValue(name);
            dest.writeValue(origin_country);
        }

        public int describeContents() {
            return 0;
        }

    }

    public static class Production_country implements Parcelable
    {

        private String iso_3166_1;
        private String name;
        public final static Creator<Production_country> CREATOR = new Creator<Production_country>() {

            public Production_country createFromParcel(Parcel in) {
                return new Production_country(in);
            }

            public Production_country[] newArray(int size) {
                return (new Production_country[size]);
            }

        }
                ;

        Production_country(Parcel in) {
            this.iso_3166_1 = ((String) in.readValue((String.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
        }

        Production_country() {
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public void setIso_3166_1(String iso_3166_1) {
            this.iso_3166_1 = iso_3166_1;
        }

        public Production_country withIso_3166_1(String iso_3166_1) {
            this.iso_3166_1 = iso_3166_1;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Production_country withName(String name) {
            this.name = name;
            return this;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(iso_3166_1);
            dest.writeValue(name);
        }

        public int describeContents() {
            return 0;
        }

    }

    public static class Spoken_language implements Parcelable
    {

        private String iso_639_1;
        private String name;
        public final static Creator<Spoken_language> CREATOR = new Creator<Spoken_language>() {

            public Spoken_language createFromParcel(Parcel in) {
                return new Spoken_language(in);
            }

            public Spoken_language[] newArray(int size) {
                return (new Spoken_language[size]);
            }

        }
                ;

        Spoken_language(Parcel in) {
            this.iso_639_1 = ((String) in.readValue((String.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
        }

        Spoken_language() {
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public void setIso_639_1(String iso_639_1) {
            this.iso_639_1 = iso_639_1;
        }

        public Spoken_language withIso_639_1(String iso_639_1) {
            this.iso_639_1 = iso_639_1;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Spoken_language withName(String name) {
            this.name = name;
            return this;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(iso_639_1);
            dest.writeValue(name);
        }

        public int describeContents() {
            return 0;
        }
    }

}