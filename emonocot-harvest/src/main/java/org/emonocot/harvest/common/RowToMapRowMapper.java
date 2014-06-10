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
