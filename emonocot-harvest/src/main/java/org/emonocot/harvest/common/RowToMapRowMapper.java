/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.harvest.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class RowToMapRowMapper implements RowMapper<Map<String,String>> {
    
    private Logger logger = LoggerFactory.getLogger(RowToMapRowMapper.class);

	@Override
	public Map<String, String> mapRow(ResultSet resultSet, int rowNumber)
			throws SQLException {
		Map<String,String> row = new HashMap<String,String>();
		for(int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
		  String columnName = resultSet.getMetaData().getColumnName(i);
		  int type = resultSet.getMetaData().getColumnType(i);
		  switch(type) {
		  case Types.VARCHAR:
		      row.put(columnName, resultSet.getString(columnName));
		      break;
		  case Types.LONGVARCHAR:
		      row.put(columnName, resultSet.getString(columnName));
		      break;
		  default:
			  logger.warn("SQL type " + type + " not recognised for column " + i);
              row.put(columnName, resultSet.getString(columnName));
		  }
		}
		return row;
	}

}
