package com.app.boot_app.shared.exeception.model;
import com.app.boot_app.shared.exeception.BaseHttpException;

public class BadRequestException extends BaseHttpException {
    public BadRequestException(String code, String friendlyMessage) {
        super(code, friendlyMessage);
    }
}