//package com.example.demo.controller;
//
//import com.example.demo.service.ElasticSearchService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//public class SearchController {
//
//    private final ElasticSearchService searchService;
//
//    @PostMapping(path = "/save")
//    public ResponseEntity<Boolean> saveData(@RequestBody String xhtml) {
//
//        searchService.save(xhtml);
//
//        return ResponseEntity.ok(true);
//    }
//
//    @GetMapping(path = "/search", params = {"query"})
//    public ResponseEntity<String> searchData(@RequestParam("query") String query) {
//        return ResponseEntity.ok(searchService.search(query));
//    }
//}
