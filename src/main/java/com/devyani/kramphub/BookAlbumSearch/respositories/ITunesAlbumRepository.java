package com.devyani.kramphub.BookAlbumSearch.respositories;

import com.devyani.kramphub.BookAlbumSearch.configurations.ApiConfig;
import com.devyani.kramphub.BookAlbumSearch.dtos.upstreams.ItunesAlbumResponse;
import com.devyani.kramphub.BookAlbumSearch.exceptions.UpstreamException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class ITunesAlbumRepository {
    @Autowired
    private ApiConfig apiConfig;

    @Autowired
    private RestTemplate restTemplate;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     *
     * @Timed is used to exposed the metrics on response time for ITune Album Service.
     * Go to this url to see this metrics http://localhost:8080/actuator/prometheus .
     */
    @Timed(description = "Time spent during itunes album api call")
    public ItunesAlbumResponse searchAlbum(String searchTerm) {

        try {
            String url = apiConfig.getiTunesUrl() + "?term=" + searchTerm + "&limit=" + apiConfig.getiTunesMaxResult();
            String body = restTemplate.getForEntity(url, String.class).getBody();

            return objectMapper.readValue(body, ItunesAlbumResponse.class);
        } catch (Exception e) {
            throw new UpstreamException("Failed to parse iTunes Api Response", e);
        }
    }

}
