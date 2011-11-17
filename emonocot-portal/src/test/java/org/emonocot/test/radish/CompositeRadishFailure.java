package org.emonocot.test.radish;

/**
 *
 * @author ben
 *
 */
public class CompositeRadishFailure extends Throwable {

    /**
     *
     */
    private static final long serialVersionUID = -8318399412753121556L;
    /**
     *
     */
    private String message;

    /**
     *
     * @param failures
     *            Set the failures
     */
    public CompositeRadishFailure(final Throwable[] failures) {
        StringBuilder stringBuilder = new StringBuilder();
        int numberOfFailures = 0;
        for (int i = 0; i < failures.length; i++) {
            if (failures[i] != null) {
                if (i != 0) {
                    stringBuilder.append("\n");
                }
                numberOfFailures++;
                stringBuilder.append(failures[i].getMessage());
            }
        }
        message = numberOfFailures + " out of " + failures.length + " acceptance tests failed\n"
                + stringBuilder.toString();
    }

    @Override
    public final String getMessage() {
        return message;
    }
}
