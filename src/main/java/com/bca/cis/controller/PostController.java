package com.bca.cis.controller;


import com.bca.cis.exception.BadRequestException;
import com.bca.cis.model.*;
import com.bca.cis.repository.PostRepository;
import com.bca.cis.response.ApiDataResponse;
import com.bca.cis.response.ApiResponse;
import com.bca.cis.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(PostController.urlRoute)
@Tag(name = "Post API")
public class PostController {

    static final String urlRoute = "/api/v1/post";
    private final PostService service;

    @GetMapping("/count")
    public ResponseEntity<TotalCountResponse> countPostJPA() {
        // response true
        log.info("GET " + urlRoute + "/count endpoint hit");
        return ResponseEntity.ok().body(new TotalCountResponse(true, service.countPostJPA()));
    }

    @GetMapping("/count/elastic")
    public ResponseEntity<TotalCountResponse> countPostELK() {
        // response true
        log.info("GET " + urlRoute + "/count/elastic endpoint hit");
//        return ResponseEntity.ok().body(new TotalCountResponse(true, service.countPostELK()));
        return null;
    }



    @GetMapping
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<PostIndexResponse>>> listDataPostIndex(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list post", service.listDataPostIndex(pages, limit, sortBy, direction, keyword)));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            PostDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse(true, "Successfully found post", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody PostCreateUpdateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created post"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") String id, @Valid @RequestBody PostCreateUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated post"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted post"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}