package org.emonocot.harvest.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hsqldb.Types;
import org.springframework.jdbc.core.RowMapper;

public class RowToMapRowMapper implements RowMapper<Map<String,String>> {

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
			  throw new RuntimeException("SQL type " + type + " not handled");
		  }
		}
		return row;
	}

}
