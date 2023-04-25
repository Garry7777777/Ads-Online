package com.skypro.adsonline.model;

import lombok.*;
import com.skypro.adsonline.dto.CommentDTO;

import javax.persistence.*;
import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    private Instant created = Instant.now();
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Ad ad;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    public CommentDTO toDTO() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPk(this.getPk());
        commentDTO.setText(this.getText());
        commentDTO.setAuthor(this.getAuthor().getId());
        commentDTO.setCreatedAt(this.getCreated().toEpochMilli());
        commentDTO.setAuthorFirstName(this.getAuthor().getFirstName());
        commentDTO.setAuthorImage("/users/me/image/" + this.getAuthor().getId());
        return commentDTO;
    }
}
