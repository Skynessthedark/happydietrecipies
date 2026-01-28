package com.happydieting.dev.repository;

import com.happydieting.dev.model.MediaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<MediaModel, Long> {

    @Query("SELECT m FROM MediaModel m WHERE m.owner.id = :ownerId AND m.owner.type = :type")
    Optional<MediaModel> findByOwner_IdAndType(@Param("ownerId") Long ownerId, @Param("type") String type);

    Optional<MediaModel> findByCode(String code);

    boolean existsByCode(String code);
}
