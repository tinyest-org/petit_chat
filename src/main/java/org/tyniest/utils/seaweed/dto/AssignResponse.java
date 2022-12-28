package org.tyniest.utils.seaweed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class AssignResponse {
    private Long count;
    private String fid;
    private String url; 
    private String publicUrl;
}
