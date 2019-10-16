package com.devyani.kramphub.BookAlbumSearch.respositories;

import com.devyani.kramphub.BookAlbumSearch.configurations.ApiConfig;
import com.devyani.kramphub.BookAlbumSearch.dtos.upstreams.GoogleBooksResponse;
import com.devyani.kramphub.BookAlbumSearch.exceptions.UpstreamException;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class GoogleBookRepository {

    @Autowired
    private ApiConfig apiConfig;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * @Timed is used to exposed the metrics on response time for Google Book Services.
     * Go to this url to see this metrics http://localhost:8080/actuator/prometheus .
     */
    @Timed(description = "Time spent during google book api call")
    public GoogleBooksResponse searchBook(String searchText){
        try {
            String url = apiConfig.getGoogleApiUrl() + "?q=" + searchText + "&maxResults=" + apiConfig.getGoogleMaxResult() + "&fields=items(volumeInfo(title,authors))";
            return restTemplate.getForEntity(url, GoogleBooksResponse.class).getBody();
        }catch (Exception e){
            throw new UpstreamException("Google Book Upstream exception", e);
        }
    }
}
