package com.app.boot_app.domain.auth.enums;

import com.app.boot_app.domain.auth.constant.Constant;
import com.app.boot_app.domain.locale.EnumI18nHelper;

public enum GroupName {
    DEFAULT_GROUP(Constant.DEFAULT_GROUP_NAME, Constant.DEFAULT_GROUP_DESCRIPTION);

    private final String name;
    private final String description;

    private static EnumI18nHelper i18nHelper;

    GroupName(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static GroupName fromName(String name) {
        for (GroupName groupName : values()) {
            if (groupName.name.equalsIgnoreCase(name)) {
                return groupName;
            }
        }
        throw new IllegalArgumentException(i18nHelper.getLocalizedMessage("group.name.invalid", new Object[]{name}));
    }
}
