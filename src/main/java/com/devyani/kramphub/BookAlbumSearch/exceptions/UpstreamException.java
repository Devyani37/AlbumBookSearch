package com.devyani.kramphub.BookAlbumSearch.exceptions;

public class UpstreamException extends RuntimeException {

    public UpstreamException(String msg){
        super(msg);
    }

    public UpstreamException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
