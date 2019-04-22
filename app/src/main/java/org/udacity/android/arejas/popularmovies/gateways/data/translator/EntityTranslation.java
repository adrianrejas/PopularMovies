package org.udacity.android.arejas.popularmovies.gateways.data.translator;

import android.annotation.SuppressLint;

import org.udacity.android.arejas.popularmovies.data.entities.MovieCreditsItem;
import org.udacity.android.arejas.popularmovies.data.entities.MovieDetails;
import org.udacity.android.arejas.popularmovies.data.entities.MovieListItem;
import org.udacity.android.arejas.popularmovies.data.entities.MovieReviewItem;
import org.udacity.android.arejas.popularmovies.data.entities.MovieVideoItem;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieCreditsRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieDetailsRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieListRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieReviewListRestApi;
import org.udacity.android.arejas.popularmovies.data.network.model.MovieVideoListRestApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
/*
* Class with static methods for translate REST API entities to global entities.
 */
public class EntityTranslation {

    @SuppressWarnings("FieldCanBeLocal")
    private static final String RESTAPI_DATE_FORMAT = "yyyy-MM-dd";

    public static List<MovieListItem> transformMovieListFromRestApiToEntity
            (MovieListRestApi restApiList, String language) {
        List<MovieListItem> list = new ArrayList<>();
        for (MovieListRestApi.Result dbItem : restApiList.getResults()) {
            MovieListItem newItem = new MovieListItem();
            newItem.setId(dbItem.getId());
            newItem.setPosterPath(dbItem.getPoster_path());
            newItem.setTitle(dbItem.getTitle());
            newItem.setVoteAverage(dbItem.getVote_average());
            newItem.setDataLanguage(language);
            list.add(newItem);
        }
        return list;
    }

    @SuppressLint("SimpleDateFormat")
    public static MovieDetails transformMovieDetailsFromRestApiToEntity
            (MovieDetailsRestApi restApiDetails, String language) throws ParseException {
        MovieDetails newDetails = new MovieDetails();
        newDetails.setId(restApiDetails.getId());
        newDetails.setTitle(restApiDetails.getTitle());
        newDetails.setPosterPath(restApiDetails.getPoster_path());
        newDetails.setVoteAverage(restApiDetails.getVote_average());
        newDetails.setAdult(restApiDetails.getAdult());
        newDetails.setBudget(restApiDetails.getBudget());
        newDetails.setRevenue(restApiDetails.getRevenue());
        newDetails.setReleaseDate((new SimpleDateFormat(RESTAPI_DATE_FORMAT)).
                parse(restApiDetails.getRelease_date()));
        newDetails.setHomepage(restApiDetails.getHomepage());
        newDetails.setImdbId(restApiDetails.getImdb_id());
        newDetails.setOriginalTitle(restApiDetails.getOriginal_title());
        newDetails.setStatus(restApiDetails.getStatus());
        newDetails.setSynopsis(restApiDetails.getOverview());
        newDetails.setTagline(restApiDetails.getTagline());
        newDetails.setSavedAsFavorite(false);
        List<String> genres = new ArrayList<>();
        for (MovieDetailsRestApi.Genre genre : restApiDetails.getGenres()) {
            genres.add(genre.getName());
        }
        newDetails.setGenres(genres);
        List<String> productionCompanies = new ArrayList<>();
        for (MovieDetailsRestApi.Production_company company :
                restApiDetails.getProduction_companies()) {
            productionCompanies.add(company.getName());
        }
        newDetails.setProductionCompanies(productionCompanies);
        List<String> productionCountries = new ArrayList<>();
        for (MovieDetailsRestApi.Production_country country :
                restApiDetails.getProduction_countries()) {
            productionCountries.add(country.getName());
        }
        newDetails.setProductionCountries(productionCountries);
        List<String> spokenLanguages = new ArrayList<>();
        for (MovieDetailsRestApi.Spoken_language lang :
                restApiDetails.getSpoken_languages()) {
            spokenLanguages.add(lang.getName());
        }
        newDetails.setSpokenLanguages(spokenLanguages);
        newDetails.setDataLanguage(language);
        return newDetails;
    }

    public static List<MovieCreditsItem> transformMovieCreditsListFromRestApiToEntity
            (MovieCreditsRestApi restApiCredits) {
        List<MovieCreditsItem> list = new ArrayList<>();
        for (MovieCreditsRestApi.Cast castItemRest : restApiCredits.getCast()) {
            MovieCreditsItem castItem = new MovieCreditsItem();
            castItem.setId(null);
            castItem.setName(castItemRest.getName());
            castItem.setCharacter(castItemRest.getCharacter());
            castItem.setOrder(castItemRest.getOrder());
            castItem.setJob(MovieCreditsItem.ACTOR_JOB);
            castItem.setDepartment(MovieCreditsItem.PERFORMANCE_DEPARTMENT);
            castItem.setProfilePath(castItemRest.getProfile_path());
            castItem.setMovieId(restApiCredits.getId());
            list.add(castItem);
        }
        for (MovieCreditsRestApi.Crew crewItemRest : restApiCredits.getCrew()) {
            MovieCreditsItem crewItem = new MovieCreditsItem();
            crewItem.setId(null);
            crewItem.setName(crewItemRest.getName());
            crewItem.setCharacter(null);
            crewItem.setOrder(null);
            crewItem.setJob(crewItemRest.getJob());
            crewItem.setDepartment(crewItemRest.getDepartment());
            crewItem.setProfilePath(crewItemRest.getProfile_path());
            crewItem.setMovieId(restApiCredits.getId());
            list.add(crewItem);
        }
        return list;
    }

    public static List<MovieVideoItem> transformMovieVideoListFromRestApiToEntity
            (MovieVideoListRestApi restApiVideos, String language) {
        List<MovieVideoItem> list = new ArrayList<>();
        for (MovieVideoListRestApi.Result videoItemRest : restApiVideos.getResults()) {
            MovieVideoItem videoItem = new MovieVideoItem();
            videoItem.setLanguageCode(videoItemRest.getLanguageCode());
            videoItem.setCountryCode(videoItemRest.getCountryCode());
            videoItem.setName(videoItemRest.getName());
            videoItem.setKey(videoItemRest.getKey());
            videoItem.setSite(videoItemRest.getSite());
            videoItem.setSize(videoItemRest.getSize());
            videoItem.setMovieId(restApiVideos.getMovieId());
            list.add(videoItem);
        }
        return list;
    }

    public static List<MovieReviewItem> transformMovieReviewListFromRestApiToEntity
            (MovieReviewListRestApi restApiReviews, String language) {
        List<MovieReviewItem> list = new ArrayList<>();
        for (MovieReviewListRestApi.Result reviewItemRest : restApiReviews.getResults()) {
            MovieReviewItem reviewItem = new MovieReviewItem();
            reviewItem.setAuthor(reviewItemRest.getAuthor());
            reviewItem.setContent(reviewItemRest.getContent());
            reviewItem.setUrl(reviewItemRest.getUrl());
            reviewItem.setMovieId(restApiReviews.getMovieId());
            reviewItem.setDataLanguage(language);
            list.add(reviewItem);
        }
        return list;
    }

}
