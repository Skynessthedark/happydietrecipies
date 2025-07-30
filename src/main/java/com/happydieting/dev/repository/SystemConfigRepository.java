package com.happydieting.dev.repository;

import com.happydieting.dev.model.SystemConfigModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SystemConfigRepository extends JpaRepository<SystemConfigModel, Long> {
    @Query("SELECT sc FROM SystemConfigModel sc WHERE sc.key = :key")
    Optional<SystemConfigModel> findByKey(@Param("key") String key);
}
