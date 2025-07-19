package com.app.boot_app.domain.locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class EnumI18nHelper {

    private final MessageSource messageSource;

    public EnumI18nHelper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getLocalizedMessage(String key, Object[] args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}
