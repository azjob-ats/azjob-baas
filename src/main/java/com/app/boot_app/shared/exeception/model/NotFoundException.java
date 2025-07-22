package com.app.boot_app.shared.exeception.model;
import com.app.boot_app.shared.exeception.BaseHttpException;

public class NotFoundException extends BaseHttpException {
    public NotFoundException(String code, String friendlyMessage) {
        super(code, friendlyMessage);
    }
}