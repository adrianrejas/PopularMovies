package org.udacity.android.arejas.popularmovies.gateways.data.entities.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import org.udacity.android.arejas.popularmovies.gateways.data.entities.Resource;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Class representing a data source for getting info from network in form of paged lists.
 * It must be implemented with the suitable implementation for network calls.
 */
abstract class NetworkListDataSource<RestType, EntityTypeItem>  extends PageKeyedDataSource<Integer, EntityTypeItem> {

    private final MutableLiveData<Resource> resourceState;

    private Integer totalPages;

    public NetworkListDataSource() {
        this.resourceState = new MutableLiveData<>();
        this.resourceState.postValue(Resource.loading(null));
        this.totalPages = null;
    }

    public LiveData<Resource> getResourceState() {
        return resourceState;
    }

    private void changeResourceState(Resource.Status status, Throwable error) {
        Resource newData = resourceState.getValue();
        Objects.requireNonNull(newData).setError(error);
        newData.setStatus(status);
        resourceState.postValue(newData);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull LoadInitialCallback<Integer, EntityTypeItem> callback) {
        resourceState.postValue(Resource.loading(null));
        createPagedRestCall(1).enqueue(new Callback<RestType>() {
            @Override
            public void onResponse(@NonNull Call<RestType> call, @NonNull Response<RestType> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        totalPages = getTotalPagesFromRestApi(response.body());
                        Integer nextPageKey = 2;
                        if ((totalPages != null) && (totalPages <= 1)) nextPageKey = null;
                        callback.onResult(transformRestToEntityList(response.body()), null, nextPageKey);
                        resourceState.postValue(Resource.success(null));
                    }
                } catch (Exception error) {
                    resourceState.postValue(Resource.error(error, null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RestType> call, @NonNull Throwable error) {
                resourceState.postValue(Resource.error(error,null));
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, EntityTypeItem> callback) {}

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, EntityTypeItem> callback) {
        resourceState.postValue(Resource.loading(null));
        Log.d("TEST", "loadAfter: Cargando pagina " + params.key);
        createPagedRestCall(params.key).enqueue(new Callback<RestType>() {
            @Override
            public void onResponse(@NonNull Call<RestType> call, @NonNull Response<RestType> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        totalPages = getTotalPagesFromRestApi(response.body());
                        Integer nextPageKey = params.key + 1;
                        if ((totalPages != null) && (totalPages <= nextPageKey)) nextPageKey = null;
                        callback.onResult(transformRestToEntityList(response.body()), nextPageKey);
                        resourceState.postValue(Resource.success(null));
                    }
                } catch (Exception error) {
                    resourceState.postValue(Resource.error(error, null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RestType> call, @NonNull Throwable error) {
                resourceState.postValue(Resource.error(error,null));
            }
        });
    }

    @MainThread
    protected abstract Call<RestType> createPagedRestCall(Integer page);

    @WorkerThread
    protected abstract List<EntityTypeItem> transformRestToEntityList(RestType result) throws Exception;

    @WorkerThread
    protected abstract Integer getTotalPagesFromRestApi(RestType result) throws Exception;
}
