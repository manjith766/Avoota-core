package com.neoteric.room.repository;
import com.neoteric.room.entity.MealPlanMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MealPlanMasterRepository extends JpaRepository<MealPlanMasterEntity, Long> {
    Optional<MealPlanMasterEntity> findByMealPlanCode(String mealPlanCode);
}
