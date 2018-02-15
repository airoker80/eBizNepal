package com.paybyonline.ebiz.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mefriend24 on 1/31/17.
 */
public class PasswordValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!]).{8,})";


    /*    (			# Start of group
            (?=.*\d)		#   must contains one digit from 0-9
            (?=.*[a-z])		#   must contains one lowercase characters
            (?=.*[A-Z])		#   must contains one uppercase characters
            (?=.*[@#$%])		#   must contains one special symbols in the list "@#$%"
            .		#     match anything with previous condition checking
    {8,}	#        length at least * characters and more
            )			# End of group*/


    public PasswordValidator(){
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    /**
     * Validate password with regular expression
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean validate(final String password){

        matcher = pattern.matcher(password);
        return matcher.matches();

    }

}
