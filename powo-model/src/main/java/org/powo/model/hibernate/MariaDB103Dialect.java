package org.powo.model.hibernate;

import org.hibernate.spatial.dialect.mysql.MySQL5InnoDBSpatialDialect;

/*
 * Copied from org.hibernate.dialect.PostgreSQL81Dialect.
 */
public class MariaDB103Dialect extends MySQL5InnoDBSpatialDialect {
	private static final long serialVersionUID = -949169585515637219L;

	@Override
	public boolean supportsSequences() {
		return true;
	}

	@Override
	public boolean supportsPooledSequences() {
		return true;
	}

	@Override
	public String getSequenceNextValString(String sequenceName) {
		return "select " + getSelectSequenceNextValString(sequenceName);
	}

	@Override
	public String getSelectSequenceNextValString(String sequenceName) {
		return "nextval (`" + sequenceName + "`)";
	}

	/*
	 * sequences must be prefixed with seq_ to be recognised
	 */
	@Override
	public String getQuerySequencesString() {
		return "select table_name from information_schema.tables where table_name like 'seq_%'";
	}
}