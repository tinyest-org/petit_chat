package org.tyniest.utils.notifier.local;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class IdentityCodec<S> implements MessageCodec<S, Object> {
    private final Class<S> aClass;

    public IdentityCodec(Class<S> aClass) {
        this.aClass = aClass;
    }

    @Override
    public void encodeToWire(Buffer buffer, Object o) {

    }

    @Override
    public S decodeFromWire(int pos, Buffer buffer) {
        return null;
    }

    @Override
    public Object transform(S o) {
        return o;
    }

    @Override
    public String name() {
        System.out.println(aClass.getCanonicalName());
        return aClass.getCanonicalName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
