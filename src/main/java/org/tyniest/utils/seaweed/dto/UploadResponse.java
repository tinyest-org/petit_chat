package org.tyniest.utils.seaweed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UploadResponse {
    private String name;
    private Long size;
    private String eTag;
}
