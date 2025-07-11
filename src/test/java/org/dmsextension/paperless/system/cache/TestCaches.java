package org.dmsextension.paperless.system.cache;

import org.dmsextension.paperless.templates.TCustomFieldTemplate;
import org.dmsextension.paperless.templates.TDocumentType;
import org.dmsextension.paperless.templates.TTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestCaches {

    @BeforeAll
    public static void initCaches() {
        SystemCache.initCaches();
        PaperlessCache.initCaches();
    }
    @Test
    public void testEnvironmentCache() {
        Assertions.assertEquals("sysuser", SystemCache.getEnvironmentCacheValue("PAPERLESS_SYSTEM_USER"));
        Assertions.assertEquals("elo", SystemCache.getEnvironmentCacheValue("PAPERLESS_SYSTEM_PW"));
        Assertions.assertEquals("ilostthegame", SystemCache.getEnvironmentCacheValue("PAPERLESS_HOST"));
    }

    @Test
    public void testUserCache() {
        Assertions.assertEquals("6341569edb96c92ac3dff09876dbabaedf1f842d", SystemCache.getUserCacheValue("sysuser"));
    }

    @Test
    public void testCustomFieldCache() {
        TCustomFieldTemplate t = PaperlessCache.getCustomField("1");
        Assertions.assertEquals("Rechnungssteller", t.getName());
        Assertions.assertEquals("select", t.getDataType());
        Assertions.assertEquals("REWE", t.getExtraData().getSelectOptions().getFirst().getLabel());
    }

    @Test
    public void testDocumentTypeCache() {
        TDocumentType t = PaperlessCache.getDocumentType("1");
        Assertions.assertEquals("Rechnungsbeleg", t.getName());
        Assertions.assertEquals(1, t.getId());
        Assertions.assertTrue(t.getDocumentCount() > 0);
    }

    @Test
    public void testTagCache() {
        TTag t = PaperlessCache.getTag("1");
        Assertions.assertEquals("#ffffff", t.getTextColor());
        Assertions.assertEquals("Test", t.getName());
    }
}
