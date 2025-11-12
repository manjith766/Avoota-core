package com.neoteric.room.model;

import lombok.Data;

@Data
public class RatePlan {
    private Long id;             // for edit
    private String roomId;       // which room this plan belongs to
    private String ratePlanCode; // AP, CP, MAP, EP
    private String mealPlanName; // Free Breakfast, etc.
}
