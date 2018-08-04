package com.nupday.dao.repository;
import com.nupday.dao.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * OwnerRepository
 * @author Neil Wan
 * @create 18-8-4
 */
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

    /**
     * findByName
     * @param name
     * @return
     */
    Owner findByName(String name);
}
