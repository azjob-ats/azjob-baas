package com.app.boot_app.domain.auth.enums;

import com.app.boot_app.domain.auth.constant.Constant;

public enum ProviderName {
    EMAIL_AND_PASSWORD_BY_GOOGLE(Constant.EMAIL_AND_PASSWORD_BY_GOOGLE),
    EMAIL_AND_PASSWORD_BY_AZJOB(Constant.EMAIL_AND_PASSWORD_BY_AZJOB);

    private final String name;

    ProviderName(String name) {
        this.name = name;;
    }

    public String getName() {
        return name;
    }

    public static ProviderName fromName(String name) {
        for (ProviderName providerName : values()) {
            if (providerName.name.equalsIgnoreCase(name)) {
                return providerName;
            }
        }
        throw new IllegalArgumentException("Provider name invalid: " + name);
    }
}
