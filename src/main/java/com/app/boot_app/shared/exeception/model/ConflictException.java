package com.app.boot_app.shared.exeception.model;
import com.app.boot_app.shared.exeception.BaseHttpException;

public class ConflictException extends BaseHttpException {
    public ConflictException(String code, String friendlyMessage) {
        super(code, friendlyMessage);
    }
}