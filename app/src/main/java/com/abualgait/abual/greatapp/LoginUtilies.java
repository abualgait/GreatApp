package com.abualgait.abual.greatapp;

public class LoginUtilies {
    public boolean isValidEmail(String email) {
        boolean isValid;

        if (email.contains("@")) {
            isValid = true;

        } else {
            isValid = false;
        }


        return isValid;
    }
}
