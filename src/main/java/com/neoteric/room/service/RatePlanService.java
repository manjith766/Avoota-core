package com.neoteric.room.service;

import com.neoteric.common.ui.ApiResponse;
import com.neoteric.common.ui.AvootaResponseStatus;
import com.neoteric.common.ui.AvootaUtil;
import com.neoteric.room.entity.RatePlanEntity;
import com.neoteric.room.entity.RoomEntity;
import com.neoteric.room.model.RatePlan;
import com.neoteric.room.repository.RatePlanRepository;
import com.neoteric.room.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.neoteric.common.ui.AvootaResponseStatus.SUCCESS;

@Slf4j
@Service
public class RatePlanService {

    private final RatePlanRepository rateRepo;
    private final RoomRepository roomRepo;

    public RatePlanService(RatePlanRepository rateRepo, RoomRepository roomRepo) {
        this.rateRepo = rateRepo;
        this.roomRepo = roomRepo;
    }

    /**
     * Create or Update a Rate Plan for a given room
     */
    @Transactional
    public ApiResponse<String> saveOrUpdateRatePlan(RatePlan ratePlan) {
        log.info("Add/Edit RatePlan for roomId={}, ratePlanCode={}, mealPlanName={}",
                ratePlan.getRoomId(), ratePlan.getRatePlanCode(), ratePlan.getMealPlanName());

        try {
            // Validation
            if (ratePlan.getRoomId() == null || ratePlan.getRatePlanCode() == null || ratePlan.getMealPlanName() == null) {
                return AvootaUtil.failure(
                        AvootaResponseStatus.FailureCode.INVALID_INPUT,
                        "roomId, ratePlanCode, and mealPlanName are required."
                );
            }

            Optional<RoomEntity> roomOpt = roomRepo.findByRoomId(ratePlan.getRoomId());
            if (roomOpt.isEmpty()) {
                return AvootaUtil.failure(
                        AvootaResponseStatus.FailureCode.NO_RESULTS_FOUND,
                        "Room not found with ID: " + ratePlan.getRoomId()
                );
            }

            RoomEntity room = roomOpt.get();

            //  Update existing rate plan
            if (ratePlan.getId() != null) {
                Optional<RatePlanEntity> existingOpt = rateRepo.findByIdAndRoom(ratePlan.getId(), room);
                if (existingOpt.isEmpty()) {
                    return AvootaUtil.failure(
                            AvootaResponseStatus.FailureCode.NO_RESULTS_FOUND,
                            "Rate plan not found for update."
                    );
                }

                RatePlanEntity existing = existingOpt.get();
                existing.setRatePlanCode(ratePlan.getRatePlanCode());
                existing.setMealPlanName(ratePlan.getMealPlanName());
                rateRepo.save(existing);

                return ApiResponse.<String>builder()
                        .status(SUCCESS)
                        .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                        .failureMessage("Rate plan updated successfully.")
                        .data("Updated Rate Plan ID: " + existing.getId())
                        .build();
            }

            //  Create new rate plan (prevent duplicates)
            boolean exists = rateRepo.existsByRoomAndRatePlanCodeIgnoreCaseAndMealPlanNameIgnoreCase(
                    room, ratePlan.getRatePlanCode(), ratePlan.getMealPlanName()
            );
            if (exists) {
                return AvootaUtil.failure(
                        AvootaResponseStatus.FailureCode.DUPLICATE_ENTRY,
                        "This RatePlanCode and MealPlanName combination already exists for this room."
                );
            }

            RatePlanEntity newPlan = new RatePlanEntity();
            newPlan.setRoom(room);
            newPlan.setRatePlanCode(ratePlan.getRatePlanCode());
            newPlan.setMealPlanName(ratePlan.getMealPlanName());
            rateRepo.save(newPlan);

            return ApiResponse.<String>builder()
                    .status(SUCCESS)
                    .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                    .failureMessage("Rate plan added successfully.")
                    .data("Created Rate Plan ID: " + newPlan.getId())
                    .build();

        } catch (Exception ex) {
            log.error("Error saving/updating rate plan for roomId={}", ratePlan.getRoomId(), ex);
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DB_ERROR,
                    "Error occurred while saving/updating rate plan."
            );
        }
    }

    /**
     * Fetch all rate plans for a given room (returns DTOs, not entities)
     */
    public ApiResponse<List<RatePlan>> getRatePlansByRoom(String roomId) {
        log.info("Fetching rate plans for roomId={}", roomId);

        try {
            Optional<RoomEntity> roomOpt = roomRepo.findByRoomId(roomId);
            if (roomOpt.isEmpty()) {
                return AvootaUtil.failure(
                        AvootaResponseStatus.FailureCode.NO_RESULTS_FOUND,
                        "No room found with ID: " + roomId
                );
            }

            List<RatePlanEntity> ratePlans = rateRepo.findByRoom_RoomId(roomId);

            if (ratePlans.isEmpty()) {
                return AvootaUtil.failure(
                        AvootaResponseStatus.FailureCode.NO_RESULTS_FOUND,
                        "No rate plans found for room ID: " + roomId
                );
            }

            // Convert to DTOs — avoids LazyInitialization & proxy errors
            List<RatePlan> dtoList = ratePlans.stream()
                    .map(rp -> {
                        RatePlan dto = new RatePlan();
                        dto.setId(rp.getId());
                        dto.setRoomId(rp.getRoom().getRoomId());
                        dto.setRatePlanCode(rp.getRatePlanCode());
                        dto.setMealPlanName(rp.getMealPlanName());
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ApiResponse.<List<RatePlan>>builder()
                    .status(SUCCESS)
                    .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                    .failureMessage("Rate plans fetched successfully.")
                    .data(dtoList)
                    .build();

        } catch (Exception ex) {
            log.error("Error fetching rate plans for roomId={}", roomId, ex);
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DB_ERROR,
                    "Error occurred while fetching rate plans."
            );
        }
    }
}
