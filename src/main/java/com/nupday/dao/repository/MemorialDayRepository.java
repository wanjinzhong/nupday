package com.nupday.dao.repository;
import com.nupday.dao.entity.MemorialDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemorialDayRepository extends JpaRepository<MemorialDay, Integer> {
    MemorialDay findByHomeIsTrue();

    MemorialDay findByHomeIsTrueAndOpenIsTrue();

    List<MemorialDay> findByOpenIsTrue();
}
