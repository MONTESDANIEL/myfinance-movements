package com.myfinance.backend.movements.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myfinance.backend.movements.entities.AppTag;
import com.myfinance.backend.movements.services.TagService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    @Autowired
    private final TagService tagService;

    @GetMapping("/viewAllTags")
    public ResponseEntity<?> viewAllTags() {
        ResponseEntity<?> response = tagService.viewAllTags();
        return response;
    }

    @GetMapping("/viewUserTags")
    public ResponseEntity<?> viewUserTags(@RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = tagService.viewUserTags(authorizationToken);
        return response;
    }

    @PostMapping("/newTag")
    public ResponseEntity<?> newTag(
            @Valid @RequestBody AppTag appTag,
            @RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = tagService.newTag(appTag, authorizationToken);
        return response;
    }

    @PutMapping("/updateTag")
    public ResponseEntity<?> updateTag(
            @Valid @RequestBody AppTag appTag,
            @RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = tagService.updateTag(appTag, authorizationToken);
        return response;
    }

    @DeleteMapping("/deleteTag/{id}")
    public ResponseEntity<?> deleteTag(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = tagService.deleteTag(id, authorizationToken);
        return response;
    }

}