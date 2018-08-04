package com.nupday.dao.repository;
import com.nupday.dao.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AlbumRepository
 * @author Neil Wan
 * @create 18-8-4
 */
@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
    /**
     * findByIdAndDeleteDatetimeIsNull
     * @param id
     * @return
     */
    Album findByIdAndDeleteDatetimeIsNull(Integer id);

    /**
     * findByDeleteDatetimeIsNull
     * @return
     */
    List<Album> findByDeleteDatetimeIsNull();

    /**
     * findByDeleteDatetimeIsNotNull
     * @return
     */
    List<Album> findByDeleteDatetimeIsNotNull();

    /**
     * findByDeleteDatetimeIsNullAndIsOpen
     * @param isOpen
     * @return
     */
    List<Album> findByDeleteDatetimeIsNullAndIsOpen(Boolean isOpen);

    /**
     * findByName
     * @param name
     * @return
     */
    List<Album> findByName(String name);

    /**
     * findByNameAndIdNotEquals
     * @param name
     * @param id
     * @return
     */
    @Query("SELECT a FROM Album a where a.name = :name and a.id <> :id")
    List<Album> findByNameAndIdNotEquals(@Param("name")String name, @Param("id") Integer id);
}
