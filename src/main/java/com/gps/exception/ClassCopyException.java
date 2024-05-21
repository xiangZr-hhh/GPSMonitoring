package com.gps.exception;


import com.gps.utils.ErrorCode;

public class ClassCopyException extends BusinessException {
    public ClassCopyException() {
        super(ErrorCode.CLASS_COPY_EXCEPTION);
    }
}
