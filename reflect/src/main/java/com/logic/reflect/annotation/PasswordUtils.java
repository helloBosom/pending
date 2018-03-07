package com.logic.reflect.annotation;

import java.util.List;

public class PasswordUtils {

    @UserCase(id = 47, description = "Password must contain at least one numeric")
    private boolean validatePassword(String password) {
        return password.matches("\\w*\\d\\w*");
    }

    @UserCase(id = 48)
    public String encryptPassword(String password) {
        return new StringBuilder(password).reverse().toString();
    }

    @UserCase(id = 49, description = "New passwords can't equal previously used ones")
    public boolean checkForNewPassword(List<String> prevPasswords, String password) {
        return !prevPasswords.contains(password);
    }
}
