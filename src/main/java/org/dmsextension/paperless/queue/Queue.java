package org.dmsextension.paperless.queue;

import com.squareup.moshi.Moshi;

abstract class Queue {

    protected static final Moshi moshi = new Moshi.Builder().build();

    protected Queue() { }
}
