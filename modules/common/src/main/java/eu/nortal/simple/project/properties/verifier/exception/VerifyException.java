package eu.nortal.simple.project.properties.verifier.exception;


/**
 * @author Margus Hanni <margus.hanni@nortal.com>
 */
public class VerifyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VerifyException(String message) {
        super(message);
    }

}
