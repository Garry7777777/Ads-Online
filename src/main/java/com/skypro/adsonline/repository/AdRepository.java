package com.skypro.adsonline.repository;

import com.skypro.adsonline.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

    List<Ad> findAllByAuthorId(Integer authorId);
    List<Ad> findAllByTitleContains(String title);
}
