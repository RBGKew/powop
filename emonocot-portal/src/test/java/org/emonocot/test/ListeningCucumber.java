package org.emonocot.test;

import java.io.IOException;

import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import cucumber.api.junit.Cucumber;

/**
 *
 * @author ben
 *
 */
public class ListeningCucumber extends Cucumber {
    /**
     *
     */
    private RunListener listener;

    /**
     *
     * @param clazz Set the feature class
     * @throws InitializationError if there is a problem initializing the test
     * @throws IOException if there is a problem reading a file
     */
    public ListeningCucumber(final Class clazz)
            throws InitializationError, IOException {
        super(clazz);
        Listener listenerAnnotation = (Listener) clazz
                .getAnnotation(Listener.class);
        if (listenerAnnotation != null) {
            try {
                listener = (RunListener) listenerAnnotation.value()
                        .newInstance();
            } catch (InstantiationException ie) {
                throw new InitializationError(ie);
            } catch (IllegalAccessException iae) {
                throw new InitializationError(iae);
            }
        }
    }

    @Override
    public final void run(final RunNotifier notifier) {
        if (listener != null) {
            notifier.addListener(listener);
        }
        super.run(notifier);
    }

}
