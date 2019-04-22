package org.udacity.android.arejas.popularmovies.gateways.data.entities.mixed;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import org.udacity.android.arejas.popularmovies.gateways.data.entities.Resource;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Class helping to create a live data of a resource object containing info which may be got from both
 * database and/or network. It must be implemented with the suitable implementation for both network
 * and database calls, and the decisions of whether to call database, network or save network result on
 * database.
 */
public abstract class MixedBoundResource<EntityType, RestType> {
    private final MediatorLiveData<Resource<EntityType>> result = new MediatorLiveData<>();

    public MixedBoundResource() {
        result.postValue(Resource.loading(null));
        if (shouldRequestToDb()) {
            LiveData<EntityType> dbSource = loadFromDb();
            result.addSource(dbSource, data -> {
                if (shouldRequestToNetwork(data)) {
                    result.removeSource(dbSource);
                    loadFromNetwork(dbSource);
                } else {
                    try {
                        transformDbResult(data);
                        Resource<EntityType> newResource = result.getValue();
                        Objects.requireNonNull(newResource).setStatus(Resource.Status.SUCCESS);
                        newResource.setData(data);
                        result.postValue(newResource);
                    } catch (Exception error) {
                        Resource<EntityType> newResource = result.getValue();
                        Objects.requireNonNull(newResource).setStatus(Resource.Status.ERROR);
                        newResource.setError(error);
                        result.postValue(newResource);
                    }
                }
            });
        } else {
            loadFromNetwork(null);
        }
    }

    private void loadFromNetwork(final LiveData<EntityType> dbSource) {
        if (dbSource != null)  result.addSource(dbSource, newData -> {
            Resource<EntityType> newResource = result.getValue();
            Objects.requireNonNull(newResource).setStatus(Resource.Status.LOADING);
            newResource.setData(newData);
            result.postValue(newResource);
        });
        Call<RestType> call = createNetworkCall();
        if (call != null) {
            call.enqueue(new Callback<RestType>() {
                @Override
                public void onResponse(@NonNull Call<RestType> call, @NonNull Response<RestType> response) {
                    if (dbSource != null)  result.removeSource(dbSource);
                    if (response.body() != null) {
                        try {
                            EntityType newData = transformRestToEntity(response.body());
                            if (shouldSaveToDb(newData)) {
                                saveRestCallResult(newData);
                                if (dbSource != null)  result.addSource(dbSource, data -> {
                                    Resource<EntityType> newResource = result.getValue();
                                    Objects.requireNonNull(newResource).setStatus(Resource.Status.SUCCESS);
                                    newResource.setData(newData);
                                    result.postValue(newResource);
                                });
                            } else {
                                Resource<EntityType> newResource = result.getValue();
                                Objects.requireNonNull(newResource).setStatus(Resource.Status.SUCCESS);
                                newResource.setData(newData);
                                result.postValue(newResource);
                            }
                        } catch (Exception error) {
                            Resource<EntityType> newResource = result.getValue();
                            Objects.requireNonNull(newResource).setStatus(Resource.Status.ERROR);
                            newResource.setError(error);
                            result.postValue(newResource);
                        }
                    } else {
                        result.postValue(Resource.error(new NullPointerException("No result from REST call"), null));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RestType> call, @NonNull Throwable error) {
                    if (dbSource != null) {
                        result.removeSource(dbSource);
                        result.addSource(dbSource, newData -> {
                            Resource<EntityType> newResource = result.getValue();
                            Objects.requireNonNull(newResource).setStatus(Resource.Status.ERROR);
                            newResource.setError(error);
                            newResource.setData(newData);
                            result.postValue(newResource);
                        });
                    } else {
                        Resource<EntityType> newResource = result.getValue();
                        Objects.requireNonNull(newResource).setStatus(Resource.Status.ERROR);
                        newResource.setError(error);
                        result.postValue(newResource);
                    }
                }
            });
        }
    }

    @MainThread
    protected abstract void transformDbResult(@Nullable EntityType data) throws Exception;

    @MainThread
    protected abstract EntityType transformRestToEntity(@NonNull RestType item) throws Exception;

    @MainThread
    protected abstract boolean shouldRequestToDb();

    @MainThread
    protected abstract boolean shouldRequestToNetwork(@Nullable EntityType data);

    @MainThread
    protected abstract boolean shouldSaveToDb(@Nullable EntityType data);

    @WorkerThread
    protected abstract void saveRestCallResult(@Nullable EntityType data);

    @MainThread
    protected abstract LiveData<EntityType> loadFromDb();

    @MainThread
    protected abstract Call<RestType> createNetworkCall();

    public final LiveData<Resource<EntityType>> getAsLiveData() {
        return result;
    }
}
