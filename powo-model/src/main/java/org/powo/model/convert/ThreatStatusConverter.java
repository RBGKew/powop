package org.powo.model.convert;

import static org.gbif.ecat.voc.ThreatStatus.*;
import org.gbif.ecat.voc.ThreatStatus;
import org.springframework.core.convert.converter.Converter;

public class ThreatStatusConverter implements Converter<String, ThreatStatus> {

	@Override
	public ThreatStatus convert(String status) {
		switch (status.toLowerCase()) {
		case ("leastconcern"): return Least_Concern;
		case ("nearthreatened"): return Near_Threatened;
		case ("vulnerable"): return Vulnerable;
		case ("endangered"): return Endangered;
		case ("criticallyendangered"): return Critically_Endangered;
		case ("extinctinthewild"): return Extinct_In_The_Wild;
		case ("extinct"): return Extinct;
		case ("datadeficient"): return Data_Deficient;
		default: return ThreatStatus.valueOf(status);
		}
	}

}
