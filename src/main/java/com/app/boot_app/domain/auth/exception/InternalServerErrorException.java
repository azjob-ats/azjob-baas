package com.app.boot_app.domain.auth.exception;
import com.app.boot_app.shared.exeception.BaseHttpException;

public class InternalServerErrorException extends BaseHttpException {
    public InternalServerErrorException(String code, String friendlyMessage) {
        super(code, friendlyMessage);
    }
}