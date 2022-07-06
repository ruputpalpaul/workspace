package com.jivesoftware.ps.addons.jep.clm.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;


public class DatabaseConnection {
	
	private final DBI dbi = new DBI("postgres://postgres:Hiy@1329@localHost:5432/mydb");
	List<String> result = null;
	
	List<String> getDatabase() {
		
		Handle h = dbi.open();
		
		List<String> result = h.createQuery("select * from test;")
					.map(String.class)
					.list();
		System.out.print(result);
        h.close();

        return result;
	}	
}
