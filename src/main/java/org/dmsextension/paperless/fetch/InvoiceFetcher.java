package org.dmsextension.paperless.fetch;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Types;
import okhttp3.Request;
import okhttp3.Response;
import org.dmsextension.paperless.templates.TDocument;
import org.dmsextension.paperless.templates.TDocumentType;
import org.dmsextension.paperless.templates.TSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;

public class InvoiceFetcher extends Fetcher {

    private final Logger logger = LoggerFactory.getLogger(InvoiceFetcher.class);
    private TSearchResult<TDocument> documents;
    public InvoiceFetcher() {

    }

    public boolean fetchNonKeywordedInvoices() {
        this.logger.debug("Building request to fetch non keyworded invoices");
        Request req = new Request.Builder()
                .url(this.getBaseUrl() + "api/documents/?custom_field_query=[\"Verschlagwortung\",\"exact\",\"Unverschlagwortet\"]")
                .header("Authorization", this.getAuthenticationString())
                .header("Content-Type", "application/json")
                .get()
                .build();

        this.logger.debug("Sending request");
        try (Response r = this.getClient().newCall(req).execute()) {
            if (r.isSuccessful() && r.body() != null) {
                Type type = Types.newParameterizedType(TSearchResult.class, TDocument.class);
                JsonAdapter<TSearchResult<TDocument>> adapter = this.getMoshi().adapter(type);
                this.documents = adapter.fromJson(r.body().string());
                this.logger.debug("Successfully fetched documents");
                this.setFetched(true);
            } else {
                this.logger.info("Request not successful: " + r);
            }
        } catch (Exception ex) {
            this.logger.info("Exception while fetching invoices: " + ex.getMessage());
            return false;
        }
        return this.isFetched();
    }

    public List<TDocument> getFetchedDocuments() {
        return this.isFetched() ? this.documents.getResults() : null;
    }

    public TSearchResult<TDocument> getSearchResult() {
        return this.isFetched() ? this.documents : null;
    }
}
