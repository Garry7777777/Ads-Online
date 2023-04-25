package com.skypro.adsonline.dto;

import com.skypro.adsonline.model.Comment;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CommentDTO {
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Long createdAt;
    private Integer pk;
    private String text;

    public static Comment fromDTO(CommentDTO commentDTO) {
        var comment = new Comment();
        BeanUtils.copyProperties(commentDTO,comment);
        return comment;
    }
    public static CommentDTO toDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPk(comment.getPk());
        commentDTO.setText(comment.getText());
        commentDTO.setAuthor(comment.getAuthor().getId());
        commentDTO.setCreatedAt(comment.getCreated().toEpochMilli());
        commentDTO.setAuthorFirstName(comment.getAuthor().getFirstName());
        commentDTO.setAuthorImage("/users/me/image/" + comment.getAuthor().getId());
        return commentDTO;
    }
}
