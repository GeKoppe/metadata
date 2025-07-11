package org.dmsextension.paperless.fetch;

import org.dmsextension.paperless.system.cache.SystemCache;
import org.dmsextension.paperless.templates.TDocument;
import org.dmsextension.paperless.templates.TSearchResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestInvoiceFetcher {

    @Test
    public void TestInvoiceFetching() {
        SystemCache.initCaches();
        InvoiceFetcher f = new InvoiceFetcher();
        f.fetchNonKeywordedInvoices();
        TSearchResult<TDocument> result = f.getSearchResult();
        Assert.assertTrue(result.getCount() > 0);
        List<TDocument> docs = f.getFetchedDocuments();
        Assert.assertFalse(docs.isEmpty());
    }
}
