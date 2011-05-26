package org.emonocot.harvest.common;

import org.springframework.batch.core.ExitStatus;

/**
 *
 * @author ben
 *
 */
public class DelayingBean {

    /**
     *
     * @param delay The duration to delay by, in milliseconds
     * @return an exit status
     */
    public final ExitStatus wait(final String delay) {

        try {
            Thread.sleep(Long.parseLong(delay));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(delay
                    + " is not a valid delay time", e);
        } catch (InterruptedException e) {
        }

        return ExitStatus.COMPLETED;
    }

}
