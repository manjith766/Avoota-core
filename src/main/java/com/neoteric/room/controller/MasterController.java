package com.neoteric.room.controller;

import com.neoteric.common.ui.ApiResponse;
import com.neoteric.room.entity.MealPlanMasterEntity;
import com.neoteric.room.entity.RatePlanMasterEntity;
import com.neoteric.room.service.MealPlanMasterService;
import com.neoteric.room.service.RatePlanMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master")
@RequiredArgsConstructor
public class MasterController {

    private final RatePlanMasterService ratePlanService;
    private final MealPlanMasterService mealPlanService;

    //  Save Rate Plan Master
    @PostMapping("/rateplans")
    public ApiResponse<String> saveRatePlan(@RequestBody RatePlanMasterEntity ratePlan) {
        return ratePlanService.saveRatePlan(ratePlan);
    }

    //  Get All Rate Plans
    @GetMapping("/rateplans")
    public ApiResponse<List<RatePlanMasterEntity>> getRatePlans() {
        return ratePlanService.getAllRatePlans();
    }

    //  Save Meal Plan Master
    @PostMapping("/mealplans")
    public ApiResponse<String> saveMealPlan(@RequestBody MealPlanMasterEntity mealPlan) {
        return mealPlanService.saveMealPlan(mealPlan);
    }

    //  Get All Meal Plans
    @GetMapping("/mealplans")
    public ApiResponse<List<MealPlanMasterEntity>> getMealPlans() {
        return mealPlanService.getAllMealPlans();
    }
}
