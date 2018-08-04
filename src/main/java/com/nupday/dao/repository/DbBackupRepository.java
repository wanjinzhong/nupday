package com.nupday.dao.repository;
import java.util.List;

import com.nupday.dao.entity.DbBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * DBBackupRepository
 * @author Neil Wan
 * @create 18-8-4
 */
@Repository
public interface DbBackupRepository extends JpaRepository<DbBackup, Integer> {

    /**
     * findToBeDeleteDBBackup
     * @param keepCount
     * @return
     */
    @Query(nativeQuery = true, value =
        "select * from db_backup where id < (select id from db_backup order by id desc limit :keepCount, 1)")
    List<DbBackup> findToBeDeleteDBBackup(@Param("keepCount") Integer keepCount);

    /**
     * findAllByOrderByEntryDatetimeDesc
     * @return
     */
    List<DbBackup> findAllByOrderByEntryDatetimeDesc();
}
