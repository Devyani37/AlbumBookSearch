package com.devyani.kramphub.BookAlbumSearch.models;


public final class Book {

    private final String title;
    private final String authors;
    private final ItemType type;

    public Book(String title, String authors) {
        this.title = title;
        this.authors = authors;
        this.type = ItemType.BOOK;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public ItemType getType() {
        return type;
    }
}
