package org.emonocot.job.delete;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.emonocot.job.delete.ResourceIsNotDeletableException;

import java.sql.ResultSetMetaData;


public class ItemCheckerTasklet implements Tasklet{

	private static Logger logger = LoggerFactory.getLogger(ItemCheckerTasklet.class);

	private JdbcOperations jdbcTemplate;


	private String resource_id;


	public void setresource_id(String resource_id){
		this.resource_id = resource_id;
	}

	public void setJdbcTemplate(JdbcOperations jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	private Map<String, List<String>> GetTablesContainingIdColumn() throws SQLException{ //done
		Map<String, List<String>> columntablemap = new HashMap<String, List<String>>();
		String dbsql = "select database()";

		String databasename = jdbcTemplate.query(dbsql, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				return rs.next() ? rs.getString("database()") : null;

			}		
		});

		logger.debug("Database name is currently" + databasename);
		String tablesql = "select TABLE_NAME from information_schema.tables where table_schema = '" + databasename + "'" ;
		List<String> tablelist = jdbcTemplate.queryForList(tablesql, String.class);
		for(String table : tablelist){
			logger.debug("Table name is currently" + table);
			String columnsql = "select *  from " + table + " where 1=0";
			ResultSetMetaData md = jdbcTemplate.query(columnsql, new ResultSetExtractor<ResultSetMetaData>() {

				@Override
				public ResultSetMetaData extractData(ResultSet results) throws SQLException, DataAccessException {
					ResultSetMetaData md = results.getMetaData();
					return md;
				}

			});

			for (int i=1; i<=md.getColumnCount(); i++){
				String columnname = md.getColumnLabel(i);
				if(columnname.endsWith("_id")){
					logger.debug("Column name is currently" + columnname);
					if(columntablemap.containsKey(columnname)){
						logger.debug("adding column name");
						List<String> tableMapList = columntablemap.get(columnname);
						tableMapList.add(table);
						columntablemap.put(columnname, tableMapList);

					}else{
						List<String> tableMapList = new ArrayList<String>();
						tableMapList.add(table);
						columntablemap.put(columnname, tableMapList);
					}
				}
			}
		}

		return columntablemap;
	}



	private List<Long> getIdsFromTable(String tablename, Long lastId){
		String sql = "SELECT id FROM " + tablename + " WHERE" + " resource_id" + " = " + resource_id + " AND id > " + lastId + " ORDER BY id " + "LIMIT 1000";
		List<Long> idList =  jdbcTemplate.queryForList(sql, Long.class);
		return idList;

	}

	private void checkTablesforId(String tablename, String row_id, Map<String, List<String>> columnTableMap) throws ResourceIsNotDeletableException{ //done
		List<String> tablesWithResourceId = columnTableMap.get("resource_id");
		String column_id = tablename + "_id";
		if(columnTableMap.containsKey(column_id)){
			List<String> tableMapList = columnTableMap.get(column_id);
			for(String table : tableMapList){
				String sql = "SELECT id FROM " + table + " WHERE " + column_id + " = " + row_id + " AND " + "resource_id " + "!= " + resource_id + " LIMIT 1";
				List<String> selectedId = jdbcTemplate.queryForList(sql, String.class);
				if(!selectedId.isEmpty()){
					if(tablesWithResourceId.contains(table)){
						String resourcesql = "SELECT resource_id FROM " + table + " WHERE " + column_id + " = " + row_id + " AND " + "resource_id " + "!= " + resource_id + " LIMIT 1";
						List<String> blockingResourceId = jdbcTemplate.queryForList(resourcesql, String.class);
						if(!blockingResourceId.isEmpty()){	
							logger.debug("Blocking resource id is" + blockingResourceId.get(0));
							String resourcenamesql = "SELECT title from resource where id = " + blockingResourceId.get(0);
							List<String> blockingResourceName = jdbcTemplate.queryForList(resourcenamesql, String.class);
							if(!blockingResourceName.isEmpty()){
								logger.debug("Delete " + blockingResourceName.get(0) + " first");
								throw new ResourceIsNotDeletableException("Delete " + blockingResourceName.get(0) + " first");

							}else{
								throw new ResourceIsNotDeletableException("Delete resource with id " + blockingResourceId.get(0) + " first" );
							}
						}else{
							logger.debug("Resource not deleteable due to null resource_id");
							throw new ResourceIsNotDeletableException("Due to null resource_id");
						}
					}	
				}		

			}
		}
	}


	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkcontext) throws Exception {
		this.resource_id = (String) chunkcontext.getStepContext().getStepExecution()
				.getJobExecution().getJobParameters().getString("resource_id");

		Map<String, List<String>> columnTableMap = GetTablesContainingIdColumn();
		List<String> tablesWithResource = columnTableMap.get("resource_id");
		//reorder so taxon comes first, helps attempt to fail quickly
		if(tablesWithResource.contains("taxon")){
			tablesWithResource.remove("taxon");
			tablesWithResource.add(0, "taxon");
		}

		for(String table : tablesWithResource){	
			Long lastId = 0L;
			logger.debug("table with resouce_id is " + table );

			List<Long> idList = getIdsFromTable(table, lastId);
			while(idList != null && !idList.isEmpty()){
				logger.debug("Last Id Checked is" + lastId.toString());	
				lastId = idList.get(idList.size() -1);
				idList = getIdsFromTable(table, lastId);
				for(Long id : idList){
					checkTablesforId(table, id.toString(), columnTableMap);
				}
			}
		}
		return null; // marks the step as complete in spring batch
	}
}










