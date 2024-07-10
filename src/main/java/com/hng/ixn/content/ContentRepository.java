package com.hng.ixn.content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {
    Content findFirstByprevIdIsNull();

    @Query("SELECT c FROM Content c WHERE c.timestamp = (SELECT MAX(c2.timestamp) FROM Content c2)")
    List<Content> findAllWithMaxTimestamp();
}
// Root = the first record where prev_id is NULL