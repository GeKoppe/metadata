package org.dmsextension.paperless.fetch;

import com.squareup.moshi.Moshi;
import org.dmsextension.paperless.communication.ApiCommunicator;
import org.dmsextension.paperless.system.cache.SystemCache;

abstract class Fetcher extends ApiCommunicator {
    private boolean fetched = false;
    protected Fetcher() {
        super();
    }

    public boolean isFetched() {
        return fetched;
    }

    public void setFetched(boolean fetched) {
        this.fetched = fetched;
    }
}
