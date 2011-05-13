package org.openarchives.pmh;

/**
 *
 * @author ben
 *
 */
public class NoRecordsMatchException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1205909186178570527L;

    /**
     *
     * @param string Set the message
     */
    public NoRecordsMatchException(final String string) {
          super(string);
    }

}
