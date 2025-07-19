package com.app.boot_app.shared.exeception;

import lombok.Getter;

@Getter
public abstract class BaseHttpException extends RuntimeException {
    private final String code;
    private final String friendlyMessage;

    public BaseHttpException(String code, String friendlyMessage) {
        super(friendlyMessage);
        this.code = code;
        this.friendlyMessage = friendlyMessage;
    }
}
