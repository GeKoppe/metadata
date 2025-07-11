package org.dmsextension.paperless.system.cache;

import org.dmsextension.paperless.templates.TCustomFieldTemplate;
import org.dmsextension.paperless.templates.TDocumentType;
import org.dmsextension.paperless.templates.TTag;
import org.junit.Assert;
import org.junit.Test;

public class TestCaches {

    @Test
    public void testEnvironmentCache() {
        SystemCache.initCaches();
        Assert.assertEquals("sysuser", SystemCache.getEnvironmentCacheValue("PAPERLESS_SYSTEM_USER"));
        Assert.assertEquals("elo", SystemCache.getEnvironmentCacheValue("PAPERLESS_SYSTEM_PW"));
        Assert.assertEquals("ilostthegame", SystemCache.getEnvironmentCacheValue("PAPERLESS_HOST"));
    }

    @Test
    public void testUserCache() {
        SystemCache.initCaches();
        Assert.assertEquals("6341569edb96c92ac3dff09876dbabaedf1f842d", SystemCache.getUserCacheValue("sysuser"));
    }

    @Test
    public void testCustomFieldCache() {
        SystemCache.initCaches();
        PaperlessCache.initCaches();
        TCustomFieldTemplate t = PaperlessCache.getCustomField("1");
        Assert.assertEquals("Rechnungssteller", t.getName());
        Assert.assertEquals("select", t.getDataType());
        Assert.assertEquals("REWE", t.getExtraData().getSelectOptions().getFirst().getLabel());
    }

    @Test
    public void testDocumentTypeCache() {
        SystemCache.initCaches();
        PaperlessCache.initCaches();
        TDocumentType t = PaperlessCache.getDocumentType("1");
        Assert.assertEquals("Rechnungsbeleg", t.getName());
        Assert.assertEquals(1, t.getId());
        Assert.assertTrue(t.getDocumentCount() > 0);
    }

    @Test
    public void testTagCache() {
        SystemCache.initCaches();
        PaperlessCache.initCaches();
        TTag t = PaperlessCache.getTag("1");
        Assert.assertEquals("#ffffff", t.getTextColor());
        Assert.assertEquals("Test", t.getName());
    }
}
