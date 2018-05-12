package com.abualgait.abual.greatapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginUitilesTest {
    LoginUtilies lu = new LoginUtilies();
    @Test
    public void isValidTest() {

        assertEquals(true,lu.isValidEmail("a@b.com"));
    }


}