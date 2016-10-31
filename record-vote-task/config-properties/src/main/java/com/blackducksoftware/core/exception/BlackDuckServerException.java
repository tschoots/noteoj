package com.blackducksoftware.core.exception;

/**
 * use BlackDuckServerException for exceptions that are programmer exceptions or can not be remediated.
 * BlackDuckServerException messages do not support i18n
 * 
 * @author ydeboeck
 * 
 */
public class BlackDuckServerException extends BlackDuckSoftwareException {

    public BlackDuckServerException(Throwable cause) {
        super(cause);
    }

    public BlackDuckServerException(String message) {
        super(message);
    }

    public BlackDuckServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
