package org.emonocot.model.geography;

import java.util.Comparator;

public class SampleData {
	public String id;
	public String code;
	public String name;
	public String shape;

	public SampleData(String line) {
		String[] vals = line.split("\t");
		id = vals[0];
		code = vals[1];
		name = vals[2];
		shape = vals[3];
	}

	public static Comparator<SampleData> NAME_ORDER = new Comparator<SampleData>() {
		@Override
		public int compare(SampleData o1, SampleData o2) {
			return o1.name.compareTo(o2.name);
		}
	};

}
