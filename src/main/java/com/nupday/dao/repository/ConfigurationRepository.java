package com.nupday.dao.repository;
import java.util.List;

import com.nupday.constant.ConfigurationItem;
import com.nupday.dao.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ConfigurationRepository
 * @author Neil Wan
 * @create 18-8-4
 */
@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {
    /**
     * findByItemId
     * @param nameId
     * @return
     */
    List<Configuration> findByItemId(Integer nameId);

    /**
     * findByItemIdAndOwnerId
     * @param nameId
     * @param ownerId
     * @return
     */
    List<Configuration> findByItemIdAndOwnerId(Integer nameId, Integer ownerId);
}
