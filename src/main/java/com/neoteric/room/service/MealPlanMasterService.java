package com.neoteric.room.service;

import com.neoteric.common.ui.ApiResponse;
import com.neoteric.common.ui.AvootaResponseStatus;
import com.neoteric.common.ui.AvootaUtil;
import com.neoteric.room.entity.MealPlanMasterEntity;
import com.neoteric.room.repository.MealPlanMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.neoteric.common.ui.AvootaResponseStatus.SUCCESS;

@Slf4j
@Service
public class MealPlanMasterService {

    private final MealPlanMasterRepository mealPlanRepo;

    public MealPlanMasterService(MealPlanMasterRepository mealPlanRepo) {
        this.mealPlanRepo = mealPlanRepo;
    }

    // Save or Update MealPlan
    public ApiResponse<String> saveMealPlan(MealPlanMasterEntity mealPlan) {
        log.info("Saving meal plan: {}", mealPlan.getMealPlanCode());
        try {
            mealPlanRepo.save(mealPlan);
            return ApiResponse.<String>builder()
                    .status(SUCCESS)
                    .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                    .failureMessage("Meal plan saved successfully.")
                    .data("Meal Plan Code: " + mealPlan.getMealPlanCode())
                    .build();
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate Meal Plan Code: {}", mealPlan.getMealPlanCode());
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DUPLICATE_ENTRY,
                    "Meal Plan Code already exists: " + mealPlan.getMealPlanCode()
            );
        } catch (Exception ex) {
            log.error("Error while saving Meal Plan: {}", ex.getMessage(), ex);
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DB_ERROR,
                    "Error occurred while saving meal plan."
            );
        }
    }

    // Get All MealPlans
    public ApiResponse<List<MealPlanMasterEntity>> getAllMealPlans() {
        log.info("Fetching all meal plans...");
        try {
            List<MealPlanMasterEntity> meals = mealPlanRepo.findAll();
            if (meals.isEmpty()) {
                return AvootaUtil.failure(
                        AvootaResponseStatus.FailureCode.NO_RESULTS_FOUND,
                        "No meal plans found in the system."
                );
            }
            return ApiResponse.<List<MealPlanMasterEntity>>builder()
                    .status(SUCCESS)
                    .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                    .failureMessage("Meal plans fetched successfully.")
                    .data(meals)
                    .build();
        } catch (Exception ex) {
            log.error("Error fetching meal plans: {}", ex.getMessage(), ex);
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DB_ERROR,
                    "Error occurred while fetching meal plans."
            );
        }
    }
}
