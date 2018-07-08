package com.nupday.dao.repository;
import com.nupday.dao.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
    Album findByIdAndDeleteDatetimeIsNull(Integer id);

    List<Album> findByDeleteDatetimeIsNull();

    List<Album> findByDeleteDatetimeIsNullAndIsOpen(Boolean isOpen);

    List<Album> findByName(String name);

    @Query("SELECT a FROM Album a where a.name = :name and a.id <> :id")
    List<Album> findByNameAndIdNotEquals(@Param("name")String name, @Param("id") Integer id);
}
