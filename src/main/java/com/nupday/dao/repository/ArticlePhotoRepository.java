package com.nupday.dao.repository;
import java.time.LocalDateTime;
import java.util.List;

import com.nupday.dao.entity.Article;
import com.nupday.dao.entity.ArticlePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlePhotoRepository extends JpaRepository<ArticlePhoto, Integer> {

    @Query("SELECT a from ArticlePhoto a where a.photo.album.id = :albumId and a.entryDatetime > :time")
    List<ArticlePhoto> findByAlbumAfterDay(@Param("albumId") Integer albumId, @Param("time") LocalDateTime time);

    @Modifying
    @Query("DELETE From ArticlePhoto a where a.photo.id = :photoId")
    void deleteByPhotoId(@Param("photoId") Integer photoId);

    @Query("SELECT distinct a.article from ArticlePhoto a where a.photo.id = :photoId")
    List<Article> findArticleByPhotoId(@Param("photoId") Integer photoId);

    List<ArticlePhoto> findByArticleId(Integer articleId);
}
