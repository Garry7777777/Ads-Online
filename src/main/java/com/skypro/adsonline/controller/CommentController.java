package com.skypro.adsonline.controller;

import com.skypro.adsonline.dto.CommentDTO;
import com.skypro.adsonline.dto.responses.Response;
import com.skypro.adsonline.service.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping(value = "/ads")
public class CommentController {

    @Autowired
    private CommentsService commentsService;
    final String  value = "@commentsService.getById(#commentId).author.username == authentication.principal.username or hasRole('ROLE_ADMIN')";

    @Operation(summary = "Получить комментарии объявления", tags = "Комментарии")
    @GetMapping("/{id}/comments")
    public Response<CommentDTO> getComments(@PathVariable Integer id) {
        return commentsService.getAll(id);
    }

    @Operation(
            summary = "Добавление нового комментария к объявлению", tags = "Комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    @PostMapping("/{id}/comments")
    public CommentDTO addComment(@PathVariable Integer id,
                                 @RequestBody CommentDTO commentDTO, Authentication authentication) {

        var res = commentsService.addComment(id, commentDTO, authentication);
        BeanUtils.copyProperties(res, commentDTO);
        return commentDTO;

    }

    @Operation( summary = "Получить комментарий", tags = "Комментарии" )
    @PreAuthorize(value)
    @GetMapping(value = "/{adId}/comments/{commentId}")
    public CommentDTO getComment(@PathVariable("commentId") Integer commentId,
                                                    @PathVariable("adId") Integer adId) {
        return commentsService.getComment(commentId, adId);
    }

    @Operation(
            summary = "Удалить комментарий", tags = "Комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    @PreAuthorize(value)
    @DeleteMapping(value = "/{adId}/comments/{commentId}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable("commentId") Integer commentId,
                                                    @PathVariable("adId") Integer adId) {
        commentsService.deleteComment(commentId, adId);
        return ResponseEntity.ok().build();
    }
    @Operation(
            summary = "Обновить комментарий", tags = "Комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    @PreAuthorize(value)
    @PatchMapping(value = "/{adId}/comments/{commentId}")
    public CommentDTO updateComment(@PathVariable("commentId") Integer commentId,
                                    @PathVariable("adId") Integer adId,
                                    @RequestBody CommentDTO commentDTO) {
        return commentsService.update(commentId, adId, commentDTO);
    }
}
