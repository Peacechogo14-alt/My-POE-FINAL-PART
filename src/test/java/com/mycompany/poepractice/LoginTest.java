package com.mycompany.poepractice;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testUsernameCorrectlyFormatted() {
        Login l = new Login();
        l.setUsername("kyl_1");
        assertTrue(l.checkUserName());
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login l = new Login();
        l.setUsername("kyle!!!!!!!");
        assertFalse(l.checkUserName());
    }

    @Test
    public void testPasswordMeetsComplexity() {
        Login l = new Login();
        l.setPassword("Ch&&sec@ke99!");
        assertTrue(l.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        Login l = new Login();
        l.setPassword("password");
        assertFalse(l.checkPasswordComplexity());
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        Login l = new Login();
        l.setCellNumber("+27817123163");
        assertTrue(l.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        Login l = new Login();
        l.setCellNumber("08966553");
        assertFalse(l.checkCellPhoneNumber());
    }

    @Test
    public void testRegisterUserSuccessAndLogin() {
        Login l = new Login();
        l.setName("Jane Smith");
        l.setUsername("jan_1");
        l.setPassword("Aa1!aaaaa");
        String reg = l.registerUser();
        assertEquals("User has been registered successfully.", reg);

        boolean loginOk = l.loginUser("jan_1", "Aa1!aaaaa");
        assertTrue(loginOk);
        assertEquals("Welcome Jane, Smith it is great to see you again.", l.returnLoginStatus());
    }

    @Test
    public void testRegisterUserUsernameInvalidMessage() {
        Login l = new Login();
        l.setUsername("kyle!!!!!!!");
        l.setPassword("Ch&&sec@ke99!");
        assertEquals("The username is incorrectly formatted.", l.registerUser());
    }

    @Test
    public void testRegisterUserPasswordInvalidMessage() {
        Login l = new Login();
        l.setUsername("kyl_1");
        l.setPassword("password");
        assertEquals("The password does not meet the complexity requirements.", l.registerUser());
    }
}
