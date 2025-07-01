package org.dmsextension.paperless.system.cache;

import com.google.common.cache.LoadingCache;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Cache {

    protected static final HashMap<String, ReentrantLock> locks = new HashMap<>();

    protected Cache() {}
}
