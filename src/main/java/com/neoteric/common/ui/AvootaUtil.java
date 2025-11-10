package com.neoteric.common.ui;

public final class AvootaUtil {

    private AvootaUtil(){}

        public static <T> ApiResponse<T> success(T data) {
            return ApiResponse.success(data);
        }

        public static <T> ApiResponse<T> failure(AvootaResponseStatus.FailureCode code) {
            return ApiResponse.failure(code);
        }

        public static <T> ApiResponse<T> failure(AvootaResponseStatus.FailureCode code, String customMsg) {
            return ApiResponse.failure(code, customMsg);
        }

        public static <T> ApiResponse<T> genericError() {
            return ApiResponse.failure(AvootaResponseStatus.FailureCode.GENERIC_ERROR);
        }
    }


