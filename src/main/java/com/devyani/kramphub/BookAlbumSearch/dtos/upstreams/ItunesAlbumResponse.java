package com.devyani.kramphub.BookAlbumSearch.dtos.upstreams;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class ItunesAlbumResponse {
    @JsonProperty("results")
    private List<Map<String,String>> results;

    public List<Map<String, String>> getResults() {
        return results;
    }

    public void setResults(List<Map<String, String>> results) {
        this.results = results;
    }
}
