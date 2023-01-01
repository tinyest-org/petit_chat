package org.tyniest.utils.seaweed.dto;

import lombok.Value;

@Value
public class UploadResult {
    protected String fid;
    protected String name;
    protected Long size;
    protected String eTag;

    public static UploadResult of(final String fid, final UploadResponse resp) {
        return new UploadResult(fid, resp.name, resp.size, resp.eTag);
    }
}
