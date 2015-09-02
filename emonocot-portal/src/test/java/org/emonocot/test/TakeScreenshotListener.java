/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
		String screenshotName = null;
		try {
			File file = ((TakesScreenshot) WebDriverFacade.getWebDriver())
					.getScreenshotAs(OutputType.FILE);
			ClassPathResource root = new ClassPathResource("/");
			screenshotName = File.separator + "screenshots"
					+ File.separator
					+ failure.getDescription().getClassName() + "-"
					+ failure.getDescription().getMethodName() + ".png";
			screenshotName = screenshotName.replace(" ", "_");
			screenshotName = screenshotName.replace(":", "");
			screenshotName = screenshotName.replaceAll("_+", "_");

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
			System.err.println("Error writing screenshot "+screenshotName);
			e.printStackTrace();
		}
	}
}
