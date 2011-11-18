package org.emonocot.test.radish;

import static java.util.Arrays.asList;
import gherkin.formatter.model.Step;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import cucumber.resources.Consumer;
import cucumber.resources.Resources;
import cucumber.runtime.CucumberException;
import cucumber.runtime.FeatureBuilder;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberTagStatement;

/**
 *
 * @author ben
 *
 */
public class Radish extends Suite {

    /**
     *
     */
    private final String featurePath;

    /**
     *
     */
    private final CucumberFeature feature;

    /**
     *
     */
    private final CucumberFeature[] results;

    /**
     *
     */
    private final String resultsPath;

    /**
     *
     */
    private final Description testSuiteDescription;

    private final Class<?> clazz;

    /**
     * Constructor called by JUnit.
     * @param clazz Set the class
     * @throws InitializationError if there is a problem initializing the test
     * @throws IOException if there is a problem opening the files
     */
    public Radish(final Class clazz) throws InitializationError, IOException {
        super(clazz, new ArrayList<Runner>());
        assertNoDeclaredMethods(clazz);
        this.clazz = clazz;
        featurePath = getFeaturePath(clazz);
        feature = parseFeature(featurePath, filters(clazz));
        testSuiteDescription = Description.createSuiteDescription(clazz);

        resultsPath = getResultsPath(clazz);
        PathMatchingResourcePatternResolver resolver
            = new PathMatchingResourcePatternResolver();
        Resource[] resultsResources = resolver.getResources(resultsPath);
        results = new CucumberFeature[resultsResources.length];

        Resource classpathRoot = new ClassPathResource("/");
        for (int i = 0; i < results.length; i++) {
            Resource resultResource = resultsResources[i];
            String resultPath = resultResource
                    .getFile()
                    .getAbsolutePath()
                    .substring(
                            classpathRoot.getFile()
                                .getAbsolutePath().length() + 1);
            results[i] = parseFeature(resultPath, filters(clazz));
        }
    }

    private Long[] toLong(long[] primitiveLongs) {
        Long[] longs = new Long[primitiveLongs.length];
        for (int i = 0; i < primitiveLongs.length; i++) {
            longs[i] = primitiveLongs[i];
        }
        return longs;
    }

    private List<Object> filters(Class clazz) {
        cucumber.junit.Feature featureAnnotation = (cucumber.junit.Feature) clazz.getAnnotation(cucumber.junit.Feature.class);
        Object[] filters = new Object[0];
        if (featureAnnotation != null) {
            filters = toLong(featureAnnotation.lines());
            if (filters.length == 0) {
                filters = featureAnnotation.tags();
            }
        }
        return asList(filters);
    }

    /**
     *
     * @param pathName
     * @param filters
     * @return
     */
    private CucumberFeature parseFeature(String pathName, final List<Object> filters) {
        List<CucumberFeature> cucumberFeatures = new ArrayList<CucumberFeature>();
        final FeatureBuilder builder = new FeatureBuilder(cucumberFeatures);
        Resources.scan(pathName, new Consumer() {
            public void consume(cucumber.resources.Resource resource) {
                builder.parse(resource, filters);
            }
        });

        if (cucumberFeatures.isEmpty()) {
            return null;
        } else {
            return cucumberFeatures.get(0);
        }
    }

    /**
     *
     * @param clazz Set the class
     * @return the path to the feature file
     */
    private String getFeaturePath(final Class clazz) {
        cucumber.junit.Feature featureAnnotation = (cucumber.junit.Feature) clazz
                .getAnnotation(cucumber.junit.Feature.class);
        String pathName;
        if (featureAnnotation != null) {
            pathName = featureAnnotation.value();
        } else {
            pathName = clazz.getName().replace('.', '/') + ".feature";
        }
        return pathName;
    }

    /**
    *
    * @param clazz Set the class
    * @return the path to the feature file
    */
   private String getResultsPath(final Class clazz) {
       Results resultsAnnotation = (Results) clazz
               .getAnnotation(Results.class);
       String pathName;
       if (resultsAnnotation != null) {
           pathName = resultsAnnotation.value();
       } else {
           pathName = clazz.getName().replace('.', '/') + ".results";
       }
       return pathName;
   }

   /**
    * @param notifier Set the run notifier
    */
    @Override
    public final void run(final RunNotifier notifier) {
        if (featurePath != null && resultsPath != null) {

            for (int i = 0; i < feature.getFeatureElements().size(); i++) {

                CucumberTagStatement tagStatement = feature
                        .getFeatureElements().get(i);

                if (!(tagStatement.getClass().equals(CucumberScenario.class))) {
                    break;
                }
                CucumberScenario scenario = (CucumberScenario) tagStatement;
                Description description = Description.createTestDescription(
                        clazz, scenario.getVisualName());
                testSuiteDescription.addChild(description);

                Throwable[] failures = new Throwable[results.length];
                boolean failed = false;
                for (int k = 0; k < results.length; k++) {
                    CucumberFeature result = results[k];
                    CucumberScenario actualScenario = null;
                    try {
                       actualScenario = (CucumberScenario) result
                            .getFeatureElements().get(i);
                    } catch (IndexOutOfBoundsException ioobe) {
                        failed = true;
                        failures[k] = new MissingScenarioRadishFailure(scenario);
                        break;
                    }
                    if(!scenario.getVisualName().equals(actualScenario.getVisualName())) {
                        failed = true;
                        failures[k] = new MissingScenarioRadishFailure(scenario, actualScenario);
                        break;
                    }
                    for (int j = 0; j < Math.max(scenario.getSteps().size(),
                            actualScenario.getSteps().size()); j++) {
                        Step step = null;
                        Step actualStep = null;
                        try {
                            step = scenario.getSteps().get(j);
                        } catch (IndexOutOfBoundsException ioobe) {
                            step = null;
                        }
                        try {
                            actualStep = actualScenario.getSteps().get(j);
                        } catch (IndexOutOfBoundsException ioobe) {
                            actualStep = null;
                        }

                        if (!compareStep(step, actualStep)) {
                            failed = true;
                            failures[k] = new RadishFailure(scenario,
                                    actualScenario, step, actualStep, j);
                            break;
                        }
                    }
                }

                if (results.length > 0) {
                    notifier.fireTestStarted(description);
                    if (failed) {
                       notifier.fireTestFailure(new Failure(description,
                            new CompositeRadishFailure(failures)));
                    }
                    notifier.fireTestFinished(description);
                }
            }

            super.run(notifier);

        }
    }

    /**
     *
     * @param expected Set the expected step
     * @param actual Set the actual step
     * @return true if the steps are the same
     */
    private boolean compareStep(final Step expected, final Step actual) {
      if (expected == null && actual != null) {
          return false;
      }
      if (expected != null && actual == null) {
          return false;
      }
      if (!expected.getKeyword().equals(actual.getKeyword())) {
          return false;
      }
      if (!expected.getName().equals(actual.getName())) {
          return false;
      }
      return true;
}

    /**
     *
     * @param clazz Set the class
     */
    private void assertNoDeclaredMethods(final Class clazz) {
        if (clazz.getDeclaredMethods().length != 0) {
            throw new CucumberException(
                    "\n\n"
                            + "Classes annotated with @RunWith(Cucumber.class) must not define any methods.\n"
                            + "Their sole purpose is to serve as an entry point for JUnit.\n"
                            + "Step Definitions should be defined in their own classes.\n"
                            + "This allows them to be reused across features.\n"
                            + "Offending class: " + clazz + "\n"
            );
        }
    }
}
