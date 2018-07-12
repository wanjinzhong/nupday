package com.nupday.dao.repository;

import com.nupday.dao.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Article findByIdAndIsDraftAndDeleteDatetimeIsNull(Integer id, Boolean isDraft);

    Article findByIdAndIsOpenAndDeleteDatetimeIsNull(Integer id, Boolean isOpen);

    Article findByIdAndIsDraftAndIsOpenAndDeleteDatetimeIsNull(Integer id, Boolean isDraft, Boolean isOpen);

    Article findByIdAndDeleteDatetimeIsNotNull(Integer id);

    Page<Article> findByDeleteDatetimeIsNullAndIsDraftIsFalseOrderByUpdateDatetimeDesc(Pageable pageable);

    Page<Article> findByIsOpenIsTrueAndIsDraftIsFalseAndDeleteDatetimeIsNullOrderByUpdateDatetimeDesc(Pageable pageable);

    Page<Article> findByDeleteDatetimeIsNullAndIsDraftIsFalseAndTypeCodeOrderByUpdateDatetimeDesc(String type, Pageable pageable);

    Page<Article> findByDeleteDatetimeIsNullAndIsOpenIsTrueAndIsDraftIsFalseAndTypeCodeOrderByUpdateDatetimeDesc(String type, Pageable pageable);
}
