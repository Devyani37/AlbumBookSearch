package com.devyani.kramphub.BookAlbumSearch.dtos.upstreams;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class GoogleBooksResponse {
    @JsonProperty("items")
    private List<BookInfo> booksInfo;

    public List<BookInfo> getBooksInfo() {
        return booksInfo;
    }

    public void setBooksInfo(List<BookInfo> booksInfo) {
        this.booksInfo = booksInfo;
    }

    public static class BookInfo {
        @JsonProperty("volumeInfo")
        private Book book;

        public Book getBook() {
            return book;
        }

        public void setBook(Book book) {
            this.book = book;
        }
    }

    public static class Book {

        @JsonProperty("title")
        private String title;
        @JsonProperty("authors")
        private List<String> authors;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getAuthors() {
            return authors;
        }

        public void setAuthors(List<String> authors) {
            this.authors = authors;
        }
    }
}