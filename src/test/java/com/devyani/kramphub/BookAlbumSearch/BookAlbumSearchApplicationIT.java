package com.devyani.kramphub.BookAlbumSearch;

import com.devyani.kramphub.BookAlbumSearch.configurations.ApiConfig;
import com.devyani.kramphub.BookAlbumSearch.dtos.downstreams.Result;
import com.devyani.kramphub.BookAlbumSearch.dtos.upstreams.GoogleBooksResponse;
import com.devyani.kramphub.BookAlbumSearch.dtos.upstreams.ItunesAlbumResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookAlbumSearchApplicationIT {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private MockRestServiceServer server;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ApiConfig apiConfig;

    @Before
    public void init() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void searchShouldReturnBook_AlbumResponse() throws JsonProcessingException {
        mockGoogleBookRequest(HttpStatus.OK, "Java", objectMapper.writeValueAsString(mockedGoogleResponse()));
        mockITunesRequest("Java", objectMapper.writeValueAsString(mockedITuneResponse()));
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        ResponseEntity<List<Result>> responseEntity = testRestTemplate.exchange("/search?q=Java", HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<Result>>() {
        });
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(2, responseEntity.getBody().size());
        Assert.assertEquals("Java Book", responseEntity.getBody().get(0).getTitle());

    }

    @Test
    public void searchShouldReturnAlbumIfBookFailed() throws JsonProcessingException {
        mockGoogleBookRequest(HttpStatus.BAD_GATEWAY, "Java", objectMapper.writeValueAsString(mockedGoogleResponse()));
        mockITunesRequest("Java", objectMapper.writeValueAsString(mockedITuneResponse()));
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        ResponseEntity<List<Result>> responseEntity = testRestTemplate.exchange("/search?q=Java", HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<Result>>() {
        });
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(1, responseEntity.getBody().size());
        Assert.assertEquals("yellow", responseEntity.getBody().get(0).getTitle());

    }

    private void mockGoogleBookRequest(HttpStatus httpStatus, String searchText, String responseBody) {
        String url = apiConfig.getGoogleApiUrl() + "?q=" + searchText + "&maxResults=" + apiConfig.getGoogleMaxResult() + "&fields=items(volumeInfo(title,authors))";
        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(httpStatus).body(responseBody).contentType(MediaType.APPLICATION_JSON));
    }

    private GoogleBooksResponse mockedGoogleResponse() {
        GoogleBooksResponse.Book book = new GoogleBooksResponse.Book();
        book.setTitle("Java Book");
        List<String> authors = new ArrayList<>();
        authors.add("Devyani");
        book.setAuthors(authors);
        GoogleBooksResponse.BookInfo bookInfo = new GoogleBooksResponse.BookInfo();
        bookInfo.setBook(book);
        GoogleBooksResponse googleBooksResponse = new GoogleBooksResponse();
        List<GoogleBooksResponse.BookInfo> booksInfo = new ArrayList<>();
        booksInfo.add(bookInfo);

        googleBooksResponse.setBooksInfo(booksInfo);

        return googleBooksResponse;
    }

    private void mockITunesRequest(String searchTerm, String responseBody) {
        String url = apiConfig.getiTunesUrl() + "?term=" + searchTerm + "&limit=" + apiConfig.getiTunesMaxResult();
        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).body(responseBody).contentType(MediaType.TEXT_PLAIN));
    }

    private ItunesAlbumResponse mockedITuneResponse() {
        //albumData.get("trackName"), albumData.get("artistName")
        ItunesAlbumResponse itunesAlbumResponse = new ItunesAlbumResponse();
        Map<String, String> map = new HashMap<>();
        map.put("trackName", "yellow");
        map.put("artistName", "Cold Play");
        List<Map<String, String>> listOfMaps = new ArrayList<>();
        listOfMaps.add(map);
        itunesAlbumResponse.setResults(listOfMaps);
        return itunesAlbumResponse;
    }

}
