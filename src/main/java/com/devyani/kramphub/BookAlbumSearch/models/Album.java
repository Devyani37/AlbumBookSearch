package com.devyani.kramphub.BookAlbumSearch.models;


public final class Album {
    private final String title;
    private final String artists;
    private final ItemType type;

    public Album(String title, String artists) {
        this.title = title;
        this.artists = artists;
        this.type = ItemType.ALBUM;
    }

    public String getTitle() {
        return title;
    }

    public String getArtists() {
        return artists;
    }

    public ItemType getType() {
        return type;
    }
}
