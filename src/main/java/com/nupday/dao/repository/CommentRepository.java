package com.nupday.dao.repository;

import com.nupday.dao.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByTargetTypeCodeAndTargetId(String targetType, Integer targetId);

    void deleteByIdIn(Set<Integer> ids);
}
