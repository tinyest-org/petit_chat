package org.tyniest.utils.seaweed.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class LookupResponse {
    private String volumeId;
    private List<Location> locations;

    @Getter
    @AllArgsConstructor
    @Setter
    public static class Location {
        private String publicUrl;
        private String url;
    }
}
