package org.emonocot.portal.format;

import java.text.ParseException;
import java.util.Locale;

import org.emonocot.api.Sorting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;

/**
 * 
 * @author jk00kg
 * 
 */
public class SortingFormatter implements Formatter<Sorting> {

    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(SortingFormatter.class);

    /**
     * @param sort Set the sort
     * @param locale Set the locale
     * @return a string version of the sort
     */
    public final String print(final Sorting sort, final Locale locale) {
        return sort.toString();
    }

    /**
     * @param sortParam the string to parse
     * @param locale the locale
     * @return a sorting object
     * @throws ParseException if there is a problem parsing the string
     */
    public final Sorting parse(final String sortParam, final Locale locale)
            throws ParseException {
        if (sortParam == null) {
            return null;
        } else if (-1 == sortParam.indexOf(".")) {
            throw new ParseException(
                    sortParam + " is not a valid sort request", 0);
        } else {
            String fieldName = sortParam.substring(0, sortParam.indexOf("."));
            String sortDirection = sortParam
                    .substring(sortParam.indexOf(".") + 1);
            Sorting sort;
            if (fieldName.length() > 0) {
                sort = new Sorting(fieldName);
            } else {
                sort = new Sorting(null);
            }
            try {
                sort.setDirection(Sorting.SortDirection.valueOf(sortDirection));
            } catch (IllegalArgumentException e) {
                logger.error(sortDirection
                        + "is not a valid direction for the sort", e);
                e.printStackTrace();
            }
            return sort;
        }
    }

}
