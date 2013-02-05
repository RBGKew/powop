package org.emonocot.portal.feature;

import org.emonocot.test.Listener;
import org.emonocot.test.ListeningCucumber;
import org.emonocot.test.TakeScreenshotListener;
import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;

/**
 *
 * @author ben
 *
 */
@RunWith(ListeningCucumber.class)
@Listener(TakeScreenshotListener.class)
@Cucumber.Options(features = "src/test/resources/features/Authentication.feature", glue = "org.emonocot.portal.steps")
public class AuthenticationFunctionalTest {

}
