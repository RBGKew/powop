package org.openarchives.pmh.format;

import java.text.ParseException;
import java.util.Locale;

import org.openarchives.pmh.SetSpec;
import org.springframework.format.Formatter;

/**
 *
 * @author ben
 *
 */
public class SetSpecFormatter implements Formatter<SetSpec> {

    @Override
    public final String print(final SetSpec setSpec, final Locale locale) {
        if (setSpec == null) {
            return null;
        } else {
            return setSpec.getValue();
        }
    }

    @Override
    public final SetSpec parse(final String text, final Locale locale)
            throws ParseException {
        if (text == null) {
            return null;
        } else {
            return new SetSpec(text);
        }
    }
}
