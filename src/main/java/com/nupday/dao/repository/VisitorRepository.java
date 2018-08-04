package com.nupday.dao.repository;
import com.nupday.dao.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * VisitorRepository
 * @author Neil Wan
 * @create 18-8-4
 */
@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Integer> {

    /**
     * findByCode
     * @param code
     * @return
     */
    Visitor findByCode(String code);
}
