package com.skypro.adsonline.repository;

import com.skypro.adsonline.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByAdPk(Integer pk);
    Optional<Comment> findByPkAndAdPk(Integer pk, Integer adPk);
    void deleteByPkAndAdPk(Integer pk, Integer adPk);
}
