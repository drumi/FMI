package com;

public class ValidateInput {

    public static boolean validateUsername(String username) {
        return username.matches("\\w{2,}");
    }

    public static boolean validateEmail(String email) {
        return email.matches("\\w+@\\w+\\.\\w+");
    }

    public static boolean validatePhone(String phone) {
        return phone.matches("\\(\\d{4}\\)\\(\\d{7}\\)");
    }
}

