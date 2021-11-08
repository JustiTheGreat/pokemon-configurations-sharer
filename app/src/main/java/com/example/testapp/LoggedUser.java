package com.example.testapp;

public class LoggedUser {
    private static String username;
    private static String password;

    public static void setUsername(String username) {
        LoggedUser.username = username;
    }

    public static void setPassword(String password) {
        LoggedUser.password = password;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}
