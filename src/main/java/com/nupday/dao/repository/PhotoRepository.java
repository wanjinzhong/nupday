package com.nupday.dao.repository;
import java.util.List;

import com.nupday.dao.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PhotoRepository
 * @author Neil Wan
 * @create 18-8-4
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    /**
     * findByIdAndDeleteDatetimeIsNull
     * @param id
     * @return
     */
    Photo findByIdAndDeleteDatetimeIsNull(Integer id);

    /**
     * findByAlbumIdAndDeleteDatetimeIsNull
     * @param albumId
     * @param pageable
     * @return
     */
    Page<Photo> findByAlbumIdAndDeleteDatetimeIsNull(Integer albumId, Pageable pageable);

    /**
     * findByDeleteDatetimeIsNotNull
     * @return
     */
    List<Photo> findByDeleteDatetimeIsNotNull();
}
