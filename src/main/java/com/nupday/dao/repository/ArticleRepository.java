package com.nupday.dao.repository;

import com.nupday.dao.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ArticleRepository
 * @author Neil Wan
 * @create 18-8-4
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    /**
     * findByIdAndIsDraftAndDeleteDatetimeIsNull
     * @param id
     * @param isDraft
     * @return
     */
    Article findByIdAndIsDraftAndDeleteDatetimeIsNull(Integer id, Boolean isDraft);

    /**
     * findByIdAndIsDraftAndIsOpenAndDeleteDatetimeIsNull
     * @param id
     * @param isDraft
     * @param isOpen
     * @return
     */
    Article findByIdAndIsDraftAndIsOpenAndDeleteDatetimeIsNull(Integer id, Boolean isDraft, Boolean isOpen);

    /**
     * findByDeleteDatetimeIsNullAndIsDraftIsFalseOrderByUpdateDatetimeDesc
     * @param pageable
     * @return
     */
    Page<Article> findByDeleteDatetimeIsNullAndIsDraftIsFalseOrderByUpdateDatetimeDesc(Pageable pageable);

    /**
     * findByIsOpenIsTrueAndIsDraftIsFalseAndDeleteDatetimeIsNullOrderByUpdateDatetimeDesc
     * @param pageable
     * @return
     */
    Page<Article> findByIsOpenIsTrueAndIsDraftIsFalseAndDeleteDatetimeIsNullOrderByUpdateDatetimeDesc(Pageable pageable);

    /**
     * findByDeleteDatetimeIsNullAndIsDraftIsFalseAndTypeCodeOrderByUpdateDatetimeDesc
     * @param type
     * @param pageable
     * @return
     */
    Page<Article> findByDeleteDatetimeIsNullAndIsDraftIsFalseAndTypeCodeOrderByUpdateDatetimeDesc(String type, Pageable pageable);

    /**
     * findByDeleteDatetimeIsNullAndIsOpenIsTrueAndIsDraftIsFalseAndTypeCodeOrderByUpdateDatetimeDesc
     * @param type
     * @param pageable
     * @return
     */
    Page<Article> findByDeleteDatetimeIsNullAndIsOpenIsTrueAndIsDraftIsFalseAndTypeCodeOrderByUpdateDatetimeDesc(String type, Pageable pageable);

    /**
     * findByDeleteDatetimeIsNotNullAndTypeCodeOrderByUpdateDatetimeDesc
     * @param type
     * @param pageable
     * @return
     */
    Page<Article> findByDeleteDatetimeIsNotNullAndTypeCodeOrderByUpdateDatetimeDesc(String type, Pageable pageable);
}
