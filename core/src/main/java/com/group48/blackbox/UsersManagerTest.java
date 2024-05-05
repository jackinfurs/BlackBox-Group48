package com.group48.blackbox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UsersManagerTest {
    
    @Test
    public void testSetAndGetUsername()
    {
        UsersManager.setUsername("Alice");
        String retrievedUsername = UsersManager.getUsername();
        
        // Check that the retrieved username is as expected
        assertEquals("Alice", retrievedUsername, "The retrieved username should match the set username.");
    }
    
    @Test
    public void testUsernamePersistence()
    {
        // Set the username to one value
        UsersManager.setUsername("Bob");
        
        // Set the username to another value
        UsersManager.setUsername("Carol");
        
        // Get the username and check it's the last set value
        assertEquals("Carol", UsersManager.getUsername(), "The retrieved username should be the last set username.");
    }
    
    @Test
    public void testUsernameInitialValue()
    {
        // Check the initial value of username prior to setting it
        assertNull(UsersManager.getUsername(), "The initial value of username should be null.");
    }
}
