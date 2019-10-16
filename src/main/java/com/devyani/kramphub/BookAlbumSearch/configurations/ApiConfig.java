package com.devyani.kramphub.BookAlbumSearch.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Value("${api.google.url}")
    private String googleApiUrl;
    @Value("${api.connectTimeout}")
    private int connectTimeout;
    @Value("${api.readTimeout}")
    private int readTimeout;
    @Value("${api.google.maxResults}")
    private int googleMaxResult;
    @Value("${api.itunes.maxResults}")
    private int iTunesMaxResult;
    @Value("${api.itunes.url}")
    private String iTunesUrl;
    @Value("${service.timeout}")
    private int serviceTimeout;

    public String getiTunesUrl() {
        return iTunesUrl;
    }

    public void setiTunesUrl(String iTunesUrl) {
        this.iTunesUrl = iTunesUrl;
    }

    public int getGoogleMaxResult() {
        return googleMaxResult;
    }

    public void setGoogleMaxResult(int googleMaxResult) {
        this.googleMaxResult = googleMaxResult;
    }

    public int getiTunesMaxResult() {
        return iTunesMaxResult;
    }

    public void setiTunesMaxResult(int iTunesMaxResult) {
        this.iTunesMaxResult = iTunesMaxResult;
    }

    public String getGoogleApiUrl() {
        return googleApiUrl;
    }

    public void setGoogleApiUrl(String googleApiUrl) {
        this.googleApiUrl = googleApiUrl;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getServiceTimeout() {
        return serviceTimeout;
    }

    public void setServiceTimeout(int serviceTimeout) {
        this.serviceTimeout = serviceTimeout;
    }
}
