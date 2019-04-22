package org.udacity.android.arejas.popularmovies.gateways.data.entities.network;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import java.util.List;

import retrofit2.Call;

/*
 * Class helping to create a data source for paged list got from network.
 * It must be implemented with the suitable implementation for network calls.
 */
@SuppressWarnings("unchecked")
abstract class NetworkListDataSourceFactory<RestType, EntityTypeItem> extends DataSource.Factory<Integer, EntityTypeItem> {

    private final MutableLiveData<NetworkListDataSource> mutableLiveDataSource;

    public NetworkListDataSourceFactory() {
        this.mutableLiveDataSource = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        NetworkListDataSource networkListDataSource = new NetworkListDataSource<RestType, EntityTypeItem>() {
            @Override
            public Call<RestType> createPagedRestCall(Integer page) {
                return createPagedRestCallForDataSource(page);
            }

            @Override
            public List<EntityTypeItem> transformRestToEntityList(RestType result) throws Exception {
                return transformRestToEntityListForDataSource(result);
            }

            @Override
            public Integer getTotalPagesFromRestApi(RestType result) throws Exception {
                return getTotalPagesFromRestApiForDataSource(result);
            }
        };
        mutableLiveDataSource.postValue(networkListDataSource);
        return networkListDataSource;
    }

    public MutableLiveData<NetworkListDataSource> getMutableLiveDataSource() {
        return mutableLiveDataSource;
    }

    @MainThread
    protected abstract Call<RestType> createPagedRestCallForDataSource(Integer page);

    @WorkerThread
    protected abstract List<EntityTypeItem> transformRestToEntityListForDataSource(RestType result) throws Exception;

    @WorkerThread
    protected abstract Integer getTotalPagesFromRestApiForDataSource(RestType result) throws Exception;

}
