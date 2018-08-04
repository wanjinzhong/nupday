package com.nupday.dao.repository;

import com.nupday.dao.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * CommentRepository
 * @author Neil Wan
 * @create 18-8-4
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    /**
     * findByTargetTypeCodeAndTargetId
     * @param targetType
     * @param targetId
     * @return
     */
    List<Comment> findByTargetTypeCodeAndTargetId(String targetType, Integer targetId);

    /**
     * deleteByIdIn
     * @param ids
     */
    void deleteByIdIn(Set<Integer> ids);

    /**
     * countByTargetTypeCode
     * @param targetType
     * @return
     */
    Integer countByTargetTypeCode(String targetType);

    /**
     * findGuestBook
     * @param start
     * @param size
     * @return
     */
    @Query(nativeQuery = true,
            value = "SELECT a.* FROM comment a " +
                    "JOIN list_box b ON a.target_type = b.id WHERE b.category_name = 'COMMENT_TARGET' AND b.item_code = 'GUEST_BOOK'  " +
                    "LIMIT :start, :pageSize")
    List<Comment> findGuestBook(@Param("start") Integer start, @Param("pageSize") Integer size);
}
