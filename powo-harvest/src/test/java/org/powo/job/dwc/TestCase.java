package org.powo.job.dwc;

import com.google.common.base.MoreObjects;

public class TestCase {
	public String fieldName;
	public String value;
	public Object expected;
	public String propertyName;

	public TestCase(String fieldName, String value) {
		this.fieldName = fieldName;
		this.value = value;
	}

	public TestCase sets(Object expected) {
		this.expected = expected;
		return this;
	}

	public TestCase on(String propertyName) {
		this.propertyName = propertyName;
		return this;
	}

	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("fieldName", fieldName)
				.add("value", value)
				.add("expected", expected)
				.add("property", propertyName)
				.omitNullValues()
				.toString();
	}
}
