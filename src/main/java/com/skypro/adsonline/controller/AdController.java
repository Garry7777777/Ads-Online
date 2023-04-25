package com.skypro.adsonline.controller;

import com.skypro.adsonline.dto.AdDTO;
import com.skypro.adsonline.dto.CreateAdDTO;
import com.skypro.adsonline.dto.FullAdDTO;
import com.skypro.adsonline.dto.responses.Response;
import com.skypro.adsonline.service.AdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping(value = "/ads")
public class AdController {

    @Autowired
    private AdService adService;
    final String  value = "@adService.getById(#id).getEmail()== authentication.principal.username or hasRole('ROLE_ADMIN')";

    @GetMapping
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity.ok(adService.getAll());
    }

    @Operation(
            summary = "Добавить объявление", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Created",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @SneakyThrows
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDTO> addAds(@RequestPart("image") MultipartFile imageFile,
                                        @RequestPart("properties") CreateAdDTO createAds,
                                        Authentication authentication)  {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(adService.createAd(imageFile,createAds,authentication));
    }
    @Operation(
            summary = "Получить информацию об объявлении", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FullAdDTO.class))}),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAds(@PathVariable Integer id) {
            return ResponseEntity.ok(adService.getById(id));
    }

    @Operation(
            summary = "Удалить объявление", tags = "Объявления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content),
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
            }
    )
    @PreAuthorize(value)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeAd(@PathVariable Integer id) {
        adService.remove(id);
        return ResponseEntity.ok().build();
    }

    @Operation(hidden = true)
    @GetMapping(value = "/search")
    public ResponseEntity<?> searchTitle(@RequestParam(required = false) String title) {
        return ResponseEntity.ok(adService.searchTitle(title));
    }

    @Operation(
            summary = "Обновить информацию об объявлении", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PreAuthorize(value)
    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateAds(@PathVariable Integer id, @RequestBody CreateAdDTO createAd) {
        return ResponseEntity.ok(adService.update(id, createAd));
    }

    @Operation(
            summary = "Получить объявления авторизованного пользователя", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
            }
    )
    @GetMapping(value = "/me")
    public ResponseEntity<?> getMyAds(Authentication authentication) {
        return ResponseEntity.ok(adService.getMyAds(authentication));
    }

    @Operation(
            summary = "Обновить картинку объявления", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdDTO.class))}),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PreAuthorize(value)
    @PatchMapping(value = "/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable Integer id, @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok(adService.updateImage(id, image));
    }

    @Operation(hidden = true)
    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] showImage(@PathVariable("id") Integer id) {
        return adService.showImage(id);
    }
}
