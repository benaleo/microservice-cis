package com.bca.cis.repository;

import com.bca.cis.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
            SELECT p FROM Post p
            WHERE
            (LOWER(p.description) LIKE(:keyword) )
            """)
    Page<Post> findByNameLikeIgnoreCase(String keyword, Pageable pageable);

    Optional<Post> findBySecureId(String id);
}