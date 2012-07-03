package org.emonocot.harvest.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.input.BOMInputStream;
import org.springframework.batch.item.file.BufferedReaderFactory;
import org.springframework.core.io.Resource;

public class BOMIgnoringBufferedReaderFactory implements BufferedReaderFactory {

	@Override
	public BufferedReader create(Resource resource, String encoding)
			throws UnsupportedEncodingException, IOException {
		BOMInputStream bomInputStream = new BOMInputStream(resource.getInputStream());
		return new BufferedReader(new InputStreamReader(bomInputStream, encoding));
	}
	
}