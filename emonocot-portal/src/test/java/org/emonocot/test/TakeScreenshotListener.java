package org.emonocot.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.emonocot.portal.driver.WebDriverFacade;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author ben
 *
 */
public class TakeScreenshotListener extends RunListener {

    /**
     * @param failure Set the failure
     */
    public final void testFailure(final Failure failure) {
        try {
            File file = ((TakesScreenshot) WebDriverFacade.getWebDriver())
                    .getScreenshotAs(OutputType.FILE);
            ClassPathResource root = new ClassPathResource("/");
            String screenshotName = File.separator + "screenshots"
                    + File.separator
                    + failure.getDescription().getClassName() + "-"
                    + failure.getDescription().getMethodName() + ".png";
            screenshotName = screenshotName.replace(" ", "_");
            screenshotName = screenshotName.replace(":", "");

            File screenshotDirectory = new File(root.getFile().getParentFile()
                    .getAbsolutePath()
                    + File.separator + "screenshots" + File.separator);
            if (!screenshotDirectory.exists()) {
                screenshotDirectory.mkdir();
            }
            File screenshot = new File(root.getFile().getParentFile()
                    .getAbsolutePath()
                    + screenshotName);
            screenshot.createNewFile();
            FileUtils.copyFile(file, screenshot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
