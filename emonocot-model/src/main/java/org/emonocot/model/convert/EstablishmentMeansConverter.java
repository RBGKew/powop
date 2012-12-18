package org.emonocot.model.convert;

import org.gbif.ecat.voc.EstablishmentMeans;
import org.springframework.core.convert.converter.Converter;

public class EstablishmentMeansConverter implements
		Converter<String, EstablishmentMeans> {

	@Override
	public EstablishmentMeans convert(String source) {
		if(source == null || source.isEmpty()) {
			return null;
		} 
		switch(source) {
		case "native":
			return EstablishmentMeans.Native;
		case "introduced":
			return EstablishmentMeans.Introduced;
		case "naturalised":
			return EstablishmentMeans.Naturalised;
		case "invasive":
			return EstablishmentMeans.Invasive;
		case "managed":
			return EstablishmentMeans.Managed;
		case "uncertain":
			return EstablishmentMeans.Uncertain;
		default:
			return EstablishmentMeans.valueOf(source);
		}
	}

}
