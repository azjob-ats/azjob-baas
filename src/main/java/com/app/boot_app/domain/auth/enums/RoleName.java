package com.app.boot_app.domain.auth.enums;

import com.app.boot_app.domain.auth.constant.Constant;
import com.app.boot_app.domain.locale.EnumI18nHelper;

public enum RoleName {
    USER(Constant.USER_NAME_ROLE, Constant.USER_DESCRIPTION_ROLE, 1),
    ADMIN(Constant.USER_NAME_ROLE, Constant.ADMIN_DESCRIPTION_ROLE, 10);

    private static EnumI18nHelper i18nHelper;

    private final String name;
    private final String description;
    private final Integer level;

    RoleName(String name, String description, Integer level) {
        this.name = name;
        this.description = description;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLevel() {
        return level;
    }

    public static RoleName fromName(String name) {
        for (RoleName roleName : values()) {
            if (roleName.name.equalsIgnoreCase(name)) {
                return roleName;
            }
        }
        throw new IllegalArgumentException(i18nHelper.getLocalizedMessage("role.name.invalid", new Object[]{name}));
    }
}
