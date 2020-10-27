package org.powo.common;

public class IdUtil {

	enum Type { Names }

	public static final String urnPrefix = "urn:lsid:";
	public static final String ipniPrefix = urnPrefix + "ipni.org";

	public static String fqName(String id) {
		return fq(id, Type.Names);
	}

	public static String idPart(String id) {
		if(id == null) return null;
		return id.startsWith(ipniPrefix) ? id.replaceAll(".*:", "") : id;
	}

	public static boolean isNameId(String id) {
		return id.startsWith(fq("", Type.Names));
	}

	private static String fq(String id, Type type) {
		if(id.startsWith(urnPrefix)) {
			return id;
		}

		return String.format("%s:%s:%s", 
				ipniPrefix,
				type.toString().toLowerCase(),
				id);
	}
}
