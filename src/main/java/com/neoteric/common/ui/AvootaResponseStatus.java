package com.neoteric.common.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;

public enum AvootaResponseStatus {
    SUCCESS, FAILURE;

    @AllArgsConstructor
    @Getter
    public enum FailureCode {

        // 🔹 Common
        NONE("AVE000", "No Error"),
        GENERIC_ERROR("AVE100", "Something went wrong, please try again."),
        INVALID_INPUT("AVE101", "Invalid input provided."),
        DB_ERROR("AVE102", "Database operation failed."),
        UNAUTHORIZED("AVE103", "Unauthorized access."),
        SERVICE_UNAVAILABLE("AVE104", "Service temporarily unavailable."),

        // 🔹 Hotel Search
        LOCATION_NOT_FOUND("AVE200", "No hotels found for given location."),
        HOTEL_NOT_FOUND("AVE201", "Hotel not found."),
        NO_RESULTS_FOUND("AVE202", "No matching hotels found."),

        // 🔹 Room Details
        ROOM_NOT_FOUND("AVE300", "Room not found."),
        ROOM_UNAVAILABLE("AVE301", "Selected room is unavailable for the given dates."),
        RATEPLAN_NOT_FOUND("AVE302", "Rate plan not found."),
        MEALPLAN_NOT_FOUND("AVE303", "Meal plan not found."),

        // 🔹 Booking & Payment
        BOOKING_FAILED("AVE400", "Unable to complete booking."),
        PAYMENT_FAILED("AVE401", "Payment gateway error."),
        PAYMENT_INTEGRATION_ERROR("AVE402", "Payment integration issue."),

        // 🔹 Inventory Management
        INVENTORY_UPDATE_FAILED("AVE500", "Unable to update inventory."),
        INVENTORY_NOT_FOUND("AVE501", "Inventory not found."),
        RATE_UPDATE_FAILED("AVE502", "Unable to update rate."),
        INVALID_DATE_RANGE("AVE503", "Invalid check-in/check-out dates.");

        private final String code;
        private final String message;
    }
}
