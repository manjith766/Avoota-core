package com.neoteric.room.controller;

import com.neoteric.common.ui.ApiResponse;
import com.neoteric.room.model.RatePlan;
import com.neoteric.room.service.RatePlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rateplans")
@RequiredArgsConstructor
public class RatePlanController {

    private final RatePlanService ratePlanService;

    // Create or Update a rate plan
    @PostMapping("/save")
    public ApiResponse<String> saveOrUpdateRatePlan(@RequestBody RatePlan ratePlan) {
        return ratePlanService.saveOrUpdateRatePlan(ratePlan);
    }

    //  Fetch all rate plans for a given room — returns DTOs, not Entities
    @GetMapping("/{roomId}")
    public ApiResponse<List<RatePlan>> getRatePlansByRoom(@PathVariable String roomId) {
        return ratePlanService.getRatePlansByRoom(roomId);
    }
}
