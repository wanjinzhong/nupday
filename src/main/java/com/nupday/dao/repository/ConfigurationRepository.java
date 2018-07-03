package com.nupday.dao.repository;
import java.util.List;

import com.nupday.constant.ConfigurationItem;
import com.nupday.dao.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {
    List<Configuration> findByItem(ConfigurationItem item);
}
