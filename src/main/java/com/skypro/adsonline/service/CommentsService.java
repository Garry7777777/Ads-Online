package com.skypro.adsonline.service;

import com.skypro.adsonline.dto.CommentDTO;
import com.skypro.adsonline.dto.responses.Response;
import com.skypro.adsonline.model.Comment;
import com.skypro.adsonline.repository.AdRepository;
import com.skypro.adsonline.repository.CommentRepository;
import com.skypro.adsonline.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.skypro.adsonline.exception.NotFoundException;
import java.util.stream.Collectors;

@Service
public class CommentsService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdRepository adRepository;


    public Response<CommentDTO> getAll(Integer adsId) {
        return Response.Wrapper(commentRepository.findAllByAdPk(adsId).stream().map(Comment::toDTO).collect(Collectors.toList()));
    }
    @Transactional
    public CommentDTO addComment(Integer id, CommentDTO commentDTO, Authentication authentication) {
        var comment = CommentDTO.fromDTO(commentDTO);
        var user = userRepository.findByUsername(authentication.getName()).orElseThrow(NotFoundException::new);
        comment.setAuthor(user);
        comment.setAd(adRepository.findById(id).orElseThrow(NotFoundException::new));
        return commentRepository.save(comment).toDTO();
    }

    @Transactional
    public void deleteComment(Integer commentId, Integer adId) {
        commentRepository.deleteByPkAndAdPk(commentId, adId);
    }

    @Transactional
    public CommentDTO update(Integer commentId, Integer adId, CommentDTO commentDTO) {
        Comment comment = commentRepository.findByPkAndAdPk(commentId, adId).orElseThrow(NotFoundException::new);
        comment.setText(commentDTO.getText());
        return commentRepository.save(comment).toDTO();
    }

    public Comment getById(Integer id) {
        return commentRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public CommentDTO getComment(Integer commentId, Integer adId) {
       return commentRepository.findByPkAndAdPk(commentId, adId).orElseThrow(NotFoundException::new).toDTO();
    }
}
