package org.tyniest.utils.seaweed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadResponse {
    protected String name;
    protected Long size;
    protected String eTag;
}
