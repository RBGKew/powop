package org.emonocot.test.radish;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author ben
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE })
public @interface Results {
   /**
    *
    */
    String value();
}
