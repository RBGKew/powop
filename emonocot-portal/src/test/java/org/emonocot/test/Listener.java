package org.emonocot.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.runner.notification.RunListener;

/**
 *
 * @author ben
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE })
public @interface Listener {
   /**
    *
    */
    Class<? extends RunListener> value();
}
