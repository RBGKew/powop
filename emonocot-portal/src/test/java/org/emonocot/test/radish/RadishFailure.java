package org.emonocot.test.radish;

import cucumber.runtime.model.CucumberScenario;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.Step;

/**
 *
 * @author ben
 *
 */
public class RadishFailure extends Throwable {

    /**
     *
     */
    private String message;

    /**
     *
     * @param actualScenario Set the actual scenario
     * @param scenario Set the scenario
     * @param expected Set the expected step
     * @param actual Set the actual step
     * @param line Set the line
     */
    public RadishFailure(final CucumberScenario scenario,
            final CucumberScenario actualScenario, final Step expected,
            final Step actual, final int line) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failure at line " + line + "\n");
        int i = 0;
        for (i = 0; i < line; i++) {
            Step s = actualScenario.getSteps().get(i);
            stringBuilder.append("  " + s.getKeyword() + " " + s.getName() + "\n");
        }
        if (actual == null) {
            for (; i < scenario.getSteps().size(); i++) {
                Step s = scenario.getSteps().get(i);
                stringBuilder.append("- " + s.getKeyword() + " " + s.getName() + "\n");
            }
        } else {
            for (; i < actualScenario.getSteps().size(); i++) {
                Step s = actualScenario.getSteps().get(i);
                stringBuilder.append("+ " + s.getKeyword() + " " + s.getName() + "\n");
            }
        }
        message = stringBuilder.toString();
    }

    @Override
    public final String getMessage() {
        return message;
    }

}
