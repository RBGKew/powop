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
package org.powo.harvest.common;

import java.util.List;

import org.powo.model.BaseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

/**
 * @author jk00kg
 * This class is intended for allowing a small number of readers to be used in a similar manner to the {@link MultiResourceItemReader}
 * with the difference of supporting different file structures by allowing different {@link LineMapper}s etc. to be used
 * N.B. Not designed for use with
 */
public class CompositeItemReader<T> implements ItemReader<T> {

	private Logger logger = LoggerFactory.getLogger(CompositeItemReader.class);

	private ItemReader<T> currentDelegate;

	private List<ItemReader<T>> delegates;

	private Class<? extends BaseData> targetType;

	@Autowired
	private ConversionService conversionService;

	public List<ItemReader<T>> getDelegates() {
		return delegates;
	}

	public void setDelegates(List<ItemReader<T>> delegates) {
		this.delegates = delegates;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setTargetType(Class targetType) {
		this.targetType = targetType;
	}

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public T read() throws Exception, UnexpectedInputException, ParseException,
	NonTransientResourceException {
		T item;
		//TODO Separate two steps and refactor
		if(currentDelegate == null || (item = currentDelegate.read()) == null) {
			logger.debug("Loading new item reader");
			try {
				if(currentDelegate != null && ItemStream.class.isAssignableFrom(currentDelegate.getClass())) {
					((ItemStream) currentDelegate).close();
				}
				currentDelegate = delegates.remove(0);
				logger.info("The current ItemReader is now " +  currentDelegate);
				if(currentDelegate != null && ItemStream.class.isAssignableFrom(currentDelegate.getClass())) {
					((ItemStream) currentDelegate).open(new ExecutionContext());
				}
			} catch (IndexOutOfBoundsException e) {
				//if there are no more delegates
				logger.info("No more readers, returning null");
				return null;
			}
			item = read();
		}
		//If targetType is null use the first delegate to define the expected type
		if (targetType == null) {
			targetType = (Class) item.getClass();
		} else {
			if(!targetType.isInstance(item)) {
				logger.debug("Attempting to cast " + item + " to " + targetType);
				item = (T) conversionService.convert(item, targetType);
			}
		}
		logger.debug("Returning " + item);
		return item;
	}

}
