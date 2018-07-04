package com.nupday.dao.repository;
import com.nupday.dao.entity.MemorialDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemorialDayRepository extends JpaRepository<MemorialDay, Integer> {
}
