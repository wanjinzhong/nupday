package com.nupday.dao.repository;
import com.nupday.dao.entity.ListBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ListBoxRepository
 * @author Neil Wan
 * @create 18-8-4
 */
@Repository
public interface ListBoxRepository extends JpaRepository<ListBox, Integer> {

    /**
     * findByNameAndCode
     * @param name
     * @param code
     * @return
     */
    ListBox findByNameAndCode(String name, String code);
}
