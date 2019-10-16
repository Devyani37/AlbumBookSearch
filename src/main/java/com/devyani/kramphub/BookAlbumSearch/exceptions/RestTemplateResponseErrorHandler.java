package com.devyani.kramphub.BookAlbumSearch.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;


public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String errorMsg = "Upstream Exception Error code: " + response.getRawStatusCode() + " Error Response" + response.getBody();
        log.error(errorMsg);
        throw new UpstreamException(errorMsg);
    }
}
