package org.tyniest.utils;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.UUID;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import lombok.experimental.UtilityClass;

/**
 * This call helps build shorter string representations of uuid4 by encoding it with b64 instead of
 * b16
 */
@UtilityClass
public class UuidHelper {
    private static final Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();

    /**
     * @return a compact string representation of a given uuid4
     */
    public static String uuidToB64(UUID uuid) {
        final var bytes = uuidToBytes(uuid);
        return BASE64_URL_ENCODER.encodeToString(bytes);
    }

    private static byte[] uuidToBytes(UUID uuid) {
        final var bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    /**
     * @return a compact string representation of a random uuid4
     */
    public static String getCompactUUID4() {
        return UuidHelper.uuidToB64(UUID.randomUUID());
    }

    public static UUID timeUUID() {
        return Uuids.timeBased();
    }
}
