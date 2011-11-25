package org.emonocot.portal.feature;

import org.emonocot.test.Listener;
import org.emonocot.test.ListeningCucumber;
import org.emonocot.test.TakeScreenshotListener;
import org.junit.runner.RunWith;

import cucumber.junit.Feature;

/**
 *
 * @author ben
 *
 */
@RunWith(ListeningCucumber.class)
@Listener(TakeScreenshotListener.class)
@Feature(value = "features/SourcePage.feature",
        packages = "org.emonocot.portal.steps")
public class SourcePageFunctionalTest {

}
