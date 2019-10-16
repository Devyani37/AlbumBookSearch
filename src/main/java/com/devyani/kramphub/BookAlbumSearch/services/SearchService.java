package com.devyani.kramphub.BookAlbumSearch.services;

import com.devyani.kramphub.BookAlbumSearch.configurations.ApiConfig;
import com.devyani.kramphub.BookAlbumSearch.dtos.downstreams.Result;
import com.devyani.kramphub.BookAlbumSearch.dtos.upstreams.GoogleBooksResponse;
import com.devyani.kramphub.BookAlbumSearch.dtos.upstreams.ItunesAlbumResponse;
import com.devyani.kramphub.BookAlbumSearch.exceptions.UpstreamException;
import com.devyani.kramphub.BookAlbumSearch.models.Album;
import com.devyani.kramphub.BookAlbumSearch.models.Book;
import com.devyani.kramphub.BookAlbumSearch.models.ItemType;
import com.devyani.kramphub.BookAlbumSearch.respositories.GoogleBookRepository;
import com.devyani.kramphub.BookAlbumSearch.respositories.ITunesAlbumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class SearchService {

    @Autowired
    private GoogleBookRepository googleBookRepository;
    @Autowired
    private ITunesAlbumRepository iTunesAlbumRepository;
    @Autowired
    private ApiConfig apiConfig;

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    public List<Result> search(String searchText) {

        if (StringUtils.isEmpty(searchText)) {
            throw new IllegalArgumentException("Search parameter should not be null or empty");
        }

        /**
         * Logic to respond our service within a minute.
         */

        CompletableFuture<List<Book>> bookSearchFuture = CompletableFuture.supplyAsync(new Supplier<List<Book>>() {
            @Override
            public List<Book> get() {
                return searchGoogleBooks(searchText);
            }
        }).orTimeout(apiConfig.getServiceTimeout(), TimeUnit.MILLISECONDS);

        CompletableFuture<List<Album>> iTunesSearchFuture = CompletableFuture.supplyAsync(new Supplier<List<Album>>() {
            @Override
            public List<Album> get() {
                return searchITunes(searchText);
            }
        }).orTimeout(apiConfig.getServiceTimeout(), TimeUnit.MILLISECONDS);


        List<Book> bookList = null;
        try {
            bookList = bookSearchFuture.get();
        } catch (Exception e) {
            log.error("Exception Occurred During Google Book Call. Exception {0}", e);
        }

        List<Album> albumList = null;
        try {
            albumList = iTunesSearchFuture.get();
        } catch (Exception e) {
            log.error("Exception Occurred During iTunes Album Call. Exception {0}", e);
        }

        List<Result> resultList = new ArrayList<>();

        if (albumList != null) {
            //Creating Result for Albums
            for (Album album : albumList) {
                Result result = new Result();
                result.setTitle(album.getTitle());
                result.setArtists(album.getArtists());
                result.setType(ItemType.ALBUM);
                resultList.add(result);
            }
        }


        if (bookList != null) {
            //Creating Result for Books
            for (Book book : bookList) {
                Result result = new Result();
                result.setTitle(book.getTitle());
                result.setAuthors(book.getAuthors());
                result.setType(ItemType.BOOK);
                resultList.add(result);
            }
        }

        //Sorted By Title
        resultList.sort((r1, r2) -> r1.getTitle().compareToIgnoreCase(r2.getTitle()));

        return resultList;
    }

    private List<Book> searchGoogleBooks(String searchText) {

        GoogleBooksResponse googleBooksResponse = null;
        try {
            googleBooksResponse = googleBookRepository.searchBook(searchText);
        } catch (UpstreamException e) {
            log.error("Upstream Error Occurred. Error {0}", e);
        }

        //Creating model
        List<Book> bookList = new ArrayList<>();
        if (googleBooksResponse == null) {
            return bookList;
        }
        for (GoogleBooksResponse.BookInfo bookInfo : googleBooksResponse.getBooksInfo()) {
            GoogleBooksResponse.Book book = bookInfo.getBook();
            String authors = null;
            if (book.getAuthors() != null) {
                authors = String.join(",", book.getAuthors());
            }
            Book item = new Book(book.getTitle(), authors);
            bookList.add(item);
        }

        return bookList;
    }

    private List<Album> searchITunes(String searchText) {
        ItunesAlbumResponse itunesAlbumResponse = null;
        try {
            itunesAlbumResponse = iTunesAlbumRepository.searchAlbum(searchText);
        } catch (UpstreamException e) {
            log.error("Upstream Error Occurred. Error {0} ", e);
        }

        List<Album> albumList = new ArrayList<>();
        if (itunesAlbumResponse == null) {
            return albumList;
        }
        for (Map<String, String> albumData : itunesAlbumResponse.getResults()) {
            Album album = new Album(albumData.get("trackName"), albumData.get("artistName"));
            albumList.add(album);
        }

        return albumList;
    }
}
