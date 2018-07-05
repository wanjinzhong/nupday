package com.nupday.dao.repository;
import com.nupday.dao.entity.Article;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Article findByIdAndIsDraftAndDeleteDatetimeIsNull(Integer id, Boolean isDraft);

    Article findByIdAndIsOpenAndDeleteDatetimeIsNull(Integer id, Boolean isOpen);

    Article findByIdAndIsDraftAndIsOpenAndDeleteDatetimeIsNull(Integer id, Boolean isDraft, Boolean isOpen);

    Article findByIdAndDeleteDatetimeIsNotNull(Integer id);
}
