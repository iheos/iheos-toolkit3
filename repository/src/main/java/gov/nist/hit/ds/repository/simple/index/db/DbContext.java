package gov.nist.hit.ds.repository.simple.index.db;

import gov.nist.hit.ds.repository.api.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;


/**
 * 
 * @author Sunil.Bhaskarla
 *
 */
public class DbContext {
	private static Logger logger = Logger.getLogger(DbContext.class.getName());
	private Connection connection = null;
	private static boolean debugMode = !true;
	
	public DbContext(Connection connection) {
		this.connection = connection;
	}
	
	public DbContext() {
		
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}	
	
	public int getInt(String sqlStr) throws RepositoryException {
		int intVal = 0;
		try {
			if (connection!=null) {
				ResultSet rs = executeQuery(sqlStr);
				while (rs.next()) {
			          intVal = rs.getInt(1);
				}
				close(rs);				
			}

		} catch (SQLException e) {
			throw new RepositoryException("Error, Sqlstate:" + e.getSQLState() , e);
		}
		
		logger.fine("value: " + intVal);
		return intVal;
		
	}

	public String getString(String sqlStr) throws RepositoryException {
		String stringVal = "";
		try {
			if (connection!=null) {
				ResultSet rs = executeQuery(sqlStr);
				while (rs.next()) {
			          stringVal = rs.getString(1);
				}
				close(rs);				
			}

		} catch (SQLException e) {
			throw new RepositoryException("Error, Sqlstate:" + e.getSQLState() , e);
		}
		
		logger.fine("value: " + stringVal);
		return stringVal;
		
	}
	
	/**
	 * This method can be used for the following purposes 1) DDL or 2) Internal container manipulations WITHOUT any user-provided parameters
	 * @param sqlStr
	 * @return
	 * @throws SQLException
	 */
	public void internalCmd(String sqlStr) throws SQLException {
		
		logger.fine("IndexContainer SQL: " +sqlStr);
		
		if (connection!=null) {
			Statement statement = connection.createStatement();
			statement.execute(sqlStr);
			statement.close();
		}  else {
			throw new SQLException("No connection.");
		}

	}
	

	
	/**
	 * This method should be used for all user-provided values
	 * All key/value pairs are specified as strings in Java properties
	 * @param sqlStr
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int[] executePreparedId(String sqlStr, String[] params) throws SQLException {	

		logger.fine("IndexContainer SQL: " +sqlStr);
		if (connection!=null) {
			PreparedStatement statement = connection.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);
			int parameterIndex=1;
			for (String p : params) {
				logger.fine("Setting param: "+parameterIndex + " to <" + p + ">");			

				statement.setString(parameterIndex++, p);
			}
			int records = statement.executeUpdate();
			int idKey = -1;
			ResultSet rs = statement.getGeneratedKeys();
			
			if (rs!=null) {
				if (rs.next()) {
					idKey = rs.getInt(1);
				}
				statement.close();
				rs.close();
			}

			return new int[]{records,idKey};
			
		}
		throw new SQLException("No connection.");
	}
	

	public int executePrepared(String sqlStr, String[] params) throws SQLException {
	
		logger.fine("executePrepared IndexContainer SQL: " +sqlStr);
		if (connection!=null) {
			PreparedStatement statement = connection.prepareStatement(sqlStr);
			int parameterIndex=1;
			for (String p : params) {
				logger.fine("Setting param: "+parameterIndex + " to <" + p + ">");			

				statement.setString(parameterIndex++, p);
			}
			int records = statement.executeUpdate();
			statement.close();
			return records;
			
		}
		throw new SQLException("No connection.");
	}
	
	public PreparedStatement prepareBulkUpdate(String sqlStr) throws SQLException {
		logger.fine("executePrepared IndexContainer SQL: " +sqlStr);
		if (connection!=null) {
			return connection.prepareStatement(sqlStr);
		}
		throw new SQLException("No connection.");		
	}
	
	public boolean setBulkParameters(PreparedStatement ps, String[] params) throws SQLException {
		int parameterIndex=1;
		for (String p : params) {
			logger.fine("Setting param: "+parameterIndex + " to <" + p + ">");			

			ps.setString(parameterIndex++, p);
		}
		ps.addBatch();
		return true;

	}
	
	public boolean updateBulk(PreparedStatement ps) throws SQLException {
		if (connection!=null && ps!=null) {
			ps.executeBatch();
			return true;
		}
		
		throw new SQLException("No connection or ps is null.");
	}
	
	public ResultSet executeQuery(String sqlStr, String[] params) throws SQLException {

		logger.fine("IndexContainer SQL: " +sqlStr);
		if (connection!=null) {
			PreparedStatement statement = connection.prepareStatement(sqlStr);
			int parameterIndex=1;
			for (String p : params) {
				logger.fine("Setting param: "+parameterIndex + " to <" + p + ">");			

				statement.setString(parameterIndex++, p);
			}
			return statement.executeQuery();
		}
		throw new SQLException("No connection.");
	}

	public ResultSet executeQuery(String sqlStr) throws SQLException {

		logger.fine("IndexContainer SQL: "+sqlStr);
		
		PreparedStatement statement = connection.prepareStatement(sqlStr);
		return  statement.executeQuery();
		
	}	
	
	public void close(ResultSet rs) {		
		try {
			if (rs!=null)
				rs.close();
			this.close();
		} catch (SQLException e) {
			logger.fine(e.toString());
		}
		
	}
	
	public void close() {
		if (getConnection()!=null)
			try {
				getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public static boolean isDebugMode() {
		return debugMode;
	}
}
