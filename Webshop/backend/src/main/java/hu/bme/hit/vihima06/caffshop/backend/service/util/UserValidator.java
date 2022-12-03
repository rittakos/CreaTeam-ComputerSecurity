package hu.bme.hit.vihima06.caffshop.backend.service.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    public static boolean validateName(String name) {
        return name.trim().length() > 3;
    }

    public static boolean validateUsername(String username) {
        Pattern usernamePattern = Pattern.compile("^(?=.{4,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
        Matcher usernameMatcher = usernamePattern.matcher(username);

        return usernameMatcher.matches();
    }
}
