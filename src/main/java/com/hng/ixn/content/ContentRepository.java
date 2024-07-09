package com.hng.ixn.content;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ContentRepository extends JpaRepository<Content, Integer> {
    Content findFirstByprevIdIsNull();

}
// Root = the first record where prev_id is NULL