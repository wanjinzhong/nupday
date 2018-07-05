package com.nupday.dao.repository;
import com.nupday.dao.entity.ListBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListBoxRepository extends JpaRepository<ListBox, Integer> {
    ListBox findByNameAndCode(String name, String code);
}
