package org.udacity.android.arejas.popularmovies.gateways.data.entities.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import org.udacity.android.arejas.popularmovies.gateways.data.entities.Resource;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import retrofit2.Call;

/*
 * Class helping to create a live data of a resource object containing a paged list got from network.
 * It must be implemented with the suitable implementation for network calls.
 */
public abstract class NetworkPagedListResource<RestType, EntityTypeItem> {

    private final MediatorLiveData<Resource<PagedList<EntityTypeItem>>> resultLD;

    public NetworkPagedListResource(Integer resultsPerPage, Executor executor) {
        Resource dataState = Resource.loading(null);
        NetworkListDataSourceFactory<RestType, EntityTypeItem> dataFactory =
                new NetworkListDataSourceFactory<RestType, EntityTypeItem>() {
                    @Override
                    public Call<RestType> createPagedRestCallForDataSource(Integer page) {
                        return createPagedRestCall(page);
                    }

                    @Override
                    public List<EntityTypeItem> transformRestToEntityListForDataSource(
                            RestType result) throws Exception {
                        return transformRestToEntityList(result);
                    }

                    @Override
                    public Integer getTotalPagesFromRestApiForDataSource(
                            RestType result) throws Exception {
                        return getTotalPagesFromRestApi(result);
                    }
                };
        LiveData<PagedList<EntityTypeItem>> dataListLD =
                new LivePagedListBuilder<>(dataFactory, resultsPerPage)
                        .setFetchExecutor(executor)
                        .build();

        LiveData<Resource> dataStateLD = Transformations.switchMap(dataFactory.getMutableLiveDataSource(),
                NetworkListDataSource::getResourceState);
        resultLD = new MediatorLiveData<>();
        resultLD.setValue(Resource.loading(null));
        resultLD.addSource(dataListLD, items -> {
            Resource<PagedList<EntityTypeItem>> newData = resultLD.getValue();
            Objects.requireNonNull(newData).setData(items);
            resultLD.setValue(newData);
        });
        resultLD.addSource(dataStateLD, resource -> {
            Resource<PagedList<EntityTypeItem>> newData = resultLD.getValue();
            Objects.requireNonNull(newData).setStatus(Objects.requireNonNull(resource).getStatus());
            newData.setError(resource.getError());
            resultLD.setValue(newData);
        });
    }

    public LiveData<Resource<PagedList<EntityTypeItem>>> getLiveData() {
        return resultLD;
    }

    @MainThread
    protected abstract Call<RestType> createPagedRestCall(Integer page);

    @WorkerThread
    protected abstract List<EntityTypeItem> transformRestToEntityList(RestType result) throws Exception;

    @WorkerThread
    protected abstract Integer getTotalPagesFromRestApi(RestType result) throws Exception;

}
