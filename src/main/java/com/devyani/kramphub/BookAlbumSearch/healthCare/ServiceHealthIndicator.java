package com.devyani.kramphub.BookAlbumSearch.healthCare;

import com.devyani.kramphub.BookAlbumSearch.dtos.upstreams.GoogleBooksResponse;
import com.devyani.kramphub.BookAlbumSearch.dtos.upstreams.ItunesAlbumResponse;
import com.devyani.kramphub.BookAlbumSearch.exceptions.UpstreamException;
import com.devyani.kramphub.BookAlbumSearch.respositories.GoogleBookRepository;
import com.devyani.kramphub.BookAlbumSearch.respositories.ITunesAlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ServiceHealthIndicator implements HealthIndicator {

    @Autowired
    private GoogleBookRepository googleBookRepository;
    @Autowired
    private ITunesAlbumRepository iTunesAlbumRepository;

    @Override
    public Health health() {
        try {
            GoogleBooksResponse googleBooksResponse = null;
            ItunesAlbumResponse itunesAlbumResponse = null;
            try {
                googleBooksResponse = googleBookRepository.searchBook("Java");
                itunesAlbumResponse = iTunesAlbumRepository.searchAlbum("Java");
            } catch (UpstreamException e) {
                e.printStackTrace();
            }
            if(googleBooksResponse == null && itunesAlbumResponse == null){
                return Health.down().withDetail("Error","Google Book and ITune Services both are down").build();

            }

            if (googleBooksResponse == null) {
                return Health.down().withDetail("Error", "Google Book Service is unavailable").build();
            }
            if (itunesAlbumResponse == null) {
                return Health.down().withDetail("Error", "ITune Service is unavailable").build();
            }


            return Health.up().withDetail("Success","Google Book and ITune Services both are running").build();

        } catch (Exception e) {
            return Health.down().withDetail("Error", "Service Unavailable").build();

        }

    }
}

