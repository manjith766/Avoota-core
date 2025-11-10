package com.neoteric.common.exception;

import com.neoteric.common.ui.AvootaResponseStatus;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final AvootaResponseStatus.FailureCode failureCode;

    // Constructor: called when you throw this exception
    public CustomException(AvootaResponseStatus.FailureCode failureCode, String string) {
        super(failureCode.getMessage()); // passes message to RuntimeException
        this.failureCode = failureCode;
    }

    public CustomException(AvootaResponseStatus.FailureCode failureCode) {
        super(failureCode.getMessage());
        this.failureCode = failureCode;
    }
}
