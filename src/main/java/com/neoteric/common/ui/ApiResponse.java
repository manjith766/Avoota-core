package com.neoteric.common.ui;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private AvootaResponseStatus status;     // SUCCESS / FAILURE
    private String failureCode;              // e.g., AVE1001
    private String failureMessage;           // readable error
    private T data;                          // actual payload

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(AvootaResponseStatus.SUCCESS)
                .failureCode(AvootaResponseStatus.FailureCode.NONE.getCode())
                .failureMessage("Success")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> failure(AvootaResponseStatus.FailureCode code) {
        return ApiResponse.<T>builder()
                .status(AvootaResponseStatus.FAILURE)
                .failureCode(code.getCode())
                .failureMessage(code.getMessage())
                .build();
    }

    public static <T> ApiResponse<T> failure(AvootaResponseStatus.FailureCode code, String customMsg) {
        return ApiResponse.<T>builder()
                .status(AvootaResponseStatus.FAILURE)
                .failureCode(code.getCode())
                .failureMessage(customMsg)
                .build();
    }
}
