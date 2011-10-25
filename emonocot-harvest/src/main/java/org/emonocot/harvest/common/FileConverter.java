package org.emonocot.harvest.common;

import java.io.File;
import java.io.IOException;

import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

/**
 * Required due to SPR-6564 Spring ConversionService breaks String-to-Resource
 * resolution. TODO Remove once this is resolved (e.g. 3.1 GA release)
 *
 * @author ben
 *
 */
public class FileConverter implements Converter<String, File> {

    /**
     *
     */
    private ResourceEditor editor = new ResourceEditor();

    /**
     * @param text Set the value
     * @return a file
     */
    public final File convert(final String text) {
        editor.setAsText(text);
        try {
            return ((Resource) editor.getValue()).getFile();
        } catch (IOException e) {
            throw new ConversionFailedException(
                    TypeDescriptor.valueOf(String.class),
                    TypeDescriptor.valueOf(File.class), text, e);
        }
    }

}
