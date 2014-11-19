/**
 * 
 */
package org.emonocot.harvest.common;

import java.util.List;

import org.emonocot.model.BaseData;
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
                if(ItemStream.class.isAssignableFrom(currentDelegate.getClass())) {
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
        logger.debug("Returning " + item.getClass().getSimpleName() + " " + item);
        return item;
    }

}
