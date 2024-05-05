package com.group48.blackbox;

public class UsersManager {
    private static String username;
    
    public static String getUsername()
    {
        return username;
    }
    
    public static void setUsername(String newUsername)
    {
        username = newUsername;
    }
}
