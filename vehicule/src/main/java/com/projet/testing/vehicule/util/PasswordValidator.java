package com.projet.testing.vehicule.util;

import java.util.List;

import org.springframework.stereotype.Component;


/**
 * The type Password validator.
 */
@Component
public class PasswordValidator {

    /**
     * Is password valid boolean.
     *
     * @param password the password
     * @return the boolean
     */
    public boolean isPasswordValid(String password) {
	    boolean hasUpper = false, hasLower = false, hasDigit = false;
	    for (char c : password.toCharArray()) {
	        if (Character.isUpperCase(c)) hasUpper = true;
	        else if (Character.isLowerCase(c)) hasLower = true;
	        else if (Character.isDigit(c)) hasDigit = true;
	    }

	    List<String> forbiddenWords = List.of("password", "123456", "admin");
	    for (String word : forbiddenWords) {
	        if (password.toLowerCase().contains(word)) return false;
	    }

	    return hasUpper && hasLower && hasDigit ;
	}
	

}
