package com.nupday.dao.repository;

import com.nupday.dao.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByTargetTypeCodeAndTargetId(String targetType, Integer targetId);

    void deleteByIdIn(Set<Integer> ids);

    Integer countByTargetTypeCode(String targetType);

    @Query(nativeQuery = true,
            value = "SELECT a.* FROM comment a " +
                    "JOIN list_box b ON a.target_type = b.id WHERE b.category_name = 'COMMENT_TARGET' AND b.item_code = 'GUEST_BOOK'  " +
                    "LIMIT :start, :pageSize")
    List<Comment> findGuestBook(@Param("start") Integer start, @Param("pageSize") Integer size);
}
