package org.emonocot.portal.acceptance;

import org.emonocot.test.radish.Radish;
import org.emonocot.test.radish.Results;
import org.junit.runner.RunWith;

import cucumber.junit.Feature;

/**
 *
 * @author ben
 *
 */
@RunWith(Radish.class)
@Feature("features/Authentication.feature")
@Results("acceptance/**/Authentication.feature")
public class AuthenticationAcceptanceTest {

}
