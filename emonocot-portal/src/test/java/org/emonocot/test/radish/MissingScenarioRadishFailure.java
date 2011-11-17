package org.emonocot.test.radish;

import cucumber.runtime.model.CucumberScenario;

/**
 *
 * @author ben
 *
 */
public class MissingScenarioRadishFailure extends Throwable {

    /**
    *
    */
    private static final long serialVersionUID = 2397055813532482356L;

    /**
     *
     */
    private String message;

    /**
     *
     * @param scenario Set the expected  scenario
     */
    public MissingScenarioRadishFailure(final CucumberScenario scenario) {
        this.message = "Expected scenario: " + scenario.getVisualName();
    }

    /**
     *
     * @param scenario Set the expected scenario
     * @param actualScenario Set the actual scenario
     */
    public MissingScenarioRadishFailure(final CucumberScenario scenario,
            final CucumberScenario actualScenario) {
        this.message = "Expected scenario: " + scenario.getVisualName()
                + " but was " + actualScenario.getVisualName();
    }

    @Override
    public final String getMessage() {
        return message;
    }
}
