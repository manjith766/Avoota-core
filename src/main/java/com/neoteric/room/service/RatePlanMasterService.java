package com.neoteric.room.service;

import com.neoteric.common.ui.ApiResponse;
import com.neoteric.common.ui.AvootaResponseStatus;
import com.neoteric.common.ui.AvootaUtil;
import com.neoteric.room.entity.RatePlanMasterEntity;
import com.neoteric.room.repository.RatePlanMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.neoteric.common.ui.AvootaResponseStatus.SUCCESS;

@Slf4j
@Service
public class RatePlanMasterService {

    private final RatePlanMasterRepository ratePlanRepo;

    public RatePlanMasterService(RatePlanMasterRepository ratePlanRepo) {
        this.ratePlanRepo = ratePlanRepo;
    }

    // Save or Update RatePlanMaster
    public ApiResponse<String> saveRatePlan(RatePlanMasterEntity ratePlan) {
        log.info("Saving rate plan: {}", ratePlan.getRatePlanCode());
        try {
            ratePlanRepo.save(ratePlan);
            return ApiResponse.<String>builder()
                    .status(SUCCESS)
                    .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                    .failureMessage("Rate plan saved successfully.")
                    .data("Rate Plan Code: " + ratePlan.getRatePlanCode())
                    .build();
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate Rate Plan Code: {}", ratePlan.getRatePlanCode());
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DUPLICATE_ENTRY,
                    "Rate Plan Code already exists: " + ratePlan.getRatePlanCode()
            );
        } catch (Exception ex) {
            log.error("Error while saving Rate Plan: {}", ex.getMessage(), ex);
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DB_ERROR,
                    "Error occurred while saving rate plan."
            );
        }
    }

    //  Get All RatePlans
    public ApiResponse<List<RatePlanMasterEntity>> getAllRatePlans() {
        log.info("Fetching all rate plans...");
        try {
            List<RatePlanMasterEntity> plans = ratePlanRepo.findAll();
            if (plans.isEmpty()) {
                return AvootaUtil.failure(
                        AvootaResponseStatus.FailureCode.NO_RESULTS_FOUND,
                        "No rate plans found in the system."
                );
            }
            return ApiResponse.<List<RatePlanMasterEntity>>builder()
                    .status(SUCCESS)
                    .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                    .failureMessage("Rate plans fetched successfully.")
                    .data(plans)
                    .build();
        } catch (Exception ex) {
            log.error("Error fetching rate plans: {}", ex.getMessage(), ex);
            return AvootaUtil.failure(
                    AvootaResponseStatus.FailureCode.DB_ERROR,
                    "Error occurred while fetching rate plans."
            );
        }
    }
}
