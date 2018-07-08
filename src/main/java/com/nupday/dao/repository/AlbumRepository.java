package com.nupday.dao.repository;
import com.nupday.dao.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
    Album findByIdAndDeleteDatetimeIsNull(Integer id);

    List<Album> findByDeleteDatetimeIsNull();

    List<Album> findByDeleteDatetimeIsNullAndIsOpen(Boolean isOpen);
}
