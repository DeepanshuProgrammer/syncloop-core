package com.eka.middleware.pooling;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBCPDataSource {
private static final Map<String,DBCPDataSource> dsMap = new ConcurrentHashMap<>();
private final BasicDataSource ds;
    private DBCPDataSource(String url,String user,String password, int pool, int timeOut) {
    	ds=new BasicDataSource();
    	//dsMap.put(url, ds);
        ds.setUrl(url);
        if(user!=null) {
	        ds.setUsername(user);
	        ds.setPassword(password);
        }
        ds.setMinIdle(pool/2);
        ds.setMaxIdle(pool+1);
        ds.setMaxOpenPreparedStatements((pool+1)*5);
        ds.setDefaultQueryTimeout(timeOut);
    }
    
    public static Connection getConnection(String name,String url,String user,String password, int pool,int timeOut) throws SQLException {
    	DBCPDataSource dbcp=dsMap.get(name);
    	if(dbcp==null || dbcp.ds.getDefaultQueryTimeout()!=timeOut || dbcp.ds.getMaxIdle()!=(pool+1)) {
    		dbcp=new DBCPDataSource(url, user, password, pool,timeOut);
    		dsMap.put(name,dbcp);
    	}
        return dbcp.ds.getConnection();
    }
}
