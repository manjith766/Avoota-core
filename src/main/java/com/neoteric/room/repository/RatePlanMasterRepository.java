package com.neoteric.room.repository;


import com.neoteric.room.entity.RatePlanMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RatePlanMasterRepository extends JpaRepository<RatePlanMasterEntity, Long> {
    Optional<RatePlanMasterEntity> findByRatePlanCode(String ratePlanCode);
}
