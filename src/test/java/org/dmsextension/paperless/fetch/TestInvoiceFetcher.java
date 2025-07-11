package org.dmsextension.paperless.fetch;

import org.dmsextension.paperless.system.cache.PaperlessCache;
import org.dmsextension.paperless.system.cache.SystemCache;
import org.dmsextension.paperless.templates.TDocument;
import org.dmsextension.paperless.templates.TSearchResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestInvoiceFetcher {

    @BeforeAll
    public static void initCaches() {
        SystemCache.initCaches();
        PaperlessCache.initCaches();
    }
    @Test
    public void TestInvoiceFetching() {
        SystemCache.initCaches();
        InvoiceFetcher f = new InvoiceFetcher();
        f.fetchNonKeywordedInvoices();
        TSearchResult<TDocument> result = f.getSearchResult();
        Assertions.assertTrue(result.getCount() > 0);
        List<TDocument> docs = f.getFetchedDocuments();
        Assertions.assertFalse(docs.isEmpty());
    }
}
