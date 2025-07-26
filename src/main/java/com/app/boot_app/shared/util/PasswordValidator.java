package com.app.boot_app.shared.util;

public class PasswordValidator {

    public static boolean isValidPassword(String value) {
        if (value == null) return false;

        boolean hasUpperCase = value.matches(".*[A-Z].*");
        boolean hasLowerCase = value.matches(".*[a-z].*");
        boolean hasNumber = value.matches(".*[0-9].*");
        boolean hasSymbol = value.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        boolean isLongEnough = value.length() >= 5;

        return hasUpperCase && hasLowerCase && hasNumber && hasSymbol && isLongEnough;
    }
}