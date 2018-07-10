package com.nupday.dao.repository;
import com.nupday.dao.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    Photo findByIdAndDeleteDatetimeIsNull(Integer id);

    Page<Photo> findByAlbumIdAndDeleteDatetimeIsNull(Integer albumId, Pageable pageable);
}
