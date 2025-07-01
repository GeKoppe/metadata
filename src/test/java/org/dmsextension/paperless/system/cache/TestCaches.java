package org.dmsextension.paperless.system.cache;

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
}
