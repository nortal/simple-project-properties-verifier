package eu.nortal.simple.project.properties.verifier.exception;

/**
 * @author Margus Hanni <margus.hanni@nortal.com>
 */
public class RunException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RunException(Exception ex) {
        super(ex);
    }

    public RunException(String message) {
        super(message);
    }
}
