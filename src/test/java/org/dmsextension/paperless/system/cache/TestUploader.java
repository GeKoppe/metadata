package org.dmsextension.paperless.system.cache;

import org.dmsextension.paperless.upload.Uploader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TestUploader {

    /**
    @Test
    public void testHandlebars() {
        SystemCache.initCaches();
        PaperlessCache.initCaches();
        Map<String, Object> context = new HashMap<>();
        context.put("Rechnungssteller", "REWE");
        context.put("Belegdatum", "2025-07-14");
        context.put("Rechnungsnummer", "TEST-1234567");
        context.put("document_type", 1);
        Uploader u = new Uploader();
        String templated = u.parseDocumentName(context);
        Assertions.assertEquals("Rechnung TEST-1234 - REWE - 2025-07-14", templated);
        context = new HashMap<>();
        context.put("Firma", "SIEVERS");
        context.put("Ausstellungsdatum", "2025-07-14");
        context.put("Rechnungsnummer", "TEST-1234567");
        context.put("document_type", 4);
        templated = u.parseDocumentName(context);
        Assertions.assertEquals("Entgeltabrechnung SIEVERS - 2025-07-14", templated);
    }*/
}
