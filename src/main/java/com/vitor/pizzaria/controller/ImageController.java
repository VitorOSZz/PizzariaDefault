package com.vitor.pizzaria.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/images")
class ImageController {

    private static final String IMAGE_ENDPOINT_PREFIX = "/api/images/";

    private final String imageStorageBaseUrl;

    ImageController(@Value("${image.storage.base-url}") String imageStorageBaseUrl) {
        this.imageStorageBaseUrl = imageStorageBaseUrl.endsWith("/")
                ? imageStorageBaseUrl.substring(0, imageStorageBaseUrl.length() - 1)
                : imageStorageBaseUrl;
    }

    @GetMapping("/**")
    ResponseEntity<Void> getImage(HttpServletRequest request) {
        String imagePath = request.getRequestURI().substring(IMAGE_ENDPOINT_PREFIX.length());
        if (imagePath.isBlank() || imagePath.contains("..")) {
            return ResponseEntity.badRequest().build();
        }

        URI imageUri = UriComponentsBuilder
                .fromUriString(imageStorageBaseUrl + "/" + imagePath)
                .build(true)
                .toUri();

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, imageUri.toString())
                .build();
    }
}
