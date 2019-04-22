package org.udacity.android.arejas.popularmovies.gateways.data.entities.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.MainThread;

import org.udacity.android.arejas.popularmovies.gateways.data.entities.Resource;

import java.util.Objects;
import java.util.concurrent.Executor;

/*
* Class helping to create a live data of a resource object containing a paged list got from database.
* It must be implemented with the suitable implementation for database calls.
 */
public abstract class DbPagedListResource<KeyType, EntityTypeItem> {

    private final MediatorLiveData<Resource<PagedList<EntityTypeItem>>> resultLD;

    public DbPagedListResource(Integer resultsPerPage, Executor executor) {
        DataSource.Factory<KeyType, EntityTypeItem> dataFactory = getDbDataSource();
        //noinspection unchecked
        LiveData<PagedList<EntityTypeItem>> dataListLD =
                new LivePagedListBuilder(dataFactory, resultsPerPage).
                        setFetchExecutor(executor)
                        .build();
        resultLD = new MediatorLiveData<>();
        resultLD.setValue(Resource.loading(null));
        resultLD.addSource(dataListLD, items -> {
            Resource<PagedList<EntityTypeItem>> newData = resultLD.getValue();
            try {
                transformResult(items);
                Objects.requireNonNull(newData).setData(items);
                if (checkIfSuccess(items)) {
                    newData.setStatus(Resource.Status.SUCCESS);
                } else {
                    newData.setStatus(Resource.Status.ERROR);
                    newData.setError(new VerifyError("There was a problem with the DB collection"));
                }
            } catch (Exception error) {
                Objects.requireNonNull(newData).setStatus(Resource.Status.ERROR);
                newData.setError(error);
            }
            resultLD.setValue(newData);
        });
    }

    public LiveData<Resource<PagedList<EntityTypeItem>>> getLiveData() {
        return resultLD;
    }

    @MainThread
    protected abstract DataSource.Factory<KeyType, EntityTypeItem> getDbDataSource();

    @MainThread
    protected abstract void transformResult(PagedList<EntityTypeItem> result) throws Exception;

    @MainThread
    protected abstract boolean checkIfSuccess(PagedList<EntityTypeItem> result) throws Exception;

}
