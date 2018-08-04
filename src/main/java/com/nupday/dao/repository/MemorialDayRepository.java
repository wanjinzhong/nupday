package com.nupday.dao.repository;
import com.nupday.dao.entity.MemorialDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MemorialDayRepository
 * @author Neil Wan
 * @create 18-8-4
 */
@Repository
public interface MemorialDayRepository extends JpaRepository<MemorialDay, Integer> {

    /**
     * findByHomeIsTrue
     * @return
     */
    MemorialDay findByHomeIsTrue();

    /**
     * findByHomeIsTrueAndOpenIsTrue
     * @return
     */
    MemorialDay findByHomeIsTrueAndOpenIsTrue();

    /**
     * findByOpenIsTrue
     * @return
     */
    List<MemorialDay> findByOpenIsTrue();

    /**
     * findByIdAndOpenIsTrue
     * @param id
     * @return
     */
    MemorialDay findByIdAndOpenIsTrue(Integer id);
}
