package com.neoteric.room.repository;

import com.neoteric.room.entity.RatePlanEntity;
import com.neoteric.room.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatePlanRepository extends JpaRepository<RatePlanEntity, Long> {
    List<RatePlanEntity> findByRoom_RoomId(String roomId);
    boolean existsByRoomAndRatePlanCodeIgnoreCaseAndMealPlanNameIgnoreCase(RoomEntity room, String ratePlanCode, String mealPlanName);
    Optional<RatePlanEntity> findByIdAndRoom(Long id, RoomEntity room);
}
