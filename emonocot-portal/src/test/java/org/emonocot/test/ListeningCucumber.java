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

import java.io.IOException;

import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import cucumber.api.junit.Cucumber;

/**
 *
 * @author ben
 *
 */
public class ListeningCucumber extends Cucumber {
	/**
	 *
	 */
	private RunListener listener;

	/**
	 *
	 * @param clazz Set the feature class
	 * @throws InitializationError if there is a problem initializing the test
	 * @throws IOException if there is a problem reading a file
	 */
	public ListeningCucumber(final Class clazz)
			throws InitializationError, IOException {
		super(clazz);
		Listener listenerAnnotation = (Listener) clazz
				.getAnnotation(Listener.class);
		if (listenerAnnotation != null) {
			try {
				listener = (RunListener) listenerAnnotation.value()
						.newInstance();
			} catch (InstantiationException ie) {
				throw new InitializationError(ie);
			} catch (IllegalAccessException iae) {
				throw new InitializationError(iae);
			}
		}
	}

	@Override
	public final void run(final RunNotifier notifier) {
		if (listener != null) {
			notifier.addListener(listener);
		}
		super.run(notifier);
	}

}
