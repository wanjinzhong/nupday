package com.nupday.dao.repository;
import java.util.List;

import com.nupday.dao.entity.DBBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DBBackupRepository extends JpaRepository<DBBackup, Integer> {

    @Query(nativeQuery = true, value =
        "select * from db_backup where id < (select id from db_backup order by id desc limit :keepCount, 1)")
    List<DBBackup> findToBeDeleteDBBackup(@Param("keepCount") Integer keepCount);
}
