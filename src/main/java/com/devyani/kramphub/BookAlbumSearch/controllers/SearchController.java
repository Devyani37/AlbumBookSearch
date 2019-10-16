package com.devyani.kramphub.BookAlbumSearch.controllers;

import com.devyani.kramphub.BookAlbumSearch.dtos.downstreams.Result;
import com.devyani.kramphub.BookAlbumSearch.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
//controller file in rest template
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<List<Result>> search(@RequestParam("q") String searchText) {
        List<Result> resultList = searchService.search(searchText);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(resultList);
    }

}
